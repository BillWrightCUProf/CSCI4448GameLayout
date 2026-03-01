package csci.ooad.layout;

import java.awt.Point;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Universal layout strategy that works for any maze topology.
 *
 * Approach:
 * 1. Use Tarjan's SCC to find structural groups (rings, fully-connected clusters, chains)
 * 2. Treat each group as a unit and space them horizontally with proper sizing
 * 3. Layout internals of each group with the right algorithm (circle, grid, line)
 */
public class MixedLayoutStrategy implements IRoomLayoutStrategy {

    private Map<String, Set<String>> adjacencyMap;

    // ─── Group Abstraction ────────────────────────────────────────────────────

    private enum GroupType { RING, CLUSTER, CHAIN }

    private static class Group {
        private final List<String> rooms;
        private final GroupType type;

        Group(Collection<String> rooms, GroupType type) {
            this.rooms = new ArrayList<>(rooms);
            this.type = type;
        }

        List<String> getRooms() { return rooms; }
        Set<String> getRoomSet() { return new HashSet<>(rooms); }
        GroupType getType() { return type; }
        int size() { return rooms.size(); }
        boolean containsRoom(String room) { return rooms.contains(room); }

        int halfWidth(int roomWidth) {
            int n = rooms.size();
            return switch (type) {
                case RING -> ringRadius(n, roomWidth) + roomWidth / 2;
                case CLUSTER -> {
                    int cols = (int) Math.ceil(Math.sqrt(n));
                    yield (cols * (roomWidth + 20)) / 2 + roomWidth / 2;
                }
                case CHAIN -> (n * (roomWidth + 20)) / 2;
            };
        }

        private static int ringRadius(int n, int roomWidth) {
            return Math.max(roomWidth * n / 3, roomWidth + 20);
        }
    }

    // ─── Room-to-Group Index ──────────────────────────────────────────────────

    private static class GroupIndex {
        private final Map<String, Integer> roomToGroupIndex = new HashMap<>();

        void register(int groupIdx, Collection<String> rooms) {
            for (String room : rooms) {
                roomToGroupIndex.put(room, groupIdx);
            }
        }

        int findGroupOf(String room) {
            return roomToGroupIndex.getOrDefault(room, -1);
        }
    }

    // ─── Public API ───────────────────────────────────────────────────────────

    public void setAdjacencyMap(Map<String, Set<String>> adjacencyMap) {
        this.adjacencyMap = adjacencyMap;
    }

    @Override
    public Map<String, Point> calculateRoomLocations(Set<String> roomNames, Integer panelWidth,
                                                     Integer panelHeight, Integer roomWidth) {
        return computeLayout(roomNames, panelWidth, panelHeight, roomWidth);
    }

    // ─── Main Layout ─────────────────────────────────────────────────────────

    private Map<String, Point> computeLayout(Set<String> roomNames, int panelWidth,
                                             int panelHeight, int roomWidth) {
        Map<String, Point> result = new HashMap<>();

        // Step 1: Identify groups from SCCs and chains
        List<Group> groups = identifyGroups(roomNames);

        // Step 2: Build room-to-group index for fast lookup
        GroupIndex groupIndex = new GroupIndex();
        for (int i = 0; i < groups.size(); i++) {
            groupIndex.register(i, groups.get(i).getRooms());
        }

        // Step 3: Order groups left-to-right by connectivity
        List<Group> ordered = orderGroups(groups, groupIndex);

        // Step 4: Calculate spacing and scale
        List<Integer> halfWidths = ordered.stream()
                .map(g -> g.halfWidth(roomWidth))
                .toList();

        int gap = roomWidth;
        int totalNeeded = halfWidths.stream().mapToInt(w -> w * 2).sum()
                + gap * (ordered.size() - 1);
        double scale = Math.min(1.0, (double)(panelWidth - roomWidth * 2) / Math.max(totalNeeded, 1));

        // Step 5: Place group centers horizontally
        int cy = panelHeight / 2;
        double x = roomWidth;
        List<Point> groupCenters = new ArrayList<>();
        for (int i = 0; i < ordered.size(); i++) {
            int hw = halfWidths.get(i);
            x += hw * scale;
            groupCenters.add(new Point((int) x, cy));
            x += hw * scale + gap * scale;
        }

        // Step 6: Layout each group's internals around its center
        for (int i = 0; i < ordered.size(); i++) {
            layoutGroup(ordered.get(i), groupCenters.get(i), result,
                    roomWidth, panelWidth, panelHeight, scale);
        }

        return result;
    }

    // ─── Group Identification ─────────────────────────────────────────────────

    private List<Group> identifyGroups(Set<String> roomNames) {
        List<Set<String>> sccs = findAllSCCs(roomNames);
        List<Group> groups = new ArrayList<>();

        Set<String> chainRooms = new HashSet<>();
        for (Set<String> scc : sccs) {
            if (scc.size() > 1) {
                GroupType type = isRingCluster(scc) ? GroupType.RING : GroupType.CLUSTER;
                groups.add(new Group(scc, type));
            } else {
                chainRooms.addAll(scc);
            }
        }

        for (List<String> chain : buildOrderedChains(chainRooms)) {
            groups.add(new Group(chain, GroupType.CHAIN));
        }

        return groups;
    }

    // ─── Group Ordering ───────────────────────────────────────────────────────

    private List<Group> orderGroups(List<Group> groups, GroupIndex groupIndex) {
        if (groups.isEmpty()) return groups;

        int n = groups.size();
        Map<Integer, Set<Integer>> groupAdj = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int i = 0; i < n; i++) {
            groupAdj.put(i, new HashSet<>());
            inDegree.put(i, 0);
        }

        for (int i = 0; i < n; i++) {
            for (String room : groups.get(i).getRooms()) {
                for (String neighbor : adjacencyMap.getOrDefault(room, Set.of())) {
                    int j = groupIndex.findGroupOf(neighbor);
                    if (j != -1 && j != i && groupAdj.get(i).add(j)) {
                        inDegree.merge(j, 1, Integer::sum);
                    }
                }
            }
        }

        // Kahn's topological sort
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree.get(i) == 0) queue.add(i);
        }

        List<Group> ordered = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            int idx = queue.poll();
            if (!visited.add(idx)) continue;
            ordered.add(groups.get(idx));
            for (int neighbor : groupAdj.get(idx)) {
                inDegree.merge(neighbor, -1, Integer::sum);
                if (inDegree.get(neighbor) == 0) queue.add(neighbor);
            }
        }

        // Add any remaining disconnected groups
        for (int i = 0; i < n; i++) {
            if (!visited.contains(i)) ordered.add(groups.get(i));
        }

        return ordered;
    }

    // ─── Group Internal Layout (dispatch by type) ─────────────────────────────

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
        List<String> ordered = orderByDirectedEdges(group.getRooms());
        int n = ordered.size();
        int radius = Group.ringRadius(n, roomWidth);

        for (int i = 0; i < n; i++) {
            double angle = i * (2 * Math.PI / n) - Math.PI / 2;
            int x = clamp(center.x + (int)(radius * Math.cos(angle)), roomWidth, panelWidth - roomWidth);
            int y = clamp(center.y + (int)(radius * Math.sin(angle)), roomWidth, panelHeight - roomWidth);
            result.put(ordered.get(i), new Point(x, y));
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
        int spacing = roomWidth + 20;
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
        int spacing = (int)((roomWidth + 20) * Math.max(scale, 0.5));
        int startX = center.x - (n * spacing) / 2 + spacing / 2;

        for (int i = 0; i < n; i++) {
            int x = clamp(startX + i * spacing, roomWidth, panelWidth - roomWidth);
            result.put(chain.get(i), new Point(x, center.y));
        }
    }

    // ─── Chain Building ───────────────────────────────────────────────────────

    private List<List<String>> buildOrderedChains(Set<String> chainRooms) {
        List<List<String>> chains = new ArrayList<>();
        if (chainRooms.isEmpty()) return chains;

        Map<String, Integer> inDegree = new HashMap<>();
        for (String r : chainRooms) inDegree.put(r, 0);
        for (String r : chainRooms) {
            for (String n : adjacencyMap.getOrDefault(r, Set.of())) {
                if (chainRooms.contains(n)) inDegree.merge(n, 1, Integer::sum);
            }
        }

        Set<String> unvisited = new HashSet<>(chainRooms);
        while (!unvisited.isEmpty()) {
            String start = unvisited.stream()
                    .filter(r -> inDegree.getOrDefault(r, 0) == 0)
                    .findFirst().orElse(unvisited.iterator().next());

            List<String> chain = new ArrayList<>();
            String current = start;
            while (current != null && unvisited.contains(current)) {
                chain.add(current);
                unvisited.remove(current);
                String next = null;
                for (String neighbor : adjacencyMap.getOrDefault(current, Set.of())) {
                    if (unvisited.contains(neighbor) && chainRooms.contains(neighbor)) {
                        next = neighbor;
                        break;
                    }
                }
                current = next;
            }
            if (!chain.isEmpty()) chains.add(chain);
        }
        return chains;
    }

    // ─── Tarjan SCC ───────────────────────────────────────────────────────────

    private List<Set<String>> findAllSCCs(Set<String> roomNames) {
        Map<String, Integer> index = new HashMap<>();
        Map<String, Integer> lowlink = new HashMap<>();
        Map<String, Boolean> onStack = new HashMap<>();
        Deque<String> stack = new ArrayDeque<>();
        List<Set<String>> sccs = new ArrayList<>();
        int[] counter = {0};

        for (String room : roomNames) {
            if (!index.containsKey(room)) {
                tarjan(room, index, lowlink, onStack, stack, sccs, counter, roomNames);
            }
        }
        return sccs;
    }

    private void tarjan(String node, Map<String, Integer> index, Map<String, Integer> lowlink,
                        Map<String, Boolean> onStack, Deque<String> stack,
                        List<Set<String>> sccs, int[] counter, Set<String> allRooms) {
        index.put(node, counter[0]);
        lowlink.put(node, counter[0]);
        counter[0]++;
        stack.push(node);
        onStack.put(node, true);

        for (String neighbor : adjacencyMap.getOrDefault(node, Set.of())) {
            if (!allRooms.contains(neighbor)) continue;
            if (!index.containsKey(neighbor)) {
                tarjan(neighbor, index, lowlink, onStack, stack, sccs, counter, allRooms);
                lowlink.put(node, Math.min(lowlink.get(node), lowlink.get(neighbor)));
            } else if (Boolean.TRUE.equals(onStack.get(neighbor))) {
                lowlink.put(node, Math.min(lowlink.get(node), index.get(neighbor)));
            }
        }

        if (lowlink.get(node).equals(index.get(node))) {
            Set<String> scc = new HashSet<>();
            while (true) {
                String w = stack.pop();
                onStack.put(w, false);
                scc.add(w);
                if (w.equals(node)) break;
            }
            sccs.add(scc);
        }
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private boolean isRingCluster(Set<String> cluster) {
        // Precompute in-degree within the cluster in a single pass
        Map<String, Integer> inDegree = new HashMap<>();
        for (String room : cluster) inDegree.put(room, 0);
        for (String room : cluster) {
            for (String neighbor : adjacencyMap.getOrDefault(room, Set.of())) {
                if (cluster.contains(neighbor)) {
                    inDegree.merge(neighbor, 1, Integer::sum);
                }
            }
        }

        for (String room : cluster) {
            long outDegree = adjacencyMap.getOrDefault(room, Set.of()).stream()
                    .filter(cluster::contains).count();
            if (outDegree != 1 || inDegree.get(room) != 1) return false;
        }
        return true;
    }

    private List<String> orderByDirectedEdges(List<String> rooms) {
        if (rooms.size() <= 2) return rooms;
        Set<String> roomSet = new HashSet<>(rooms);
        List<String> ordered = new ArrayList<>();
        Set<String> remaining = new HashSet<>(rooms);
        String current = rooms.get(0);

        while (!remaining.isEmpty()) {
            ordered.add(current);
            remaining.remove(current);
            String next = null;
            for (String n : adjacencyMap.getOrDefault(current, Set.of())) {
                if (remaining.contains(n) && roomSet.contains(n)) {
                    next = n;
                    break;
                }
            }
            if (next == null) break;
            current = next;
        }
        return ordered;
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}