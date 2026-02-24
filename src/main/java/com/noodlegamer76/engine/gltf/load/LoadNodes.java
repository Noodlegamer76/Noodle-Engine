package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.McGltfLoader;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LoadNodes {
    public static void loadNodes(McGltfLoader gltf) {
        for (NodeModel nodeModel : gltf.getModel().getNodeModels()) {

            float[] localArr = new float[16];
            nodeModel.computeLocalTransform(localArr);
            Matrix4f localMatrix = new Matrix4f().set(localArr);

            Node node = new Node(localMatrix, gltf.getResult());
            gltf.addNodeModelToNode(nodeModel, node);

            List<MeshModel> meshModels = nodeModel.getMeshModels();
            for (MeshModel meshModel : meshModels) {
                MeshData data = gltf.getMeshModelToMeshData().get(meshModel);
                if (data != null) {
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
            List<NodeModel> children = nodeModel.getChildren();
            for (NodeModel child : children) {
                Node childNode = gltf.getNodeModelToNode().get(child);
                node.addChild(childNode);
            }
        }

        for (NodeModel nodeModel : gltf.getModel().getNodeModels()) {
            Node node = gltf.getNodeModelToNode().get(nodeModel);
            float[] globalArr = new float[16];
            nodeModel.computeGlobalTransform(globalArr);
            Matrix4f globalMatrix = new Matrix4f().set(globalArr);
            node.setGlobal(globalMatrix);
        }

        Set<Node> rootNodes = new LinkedHashSet<>();
        for (NodeModel nodeModel : gltf.getModel().getNodeModels()) {
            Node node = gltf.getNodeModelToNode().get(nodeModel);
            if (node.getParent() == null) {
                rootNodes.add(node);
            }
        }

        gltf.getResult().getRootNodes().addAll(rootNodes);
    }

}