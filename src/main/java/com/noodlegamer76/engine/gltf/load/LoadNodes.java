package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class LoadNodes {
    public static void loadNodes(McGltf gltf) {
        for (NodeModel nodeModel : gltf.getModel().getNodeModels()) {

            float[] localArr = new float[16];
            nodeModel.computeLocalTransform(localArr);
            Matrix4f localMatrix = new Matrix4f().set(localArr);

            Node node = new Node(localMatrix, nodeModel);
            gltf.addNodeModelToNode(nodeModel, node);

            List<MeshModel> meshModels = nodeModel.getMeshModels();
            for (MeshModel meshModel : meshModels) {
                MeshData data = gltf.getMeshModelToMeshData().get(meshModel);
                if (data != null) {
                    gltf.addMeshToNode(data, node);
                    data.setNode(node);
                }
            }
        }

        for (NodeModel nodeModel : gltf.getModel().getNodeModels()) {
            Node childNode = gltf.getNodeModelToNode().get(nodeModel);
            NodeModel parentModel = nodeModel.getParent();

            if (parentModel != null) {
                Node parentNode = gltf.getNodeModelToNode().get(parentModel);
                childNode.setParent(parentNode);
            }
        }

        for (NodeModel nodeModel : gltf.getModel().getNodeModels()) {
            Node node = gltf.getNodeModelToNode().get(nodeModel);
            float[] globalArr = new float[16];
            nodeModel.computeGlobalTransform(globalArr);
            Matrix4f globalMatrix = new Matrix4f().set(globalArr);
            node.setGlobal(globalMatrix);
        }
    }

}