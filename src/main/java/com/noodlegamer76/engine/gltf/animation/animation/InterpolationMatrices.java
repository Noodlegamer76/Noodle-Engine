package com.noodlegamer76.engine.gltf.animation.animation;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class InterpolationMatrices {
    private static final Vector3f TMP_TRANSLATION_A = new Vector3f();
    private static final Vector3f TMP_TRANSLATION_B = new Vector3f();
    private static final Vector3f TMP_SCALE_A = new Vector3f();
    private static final Vector3f TMP_SCALE_B = new Vector3f();
    private static final Quaternionf TMP_ROT_A = new Quaternionf();
    private static final Quaternionf TMP_ROT_B = new Quaternionf();

    public static Matrix4f interpolateMatrices(Matrix4f a, Matrix4f b, float t) {
        a.getTranslation(TMP_TRANSLATION_A);
        a.getUnnormalizedRotation(TMP_ROT_A);
        a.getScale(TMP_SCALE_A);

        b.getTranslation(TMP_TRANSLATION_B);
        b.getUnnormalizedRotation(TMP_ROT_B);
        b.getScale(TMP_SCALE_B);

        Vector3f translation = TMP_TRANSLATION_A.lerp(TMP_TRANSLATION_B, t, new Vector3f());
        Vector3f scale = TMP_SCALE_A.lerp(TMP_SCALE_B, t, new Vector3f());
        Quaternionf rotation = TMP_ROT_A.slerp(TMP_ROT_B, t, new Quaternionf());

        return new Matrix4f()
                .translationRotateScale(
                        translation,
                        rotation,
                        scale
                );
    }
}
