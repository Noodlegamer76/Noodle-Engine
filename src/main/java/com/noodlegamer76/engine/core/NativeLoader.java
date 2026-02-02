package com.noodlegamer76.engine.core;

import java.io.*;
import java.nio.file.*;
import java.util.Locale;

public class NativeLoader {

    static {
       if (false) {
           try {
               String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
               String arch = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);

               String archDir = arch.contains("64") ? "x86_64" : "x86";

               if (os.contains("win")) {
                   loadNativeFromPath("/natives/windows/" + archDir + "/bulletjme.dll");
               } else if (os.contains("nix") || os.contains("nux")) {
                   loadNativeFromPath("/natives/linux/" + archDir + "/libbulletjme.so");
               } else if (os.contains("mac")) {
                   loadNativeFromPath("/natives/osx/" + archDir + "/libbulletjme.dylib");
               }
           } catch (IOException e) {
               throw new RuntimeException("Failed to extract native physics library", e);
           }
       }
    }

    public static void loadNativeFromPath(String resourcePath) throws IOException {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String path = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;

        if (!isCorrectExtensionForOS(path, os)) {
            System.err.println("Skipping: " + path + " is not compatible with " + os);
            return;
        }

        String fileName = path.substring(path.lastIndexOf('/') + 1);
        Path tempFile = Files.createTempFile("native-", fileName);

        try (InputStream is = NativeLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found at: " + path);
            }
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Loading native from: " + tempFile.toAbsolutePath());
        System.out.println("Exists: " + Files.exists(tempFile) + " Size: " + Files.size(tempFile));
        System.load(tempFile.toAbsolutePath().toString());
    }

    private static boolean isCorrectExtensionForOS(String path, String os) {
        if (os.contains("win")) {
            return path.endsWith(".dll");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return path.endsWith(".so");
        } else if (os.contains("mac")) {
            return path.endsWith(".dylib") || path.endsWith(".jnilib");
        }
        return false;
    }
}