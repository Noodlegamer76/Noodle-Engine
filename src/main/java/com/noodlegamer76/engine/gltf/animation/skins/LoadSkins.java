package com.noodlegamer76.engine.gltf.animation.skins;

import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.animation.animation.controller.Animator;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SkinModel;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;

public class LoadSkins {
    public static void loadSkins(McGltf gltf) {
        Map<SkinModel, Map<Node, Matrix4f>> allInvBindMaps = new HashMap<>();

        for (SkinModel skin : gltf.getModel().getSkinModels()) {
            Map<Node, Matrix4f> invBindMatrices = loadInverseBindMatrices(gltf, skin);

            allInvBindMaps.put(skin, invBindMatrices);
        }

        gltf.getInverseBindMatrices().putAll(allInvBindMaps);

        for (NodeModel nodeModel: gltf.getModel().getNodeModels()) {
            SkinModel skin = nodeModel.getSkinModel();
            if (skin == null) continue;
            for (MeshModel meshModel: nodeModel.getMeshModels()) {
                MeshData meshData = gltf.getMeshModelToMeshData().get(meshModel);
                meshData.setSkin(skin);
            }
        }
    }

    public static Map<Node, Matrix4f> loadInverseBindMatrices(McGltf gltf, SkinModel skin) {
        Map<Node, Matrix4f> invBindMap = new HashMap<>();

        AccessorModel invBindMatricesAcc = skin.getInverseBindMatrices();
        if (invBindMatricesAcc == null) return invBindMap;

        FloatBuffer invBindMatricesBuffer = null;
        Object buf = gltf.getAccessorBuffers().get(invBindMatricesAcc);

        if (buf instanceof ByteBuffer bb) {
            bb.order(ByteOrder.LITTLE_ENDIAN);
            invBindMatricesBuffer = bb.asFloatBuffer();
        } else if (buf instanceof FloatBuffer fb) {
            invBindMatricesBuffer = fb;
        }
        if (invBindMatricesBuffer == null) return invBindMap;

        List<NodeModel> jointModels = skin.getJoints();

        for (int i = 0; i < jointModels.size(); i++) {
            Node jointNode = gltf.getNodeModelToNode().get(jointModels.get(i));

            Matrix4f invBind = new Matrix4f().identity();

            if (invBindMatricesBuffer.capacity() >= (i + 1) * 16) {
                float[] arr = new float[16];
                invBindMatricesBuffer.position(i * 16);
                invBindMatricesBuffer.get(arr, 0, 16);

                invBind.set(arr);
            }

            invBindMap.put(jointNode, invBind);
        }

        return invBindMap;
    }

    public static void getSkinGlobal(RenderableMesh mesh) {
        SkinModel skin = mesh.getMeshData().getSkin();
        if (skin == null) return;

        List<Matrix4f> finalJointMatrices = new ArrayList<>();
        List<NodeModel> jointModels = skin.getJoints();

        for (int i = 0; i < jointModels.size(); i++) {
            Node jointNode = mesh.getGltf()
                    .getNodeModelToNode()
                    .get(jointModels.get(i));

            Matrix4f invBind = mesh.getGltf()
                    .getInverseBindMatrices()
                    .get(skin)
                    .get(jointNode);

            if (invBind == null) {
                throw new IllegalStateException("Missing inverse bind for joint: " + jointNode.getNodeModel().getName());
            }

            Matrix4f jointGlobal;

            if (mesh.getAnimator() != null) {
                Animator animator = mesh.getAnimator();
                jointGlobal = animator.getAnimatedGlobal(jointNode);
            } else {
                jointGlobal = jointNode.getGlobal();
            }


            Matrix4f skinMatrix = new Matrix4f(jointGlobal).mul(invBind);

            finalJointMatrices.add(skinMatrix);
        }

        mesh.setJointMatrices(finalJointMatrices);
    }
}