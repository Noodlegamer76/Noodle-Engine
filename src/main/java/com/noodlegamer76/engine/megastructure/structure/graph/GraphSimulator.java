package com.noodlegamer76.engine.megastructure.structure.graph;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;

import java.util.*;

public class GraphSimulator {

    public void run(StructureExecuter executer, StructureInstance instance) {
        run(executer, new ExecutionContext(), instance);
    }

    public void run(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        context.clear();
        Graph graph = executer.getFunction();
        ExecutionNode<?> entry = findEntryNode(graph);
        if (entry == null) return;
        runFrom(executer, context, entry, instance);
    }

    public void runFrom(StructureExecuter executer, ExecutionContext context, ExecutionNode<?> startNode, StructureInstance instance) {
        Graph graph = executer.getFunction();
        ExecutionNode<?> current = startNode;
        Set<Integer> visited = new HashSet<>();

        while (current != null) {
            if (!visited.add(current.getId())) {
                throw new GraphSimulationException(
                        "Cycle detected in execution flow at node: " + current.getId());
            }

            resolveDataInputs(executer, context, current, instance);
            current.execute(executer, context, instance);

            current = findNextExecutionNode(graph, current);
        }
    }

    public List<GenVar<?>> evaluateValues(StructureExecuter executer, ExecutionContext context, ValueNode<?> node, StructureInstance instance) {
        Graph graph = executer.getFunction();

        List<GenVar<?>> cached = context.getLocalVars(node.getId());
        if (cached != null) return cached;

        resolveDataInputs(executer, context, node, instance);

        List<GenVar<?>> result = node.evaluate(executer, context, instance);
        for (GenVar<?> var : result) {
            if (var != null) {
                if (var.isGlobal()) {
                    context.addGlobalVar(var);
                } else {
                    context.addCachedVar(node.getId(), var);
                }
            }
        }
        return result;
    }

    public ExecutionNode<?> findEntryNode(Graph graph) {
        return (ExecutionNode<?>) graph.getNodes().values().stream()
                .filter(n -> n instanceof ExecutionNode)
                .filter(n -> !hasIncomingExecutionLink(graph, n))
                .min(Comparator.comparingInt(Node::getId))
                .orElse(null);
    }

    private void resolveDataInputs(StructureExecuter executer, ExecutionContext context, Node<?> node, StructureInstance instance) {
        Graph graph = executer.getFunction();
        for (NodePin pin : node.getPins()) {
            if (pin.getKind() == PinKind.INPUT && pin.getCategory() == PinCategory.DATA) {
                Optional<NodePin> upstream = graph.getConnectedOutput(pin);
                upstream.ifPresent(outputPin -> {
                    Node<?> upstreamNode = graph.getNode(outputPin.getNodeId());
                    if (upstreamNode instanceof ValueNode) {
                        evaluateValues(executer, context, (ValueNode<?>) upstreamNode, instance);
                    }
                });
            }
        }
    }

    private ExecutionNode<?> findNextExecutionNode(Graph graph, ExecutionNode<?> current) {
        Optional<NodePin> execOutPin = current.getPins().stream()
                .filter(p -> p.getKind() == PinKind.OUTPUT && p.getCategory() == PinCategory.EXECUTION)
                .findFirst();

        if (execOutPin.isEmpty()) return null;

        List<NodePin> connectedInputs = graph.getConnectedInputs(execOutPin.get());
        if (connectedInputs.isEmpty()) return null;

        NodePin nextInput = connectedInputs.get(0);
        Node<?> nextNode = graph.getNode(nextInput.getNodeId());

        if (nextNode instanceof ExecutionNode) {
            return (ExecutionNode<?>) nextNode;
        }

        throw new GraphSimulationException(
                "Execution link leads to a non-ExecutionNode: " + nextNode.getId());
    }

    private boolean hasIncomingExecutionLink(Graph graph, Node<?> node) {
        for (NodePin pin : node.getPins()) {
            if (pin.getKind() == PinKind.INPUT && pin.getCategory() == PinCategory.EXECUTION) {
                if (graph.getConnectedOutput(pin).isPresent()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <V> V resolveInput(Graph graph, ExecutionContext context,
                                     NodePin inputPin, Class<V> type) {
        Optional<NodePin> upstream = graph.getConnectedOutput(inputPin);
        if (upstream.isEmpty()) return null;

        List<GenVar<?>> vars = context.getLocalVars(upstream.get().getNodeId());
        if (vars == null) return null;

        return vars.stream()
                .filter(var -> type.isInstance(var.getValue()))
                .map(var -> type.cast(var.getValue()))
                .findFirst()
                .orElse(null);
    }

    public static <V> V resolveInputByPin(Graph graph, ExecutionContext context,
                                          NodePin inputPin, Class<V> type) {
        Optional<NodePin> upstreamPin = graph.getConnectedOutput(inputPin);
        if (upstreamPin.isEmpty()) return null;

        List<GenVar<?>> vars = context.getLocalVars(upstreamPin.get().getNodeId());
        if (vars == null) return null;

        return vars.stream()
                .filter(var -> upstreamPin.get().getDataType() != null
                        && upstreamPin.get().getDataType().isInstance(var.getValue()))
                .map(var -> type.cast(var.getValue()))
                .findFirst()
                .orElse(null);
    }

    public static class GraphSimulationException extends RuntimeException {
        public GraphSimulationException(String message) {
            super(message);
        }

        public GraphSimulationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}