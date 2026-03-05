package csci.ooad.layout;

import java.awt.Point;
import java.util.*;

import csci.ooad.layout.intf.IMaze;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.alg.connectivity.ConnectivityInspector;

/**
 * Universal layout strategy that works for any maze topology,
 * including bidirectional (undirected) and unidirectional (directed) edges.
 *
 * Uses JGraphT's BiconnectivityInspector to detect structural groups,
 * then applies the appropriate layout per group type:
 *   - Ring (every node degree 2) → circle layout
 *   - Cluster (dense connectivity) → grid layout
 *   - Chain (linear sequence) → horizontal line layout
 */
public class MixedLayoutStrategy implements IRoomLayoutStrategy {

    public static Map<String, Set<String>> buildAdjacency(IMaze maze) {
        Map<String, Set<String>> adj = new HashMap<>();
        for (String room : maze.getRoomNames()) {
            adj.put(room, new HashSet<>(maze.getNeighborsOf(room)));
        }
        return adj;
    }

    private Map<String, Set<String>> adjacencyMap;

    // ─── Group Abstraction ────────────────────────────────────────────────────

    // Classifies how a group of rooms should be visually arranged
    private enum GroupType { RING, CLUSTER, CHAIN }

    // Holds a group of rooms, their type, and knows its own space requirements
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

        // Returns half the horizontal space this group needs for layout spacing
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

        // Calculates circle radius based on room count and size
        static int ringRadius(int n, int roomWidth) {
            return Math.max(roomWidth * n / 3, roomWidth + 20);
        }
    }

    // Entry point: builds graph, identifies groups, orders them, computes positions
    @Override
    public Map<String, Point> calculateRoomLocations(IMaze maze, Integer panelWidth,
                                                     Integer panelHeight, Integer roomWidth) {
        adjacencyMap = buildAdjacency(maze);
        Map<String, Point> result = new HashMap<>();
        Graph<String, DefaultEdge> graph = buildUndirectedGraph(maze.getRoomNames());

        // Find rings, clusters, and chains
        List<Group> groups = identifyGroups(graph);

        // Map each room to its group index for O(1) lookup
        Map<String, Integer> roomToGroup = new HashMap<>();
        for (int i = 0; i < groups.size(); i++) {
            for (String room : groups.get(i).getRooms()) {
                roomToGroup.put(room, i);
            }
        }

        // Determine left-to-right ordering via BFS on group connectivity
        List<Group> ordered = orderGroups(groups, roomToGroup);

        // How much horizontal space each group needs
        List<Integer> halfWidths = ordered.stream()
                .map(g -> g.halfWidth(roomWidth))
                .toList();

        // Shrink everything proportionally if total width exceeds panel
        int gap = roomWidth;
        int totalNeeded = halfWidths.stream().mapToInt(w -> w * 2).sum()
                + gap * (ordered.size() - 1);
        double scale = Math.min(1.0, (double)(panelWidth - roomWidth * 2) / Math.max(totalNeeded, 1));

        // Walk left-to-right placing each group's center point
        int cy = panelHeight / 2;
        double x = roomWidth;
        List<Point> groupCenters = new ArrayList<>();
        for (int i = 0; i < ordered.size(); i++) {
            int hw = halfWidths.get(i);
            x += hw * scale;
            groupCenters.add(new Point((int) x, cy));
            x += hw * scale + gap * scale;
        }

        // Place individual rooms around each group's center
        for (int i = 0; i < ordered.size(); i++) {
            layoutGroup(ordered.get(i), groupCenters.get(i), result,
                    roomWidth, panelWidth, panelHeight, scale);
        }

        return result;
    }

    // ─── Graph Construction ───────────────────────────────────────────────────

    // Converts the directed adjacency map into an undirected JGraphT graph
    // A→B and B→A collapse into a single undirected edge A—B
    private Graph<String, DefaultEdge> buildUndirectedGraph(Set<String> roomNames) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (String room : roomNames) {
            graph.addVertex(room);
        }
        for (String room : roomNames) {
            for (String neighbor : adjacencyMap.getOrDefault(room, Set.of())) {
                if (roomNames.contains(neighbor) && !graph.containsEdge(room, neighbor)) {
                    graph.addEdge(room, neighbor);
                }
            }
        }
        return graph;
    }

    // ─── Group Identification ─────────────────────────────────────────────────

    // Uses biconnected components to classify rooms into rings, clusters, and chains
    // Blocks with >2 nodes that all have degree 2 → ring
    // Blocks with >2 nodes and higher degree → cluster
    // Remaining unassigned rooms → chains (linked by bridge edges)
    private List<Group> identifyGroups(Graph<String, DefaultEdge> graph) {
        BiconnectivityInspector<String, DefaultEdge> inspector =
                new BiconnectivityInspector<>(graph);

        Set<Graph<String, DefaultEdge>> blocks = inspector.getBlocks();

        List<Group> groups = new ArrayList<>();
        Set<String> assignedRooms = new HashSet<>();

        // Classify blocks with 3+ nodes as ring or cluster
        for (Graph<String, DefaultEdge> block : blocks) {
            if (block.vertexSet().size() <= 2) continue;

            if (isRing(block)) {
                groups.add(new Group(traverseCycle(block), GroupType.RING));
            } else {
                groups.add(new Group(new ArrayList<>(block.vertexSet()), GroupType.CLUSTER));
            }
            assignedRooms.addAll(block.vertexSet());
        }

        // Rooms not in any ring/cluster form chains
        Set<String> chainRooms = new HashSet<>(graph.vertexSet());
        chainRooms.removeAll(assignedRooms);

        if (!chainRooms.isEmpty()) {
            // Split chain rooms into connected components, order each one
            Graph<String, DefaultEdge> chainSubgraph = new AsSubgraph<>(graph, chainRooms);
            for (Set<String> component : new ConnectivityInspector<>(chainSubgraph).connectedSets()) {
                groups.add(new Group(orderChain(component, chainSubgraph), GroupType.CHAIN));
            }
        }

        return groups;
    }

    // ─── Group Ordering ───────────────────────────────────────────────────────

    // BFS across groups to determine left-to-right placement order
    // Groups connected to each other appear adjacent in the layout
    private List<Group> orderGroups(List<Group> groups, Map<String, Integer> roomToGroup) {
        if (groups.size() <= 1) return groups;

        // Build adjacency between groups (if any room in group i neighbors a room in group j)
        Map<Integer, Set<Integer>> groupAdj = new HashMap<>();
        for (int i = 0; i < groups.size(); i++) groupAdj.put(i, new HashSet<>());

        for (int i = 0; i < groups.size(); i++) {
            for (String room : groups.get(i).getRooms()) {
                for (String neighbor : adjacencyMap.getOrDefault(room, Set.of())) {
                    int j = roomToGroup.getOrDefault(neighbor, -1);
                    if (j != -1 && j != i) {
                        groupAdj.get(i).add(j);
                    }
                }
            }
        }

        // BFS from first group — visit order becomes left-to-right order
        List<Group> ordered = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited.add(0);

        while (!queue.isEmpty()) {
            int idx = queue.poll();
            ordered.add(groups.get(idx));
            for (int neighbor : groupAdj.get(idx)) {
                if (visited.add(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }

        // Append any disconnected groups not reached by BFS
        for (int i = 0; i < groups.size(); i++) {
            if (!visited.contains(i)) ordered.add(groups.get(i));
        }

        return ordered;
    }

    // ─── Layout Dispatch ──────────────────────────────────────────────────────

    // Routes to the correct layout method based on group type
    private void layoutGroup(Group group, Point center, Map<String, Point> result,
                             int roomWidth, int panelWidth, int panelHeight, double scale) {
        switch (group.getType()) {
            case RING -> layoutRing(group, center, result, roomWidth, panelWidth, panelHeight);
            case CLUSTER -> layoutGrid(group, center, result, roomWidth, panelWidth, panelHeight);
            case CHAIN -> layoutChain(group, center, result, roomWidth, panelWidth, panelHeight, scale);
        }
    }

    // Places rooms evenly around a circle, starting from the top
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

    // Places rooms in a square-ish grid centered on the group center
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

    // Places rooms in a horizontal line centered on the group center
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

    // Checks if a block is a simple cycle: every node has exactly 2 neighbors
    private boolean isRing(Graph<String, DefaultEdge> block) {
        for (String room : block.vertexSet()) {
            if (block.degreeOf(room) != 2) return false;
        }
        return true;
    }

    // Walks an undirected cycle by always going to the neighbor we didn't come from
    private List<String> traverseCycle(Graph<String, DefaultEdge> block) {
        List<String> ordered = new ArrayList<>();
        String start = block.vertexSet().iterator().next();
        String prev = null;
        String current = start;
        do {
            ordered.add(current);
            List<String> neighbors = Graphs.neighborListOf(block, current);
            String next = neighbors.get(0).equals(prev) ? neighbors.get(1) : neighbors.get(0);
            prev = current;
            current = next;
        } while (!current.equals(start));
        return ordered;
    }

    // Orders chain rooms by starting from a degree-1 endpoint and walking the path
    private List<String> orderChain(Set<String> component, Graph<String, DefaultEdge> chainSubgraph) {
        Graph<String, DefaultEdge> sub = new AsSubgraph<>(chainSubgraph, component);

        // Find an endpoint (degree 1) to start from, or pick any node
        String start = component.stream()
                .filter(r -> sub.degreeOf(r) <= 1)
                .findFirst()
                .orElse(component.iterator().next());

        List<String> ordered = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        String current = start;
        while (current != null && visited.add(current)) {
            ordered.add(current);
            current = Graphs.neighborListOf(sub, current).stream()
                    .filter(n -> !visited.contains(n))
                    .findFirst()
                    .orElse(null);
        }
        return ordered;
    }

    // Keeps a value within [min, max] bounds to prevent rooms going off-panel
    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}