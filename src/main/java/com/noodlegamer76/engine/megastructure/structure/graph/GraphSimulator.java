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
    public static final int MAX_DEFERRED_EXECUTIONS = 1000;
    private int deferredExecutionCount = 0;
    private final List<ExecutionNode<?>> executeLast = new ArrayList<>();


    public void run(StructureExecuter executer, StructureInstance instance) {
        run(executer, new ExecutionContext(), instance);
    }

    public void run(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        executeLast.clear();
        deferredExecutionCount = 0;
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
        boolean transitioningToDeferred = false;

        while (current != null) {
            if (transitioningToDeferred) {
                visited.clear();
                invalidateAllNonCacheable(graph, context);
                transitioningToDeferred = false;
            }

            if (!visited.add(current.getId())) {
                throw new GraphSimulationException(
                        "Cycle detected in execution flow at node: " + current.getId());
            }

            resolveDataInputs(executer, context, current, instance);
            current.execute(executer, context, instance);

            ExecutionNode<?> next = findNextExecutionNode(graph, current, instance);

            if (next == null && !executeLast.isEmpty()) {
                transitioningToDeferred = true;
                next = extractEndNode(instance);
            }

            current = next;
        }
    }

    private void invalidateAllNonCacheable(Graph graph, ExecutionContext context) {
        for (Node<?> node : graph.getNodes().values()) {
            if (node instanceof ValueNode && !((ValueNode<?>) node).isCacheable()) {
                invalidateDownstream(graph, context, node);
            }
        }
    }

    public List<GenVar<?>> evaluateValues(StructureExecuter executer, ExecutionContext context, ValueNode<?> node, StructureInstance instance) {
        List<GenVar<?>> cached = context.getLocalVars(node.getId());
        if (cached != null && node.isCacheable()) return cached;

        if (!node.isCacheable()) {
            invalidateDownstream(executer.getFunction(), context, node);
        }

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

    private void invalidateDownstream(Graph graph, ExecutionContext context, Node<?> node) {
        for (NodePin pin : node.getPins()) {
            if (pin.getKind() == PinKind.OUTPUT) {
                for (NodePin connected : graph.getConnectedInputs(pin)) {
                    Node<?> downstream = graph.getNode(connected.getNodeId());
                    context.invalidateCachedVar(downstream.getId());
                    if (downstream instanceof ValueNode) {
                        invalidateDownstream(graph, context, downstream);
                    }
                }
            }
        }
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

    private ExecutionNode<?> findNextExecutionNode(Graph graph, ExecutionNode<?> current, StructureInstance instance) {
        String targetPinName = current.getNextExecutionPin(current.getLastContext());

        Optional<NodePin> execOutPin;

        if (targetPinName == null) {
            execOutPin = current.getPins().stream()
                    .filter(p -> p.getKind() == PinKind.OUTPUT && p.getCategory() == PinCategory.EXECUTION)
                    .findFirst();
        } else {
            execOutPin = current.getPins().stream()
                    .filter(p -> p.getKind() == PinKind.OUTPUT && p.getCategory() == PinCategory.EXECUTION)
                    .filter(p -> p.getDisplayName().equals(targetPinName))
                    .findFirst();
        }

        if (execOutPin.isEmpty()) return null;

        List<NodePin> connectedInputs = graph.getConnectedInputs(execOutPin.get());
        if (connectedInputs.isEmpty()) return null;

        NodePin nextInput = connectedInputs.get(0);
        Node<?> nextNode = graph.getNode(nextInput.getNodeId());

        if (nextNode instanceof ExecutionNode) return (ExecutionNode<?>) nextNode;

        throw new GraphSimulationException(
                "Execution link leads to a non-ExecutionNode: " + nextNode.getId());
    }

    private ExecutionNode<?> extractEndNode(StructureInstance instance) {
        deferredExecutionCount++;
        if (deferredExecutionCount > MAX_DEFERRED_EXECUTIONS || executeLast.isEmpty()) {
            return null;
        }
        return executeLast.remove(0);
    }

    public void scheduleAfter(ExecutionNode<?> node) {
        executeLast.add(node);
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

    //I might be the best coder ever
    public static <V> V resolveInputByPin(Graph graph, ExecutionContext context,
                                          NodePin inputPin, Class<V> type) {
        Optional<NodePin> upstreamPin = graph.getConnectedOutput(inputPin);
        if (upstreamPin.isEmpty()) return null;

        List<GenVar<?>> vars = context.getLocalVars(upstreamPin.get().getNodeId());
        if (vars == null) return null;

        String pinName = upstreamPin.get().getDisplayName();

        Optional<V> named = vars.stream()
                .filter(var -> var.getName().equals(pinName))
                .filter(var -> type.isInstance(var.getValue()))
                .map(var -> type.cast(var.getValue()))
                .findFirst();

        return named.orElseGet(() -> vars.stream()
                .filter(var -> type.isInstance(var.getValue()))
                .map(var -> type.cast(var.getValue()))
                .findFirst()
                .orElse(null));
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