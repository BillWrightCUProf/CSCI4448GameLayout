package csci.ooad.layout.example;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.MazeObserver;

import java.util.*;

/**
 * Demonstrates how to create a custom maze and display it using the MazeObserver.
 * You can define your own rooms, connections, characters, and artifacts.
 */
public class CustomMazeDemo {

    /**
     * A simple custom maze implementation that lets you define rooms and connections manually.
     * This is the core data model used by the layout system.
     */
    public static class CustomMaze implements IMaze {
        private final Map<String, Set<String>> connections = new HashMap<>();
        private final Map<String, List<String>> characters = new HashMap<>();
        private final Map<String, List<String>> artifacts = new HashMap<>();

        public CustomMaze addRoom(String roomName) {
            connections.putIfAbsent(roomName, new HashSet<>());
            return this;
        }

        // Two-way connection
        public CustomMaze connect(String room1, String room2) {
            connections.computeIfAbsent(room1, k -> new HashSet<>()).add(room2);
            connections.computeIfAbsent(room2, k -> new HashSet<>()).add(room1);
            return this;
        }

        // One-way connection (directed edge)
        public CustomMaze connectOneWay(String from, String to) {
            connections.computeIfAbsent(from, k -> new HashSet<>()).add(to);
            connections.putIfAbsent(to, new HashSet<>());
            return this;
        }

        public CustomMaze addCharacters(String room, String... chars) {
            characters.put(room, Arrays.asList(chars));
            return this;
        }

        public CustomMaze addArtifacts(String room, String... items) {
            artifacts.put(room, Arrays.asList(items));
            return this;
        }

        @Override
        public Set<String> getRoomNames() {
            return connections.keySet();
        }

        @Override
        public Set<String> getNeighborsOf(String room) {
            return connections.getOrDefault(room, Set.of());
        }

        @Override
        public List<String> getCharacters(String room) {
            return characters.getOrDefault(room, List.of());
        }

        @Override
        public List<String> getArtifacts(String room) {
            return artifacts.getOrDefault(room, List.of());
        }
    }

    /**
     * Fluent builder for CustomMaze — same API as CustomMaze but returns IMaze at the end.
     * Used by PolymorphiaDemo to build mazes cleanly.
     */
    public static class CustomMazeBuilder {
        private final CustomMaze maze = new CustomMaze();

        public CustomMazeBuilder addRoom(String roomName) {
            maze.addRoom(roomName);
            return this;
        }

        public CustomMazeBuilder connect(String room1, String room2) {
            maze.connect(room1, room2);
            return this;
        }

        public CustomMazeBuilder connectOneWay(String from, String to) {
            maze.connectOneWay(from, to);
            return this;
        }

        public CustomMazeBuilder addCharacters(String room, String... chars) {
            maze.addCharacters(room, chars);
            return this;
        }

        public CustomMazeBuilder addArtifacts(String room, String... items) {
            maze.addArtifacts(room, items);
            return this;
        }

        public IMaze build() {
            return maze;
        }
    }

    // ─── Pre-built example mazes ──────────────────────────────────────────────

    public static IMaze createDungeonMaze() {
        return new CustomMaze()
                .addRoom("Entrance")
                .addRoom("Hallway")
                .addRoom("Treasury")
                .addRoom("Dungeon")
                .addRoom("Dragon's Lair")
                .connect("Entrance", "Hallway")
                .connect("Hallway", "Treasury")
                .connect("Hallway", "Dungeon")
                .connect("Dungeon", "Dragon's Lair")
                .addCharacters("Entrance", "Guard")
                .addCharacters("Dungeon", "Prisoner", "Goblin")
                .addCharacters("Dragon's Lair", "Dragon")
                .addArtifacts("Treasury", "Gold", "Sword", "Shield")
                .addArtifacts("Dragon's Lair", "Magic Ring");
    }

    public static IMaze createHouseMaze() {
        return new CustomMaze()
                .connect("Living Room", "Kitchen")
                .connect("Living Room", "Bedroom")
                .connect("Living Room", "Bathroom")
                .connect("Kitchen", "Pantry")
                .connect("Bedroom", "Closet")
                .connect("Living Room", "Garage")
                .addCharacters("Living Room", "Mom", "Dad")
                .addCharacters("Kitchen", "Chef")
                .addCharacters("Bedroom", "Cat")
                .addArtifacts("Kitchen", "Knife", "Pan")
                .addArtifacts("Garage", "Car", "Tools");
    }

    public static IMaze createCastleMaze() {
        return new CustomMaze()
                .connect("Castle Gate", "Great Hall")
                .connect("Great Hall", "Dining Room")
                .connect("Great Hall", "Library")
                .connect("Great Hall", "Tower Stairs")
                .connect("Tower Stairs", "King's Chamber")
                .connect("Tower Stairs", "Queen's Chamber")
                .connect("King's Chamber", "Balcony")
                .connect("Great Hall", "Cellar Stairs")
                .connect("Cellar Stairs", "Wine Cellar")
                .connect("Cellar Stairs", "Prison")
                .addCharacters("Castle Gate", "Knight", "Guard")
                .addCharacters("Great Hall", "King", "Queen", "Jester")
                .addCharacters("Library", "Wizard", "Scholar")
                .addCharacters("Prison", "Thief")
                .addCharacters("King's Chamber", "King")
                .addArtifacts("Library", "Ancient Book", "Scroll")
                .addArtifacts("Wine Cellar", "Wine", "Treasure")
                .addArtifacts("King's Chamber", "Crown", "Scepter");
    }

    public static void main(String[] args) throws InterruptedException {
        MazeObserver observer = MazeObserver.getNewBuilder("Custom Maze Demo")
                .useImageRooms()
                .useRadialLayoutStrategy()
                .setDimension(1200)
                .setRoomDimension(120)
                .setDelayInSecondsAfterUpdate(2)
                .build();

        System.out.println("Showing Dungeon Maze...");
        observer.update(createDungeonMaze(), "Welcome to the Dungeon!");
        Thread.sleep(5000);

//        System.out.println("Showing House Maze...");
//        observer.update(createHouseMaze(), "Welcome Home!");
//        Thread.sleep(5000);
//
//        System.out.println("Showing Castle Maze...");
//        observer.update(createCastleMaze(), "Welcome to the Castle!");
//        Thread.sleep(5000);
//
//        System.out.println("Adding a secret room to the castle...");
//        CustomMaze expandedCastle = (CustomMaze) createCastleMaze();
//        expandedCastle
//                .connect("Library", "Secret Room")
//                .addCharacters("Secret Room", "Ghost")
//                .addArtifacts("Secret Room", "Forbidden Tome", "Magic Wand");
//        observer.update(expandedCastle, "You found a Secret Room!");
//        Thread.sleep(5000);

        System.out.println("Demo complete!");
    }
}
