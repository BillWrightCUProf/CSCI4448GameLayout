package csci.ooad.layout;

import java.awt.Point;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 * Universal layout strategy that works for any maze topology.
 *
 * Approach:
 * 1. Use JGraphT's SCC detection to find structural groups (rings, clusters, chains)
 * 2. Use topological sorting to order groups left-to-right
 * 3. Layout internals of each group with the right algorithm (circle, grid, line)
 */
public class MixedLayoutStrategy implements IRoomLayoutStrategy {

    private final Map<String, Set<String>> adjacencyMap;

    // ─── Group Abstraction ────────────────────────────────────────────────────

    private enum GroupType { RING, CLUSTER, CHAIN }

    private static class Group {
        private final List<String> rooms;
        private final GroupType type;

        Group(List<String> rooms, GroupType type) {
            this.rooms = rooms;
            this.type = type;
        }

        List<String> getRooms() { return rooms; }
        GroupType getType() { return type; }
        int size() { return rooms.size(); }

        int halfWidth(int roomWidth) {
            int n = rooms.size();
            int spacing = roomWidth * 2;
            return switch (type) {
                case RING -> ringRadius(n, roomWidth) + roomWidth / 2;
                case CLUSTER -> {
                    int cols = (int) Math.ceil(Math.sqrt(n));
                    yield (cols * spacing) / 2 + roomWidth / 2;
                }
                case CHAIN -> (n * spacing) / 2;
            };
        }

        static int ringRadius(int n, int roomWidth) {
            return Math.max(roomWidth * n / 3, roomWidth + 20);
        }
    }

    // ─── Constructor ──────────────────────────────────────────────────────────

    public MixedLayoutStrategy(Map<String, Set<String>> adjacencyMap) {
        this.adjacencyMap = adjacencyMap;
    }

    @Override
    public Map<String, Point> calculateRoomLocations(Set<String> roomNames, Integer panelWidth,
                                                     Integer panelHeight, Integer roomWidth) {
        return computeLayout(roomNames, panelWidth, panelHeight, roomWidth);
    }

    // ─── Graph Construction ───────────────────────────────────────────────────

    private Graph<String, DefaultEdge> buildGraph(Set<String> roomNames) {
        Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (String room : roomNames) {
            graph.addVertex(room);
        }
        for (String room : roomNames) {
            for (String neighbor : adjacencyMap.getOrDefault(room, Set.of())) {
                if (roomNames.contains(neighbor)) {
                    graph.addEdge(room, neighbor);
                }
            }
        }
        return graph;
    }

    // ─── Main Layout ─────────────────────────────────────────────────────────

    private Map<String, Point> computeLayout(Set<String> roomNames, int panelWidth,
                                             int panelHeight, int roomWidth) {
        Map<String, Point> result = new HashMap<>();
        Graph<String, DefaultEdge> graph = buildGraph(roomNames);

        // Step 1: Identify groups
        List<Group> groups = identifyGroups(graph);

        // Step 2: Build room-to-group index
        Map<String, Integer> roomToGroup = new HashMap<>();
        for (int i = 0; i < groups.size(); i++) {
            for (String room : groups.get(i).getRooms()) {
                roomToGroup.put(room, i);
            }
        }

        // Step 3: Order groups left-to-right
        List<Group> ordered = orderGroups(groups, roomToGroup);

        // Step 4: Calculate spacing and scale
        List<Integer> halfWidths = ordered.stream()
                .map(g -> g.halfWidth(roomWidth))
                .toList();

        int gap = roomWidth;
        int totalNeeded = halfWidths.stream().mapToInt(w -> w * 2).sum()
                + gap * (ordered.size() - 1);
        double scale = Math.min(1.0, (double)(panelWidth - roomWidth * 2) / Math.max(totalNeeded, 1));

        // Step 5: Place group centers
        int cy = panelHeight / 2;
        double x = roomWidth;
        List<Point> groupCenters = new ArrayList<>();
        for (int i = 0; i < ordered.size(); i++) {
            int hw = halfWidths.get(i);
            x += hw * scale;
            groupCenters.add(new Point((int) x, cy));
            x += hw * scale + gap * scale;
        }

        // Step 6: Layout each group
        for (int i = 0; i < ordered.size(); i++) {
            layoutGroup(ordered.get(i), groupCenters.get(i), result,
                    roomWidth, panelWidth, panelHeight, scale);
        }

        return result;
    }

    // ─── Group Identification ─────────────────────────────────────────────────

    private List<Group> identifyGroups(Graph<String, DefaultEdge> graph) {
        List<Set<String>> sccs = new KosarajuStrongConnectivityInspector<>(graph)
                .stronglyConnectedSets();

        List<Group> groups = new ArrayList<>();
        Set<String> chainRooms = new HashSet<>();

        for (Set<String> scc : sccs) {
            if (scc.size() > 1) {
                Graph<String, DefaultEdge> subgraph = new AsSubgraph<>(graph, scc);
                GroupType type = isRing(subgraph) ? GroupType.RING : GroupType.CLUSTER;
                List<String> ordered = type == GroupType.RING
                        ? traverseCycle(subgraph) : new ArrayList<>(scc);
                groups.add(new Group(ordered, type));
                System.out.println("Identified " + type + " group: " + ordered);
            } else {
                chainRooms.addAll(scc);
            }
        }

        if (!chainRooms.isEmpty()) {
            Graph<String, DefaultEdge> chainSubgraph = new AsSubgraph<>(graph, chainRooms);
            for (Set<String> component : new ConnectivityInspector<>(chainSubgraph).connectedSets()) {
                Graph<String, DefaultEdge> compGraph = new AsSubgraph<>(graph, component);
                groups.add(new Group(topologicalOrder(compGraph), GroupType.CHAIN));
            }
        }

        return groups;
    }

    // ─── Group Ordering ───────────────────────────────────────────────────────

    private List<Group> orderGroups(List<Group> groups, Map<String, Integer> roomToGroup) {
        if (groups.isEmpty()) return groups;

        Graph<Integer, DefaultEdge> metaGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (int i = 0; i < groups.size(); i++) {
            metaGraph.addVertex(i);
        }

        for (int i = 0; i < groups.size(); i++) {
            for (String room : groups.get(i).getRooms()) {
                for (String neighbor : adjacencyMap.getOrDefault(room, Set.of())) {
                    int j = roomToGroup.getOrDefault(neighbor, -1);
                    if (j != -1 && j != i && !metaGraph.containsEdge(i, j)) {
                        metaGraph.addEdge(i, j);
                    }
                }
            }
        }

        try {
            List<Group> ordered = new ArrayList<>();
            new TopologicalOrderIterator<>(metaGraph)
                    .forEachRemaining(idx -> ordered.add(groups.get(idx)));
            return ordered;
        } catch (Exception e) {
            return groups;
        }
    }

    // ─── Layout Dispatch ──────────────────────────────────────────────────────

    private void layoutGroup(Group group, Point center, Map<String, Point> result,
                             int roomWidth, int panelWidth, int panelHeight, double scale) {
        switch (group.getType()) {
            case RING -> layoutRing(group, center, result, roomWidth, panelWidth, panelHeight);
            case CLUSTER -> layoutGrid(group, center, result, roomWidth, panelWidth, panelHeight);
            case CHAIN -> layoutChain(group, center, result, roomWidth, panelWidth, panelHeight, scale);
        }
    }

    private void layoutRing(Group group, Point center, Map<String, Point> result,
                            int roomWidth, int panelWidth, int panelHeight) {
        List<String> rooms = group.getRooms();
        int n = rooms.size();
        int radius = Group.ringRadius(n, roomWidth);

        for (int i = 0; i < n; i++) {
            double angle = i * (2 * Math.PI / n) - Math.PI / 2;
            int x = clamp(center.x + (int)(radius * Math.cos(angle)), roomWidth, panelWidth - roomWidth);
            int y = clamp(center.y + (int)(radius * Math.sin(angle)), roomWidth, panelHeight - roomWidth);
            result.put(rooms.get(i), new Point(x, y));
        }
    }

    private void layoutGrid(Group group, Point center, Map<String, Point> result,
                            int roomWidth, int panelWidth, int panelHeight) {
        List<String> rooms = group.getRooms();
        int n = rooms.size();

        if (n == 1) {
            result.put(rooms.get(0), center);
            return;
        }

        int cols = (int) Math.ceil(Math.sqrt(n));
        int spacing = roomWidth * 2;
        int startX = center.x - (cols * spacing) / 2;
        int startY = center.y - ((n / cols) * spacing) / 2;

        for (int i = 0; i < n; i++) {
            int x = clamp(startX + (i % cols) * spacing, roomWidth, panelWidth - roomWidth);
            int y = clamp(startY + (i / cols) * spacing, roomWidth, panelHeight - roomWidth);
            result.put(rooms.get(i), new Point(x, y));
        }
    }

    private void layoutChain(Group group, Point center, Map<String, Point> result,
                             int roomWidth, int panelWidth, int panelHeight, double scale) {
        List<String> chain = group.getRooms();
        int n = chain.size();
        int spacing = (int)((roomWidth * 2) * Math.max(scale, 0.5));
        int startX = center.x - (n * spacing) / 2 + spacing / 2;

        for (int i = 0; i < n; i++) {
            int x = clamp(startX + i * spacing, roomWidth, panelWidth - roomWidth);
            result.put(chain.get(i), new Point(x, center.y));
        }
    }

    // ─── Graph Helpers ───────────────────────────────────────────────────────

    private boolean isRing(Graph<String, DefaultEdge> subgraph) {
        for (String room : subgraph.vertexSet()) {
            if (subgraph.inDegreeOf(room) != 1 || subgraph.outDegreeOf(room) != 1) return false;
        }
        return true;
    }

    private List<String> traverseCycle(Graph<String, DefaultEdge> subgraph) {
        List<String> ordered = new ArrayList<>();
        String start = subgraph.vertexSet().iterator().next();
        String current = start;
        do {
            ordered.add(current);
            current = Graphs.successorListOf(subgraph, current).get(0);
        } while (!current.equals(start));
        return ordered;
    }

    private List<String> topologicalOrder(Graph<String, DefaultEdge> subgraph) {
        List<String> ordered = new ArrayList<>();
        new TopologicalOrderIterator<>(subgraph).forEachRemaining(ordered::add);
        return ordered;
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}