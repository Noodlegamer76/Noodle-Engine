package com.noodlegamer76.engine.megastructure.structure.graph;

import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;

import java.util.*;

/**
 * Simulates execution of a {@link Graph}.
 *
 * <h3>Execution Model</h3>
 * <ol>
 *   <li>Simulation starts from an <em>entry node</em> — the first {@link ExecutionNode}
 *       whose EXECUTION INPUT pin has no incoming link (i.e. nothing drives it).</li>
 *   <li>Each {@link ExecutionNode} is executed in turn. After execution the simulator
 *       follows the EXECUTION OUTPUT pin to find the next node.</li>
 *   <li>Whenever an {@link ExecutionNode} (or any node) needs the value on one of its
 *       DATA INPUT pins, the simulator walks backwards through DATA links and calls
 *       {@link ValueNode#evaluate} on the upstream node, caching the result in the
 *       {@link ExecutionContext} so each value node is evaluated at most once per run.</li>
 * </ol>
 */
public class GraphSimulator {

    /**
     * Runs the graph from its natural entry point with a fresh context.
     *
     * @param graph the graph to simulate
     * @throws GraphSimulationException if the graph is malformed or no entry node exists
     */
    public void run(Graph graph) {
        run(graph, new ExecutionContext());
    }

    /**
     * Runs the graph from its natural entry point using the supplied context.
     * The context is cleared before execution begins so cached values from a
     * previous run do not leak in.
     *
     * @param graph   the graph to simulate
     * @param context execution context (globals are preserved; value cache is cleared)
     */
    public void run(Graph graph, ExecutionContext context) {
        context.clear();
        ExecutionNode<?> entry = findEntryNode(graph);
        runFrom(graph, context, entry);
    }

    /**
     * Runs the graph starting from a specific {@link ExecutionNode}.
     * Useful when you want to resume from a known node or trigger a sub-graph.
     */
    public void runFrom(Graph graph, ExecutionContext context, ExecutionNode<?> startNode) {
        ExecutionNode<?> current = startNode;
        Set<Integer> visited = new HashSet<>();

        while (current != null) {
            if (!visited.add(current.getId())) {
                throw new GraphSimulationException(
                        "Cycle detected in execution flow at node: " + current.getId());
            }

            resolveDataInputs(graph, context, current);
            current.execute(graph, context);

            current = findNextExecutionNode(graph, current);
        }
    }

    /**
     * Evaluates a single {@link ValueNode}, resolving its upstream data
     * dependencies first. The result is cached in the context.
     *
     * @return the evaluated value (may be {@code null} if the node returns null)
     */
    public List<GenVar<?>> evaluateValues(Graph graph, ExecutionContext context, ValueNode<?> node) {
        List<GenVar<?>>  cached = context.getLocalVars(node.getId());
        if (cached != null) {
            return cached;
        }

        resolveDataInputs(graph, context, node);

        List<GenVar<?>>  result = node.evaluate(graph, context);
        for (GenVar<?> var : result) {
            if (var != null) {
                if (var.isGlobal()) {
                    context.addGlobalVar(var);
                }
                else {
                    context.addCachedVar(node.getId(), var);
                }
            }
        }
        return result;
    }

    /**
     * Finds the entry {@link ExecutionNode}: the execution node whose EXECUTION
     * INPUT pin has no incoming link. If multiple candidates exist the one with
     * the lowest node ID is chosen for determinism.
     */
    public ExecutionNode<?> findEntryNode(Graph graph) {
        return graph.getNodes().values().stream()
                .filter(n -> n instanceof ExecutionNode)
                .filter(n -> !hasIncomingExecutionLink(graph, n))
                .min(Comparator.comparingInt(Node::getId))
                .map(n -> (ExecutionNode<?>) n)
                .orElseThrow(() -> new GraphSimulationException(
                        "No entry ExecutionNode found in graph '" + graph.getName() + "'. " +
                                "Every ExecutionNode has an incoming execution link (cycle?) " +
                                "or the graph contains no ExecutionNodes."));
    }

    /**
     * For every DATA INPUT pin on {@code node}, traces back through links to the
     * connected VALUE node and evaluates it, storing the result in the context
     * keyed by the <em>upstream node's</em> ID. The executing node can then
     * retrieve those values by calling {@link #resolveInput}.
     */
    private void resolveDataInputs(Graph graph, ExecutionContext context, Node<?> node) {
        for (NodePin pin : node.getPins()) {
            if (pin.getKind() == PinKind.INPUT && pin.getCategory() == PinCategory.DATA) {
                Optional<NodePin> upstream = graph.getConnectedOutput(pin);
                upstream.ifPresent(outputPin -> {
                    Node<?> upstreamNode = graph.getNode(outputPin.getNodeId());
                    if (upstreamNode instanceof ValueNode) {
                        evaluateValues(graph, context, (ValueNode<?>) upstreamNode);
                    }
                });
            }
        }
    }

    /**
     * Follows the EXECUTION OUTPUT pin of the given node to find the next
     * {@link ExecutionNode}, or returns {@code null} if there is none (end of flow).
     */
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

    /** Returns true if {@code node} has at least one incoming EXECUTION link. */
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

    /**
     * Helper that nodes can call during {@code execute()} or {@code evaluate()}
     * to retrieve the resolved value feeding a specific DATA INPUT pin.
     *
     * <pre>{@code
     * // Inside an ExecutionNode:
     * Integer myInt = GraphSimulator.resolveInput(graph, context, myInputPin, Integer.class);
     * }</pre>
     *
     * @param graph    the graph
     * @param context  the current execution context
     * @param inputPin the DATA INPUT pin whose upstream value you want
     * @param type     expected type — used only for the cast
     * @param <V>      value type
     * @return the upstream value, or {@code null} if the pin is unconnected
     * @throws ClassCastException if the upstream value is not assignable to {@code type}
     */
    public static <V> V resolveInput(Graph graph, ExecutionContext context,
                                     NodePin inputPin, Class<V> type) {
        Optional<NodePin> upstream = graph.getConnectedOutput(inputPin);
        if (upstream.isEmpty()) return null;

        Object cached = context.getLocalVars(upstream.get().getNodeId());
        if (cached == null) return null;

        return type.cast(cached);
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