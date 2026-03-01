package csci.ooad.layout.example;

import csci.ooad.layout.MazeLayout;

import csci.ooad.layout.IRoomLayoutStrategy;
import csci.ooad.layout.MixedLayoutStrategy;
import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.MazeObserver;

import java.util.List;




public class PolymorphiaDemo {

    // ─── Maze Builders ────────────────────────────────────────────────────────

    /**
     * Sequential maze: rooms in a simple chain.
     * Layout: [ Entrance ] → [ Hallway ] → [ Chamber ] → [ Vault ] → [ Exit ]
     */
    public static IMaze createSequentialMaze() {
        return new CustomMazeDemo.CustomMazeBuilder()
                .connectOneWay("Entrance", "Hallway")
                .connectOneWay("Hallway", "Chamber")
                .connectOneWay("Chamber", "Vault")
                .connectOneWay("Vault", "Exit")
                .addCharacters("Entrance", "Guard", "Knight")
                .addCharacters("Chamber", "Demon", "Orc")
                .addCharacters("Vault", "Dragon")
                .addArtifacts("Hallway", "Torch", "Map")
                .addArtifacts("Vault", "Gold", "Crown", "Scepter")
                .build();
    }

    /**
     * Ring maze: rooms in a circular loop.
     * Layout: rooms arranged in a circle, each connecting to the next.
     */
    public static IMaze createRingMaze() {
        return new CustomMazeDemo.CustomMazeBuilder()
                .connectOneWay("North Tower", "East Wing")
                .connectOneWay("East Wing", "South Tower")
                .connectOneWay("South Tower", "West Wing")
                .connectOneWay("West Wing", "Northwest Room")
                .connectOneWay("Northwest Room", "North Tower")
                .addCharacters("North Tower", "Archer", "Knight")
                .addCharacters("East Wing", "Demon")
                .addCharacters("South Tower", "Troll")
                .addCharacters("West Wing", "Wizard", "Adventurer")
                .addCharacters("Northwest Room", "Ghost")
                .addArtifacts("East Wing", "Sword", "Shield")
                .addArtifacts("South Tower", "Potion")
                .build();
    }

    /**
     * Mixed maze: a sequential entrance chain leading into a circular boss arena.
     *
     * Sequential section:  [Start] → [Tunnel] → [Antechamber]
     *                                                  ↓
     * Ring section:              [Arena North] → [Arena East]
     *                            ↑                        ↓
     *                       [Arena West] ← [Arena South]
     */
    public static IMaze createMixedMaze() {
        return new CustomMazeDemo.CustomMazeBuilder()
                // Sequential entrance
                .connectOneWay("Village Gate", "Dark Forest")
                .connectOneWay("Dark Forest", "Cave Mouth")
                .connectOneWay("Cave Mouth", "Antechamber")
                // Ring: boss arena (connects from antechamber)
                .connectOneWay("Antechamber", "Arena North")
                .connectOneWay("Arena North", "Arena East")
                .connectOneWay("Arena East", "Arena South")
                .connectOneWay("Arena South", "Arena West")
                .connectOneWay("Arena West", "Arena North")  // closes the ring
                // Characters
                .addCharacters("Village Gate", "Adventurer")
                .addCharacters("Dark Forest", "Wolf", "Bandit")
                .addCharacters("Cave Mouth", "Troll")
                .addCharacters("Antechamber", "Demon Guard")
                .addCharacters("Arena North", "Boss Demon", "Knight")
                .addCharacters("Arena East", "Orc Warrior")
                .addCharacters("Arena South", "Dark Wizard")
                .addCharacters("Arena West", "Dragon")
                // Artifacts
                .addArtifacts("Village Gate", "Health Potion", "Map")
                .addArtifacts("Dark Forest", "Sword")
                .addArtifacts("Cave Mouth", "Torch", "Rope")
                .addArtifacts("Arena North", "Boss Key", "Crown")
                .addArtifacts("Arena South", "Magic Staff")
                .build();
    }

    /**
     * Mixed maze with TWO rings connected by a chain.
     * Shows a more complex combination layout.
     */

    public static IMaze createComplexMaze() {
        return new CustomMazeDemo.CustomMazeBuilder()
                // Fully connected cluster
                .connect("Hub A", "Hub B")
                .connect("Hub A", "Hub C")
                .connect("Hub A", "Hub D")
                .connect("Hub A", "Hub E")
                .connect("Hub B", "Hub C")
                .connect("Hub B", "Hub D")
                .connect("Hub B", "Hub E")
                .connect("Hub C", "Hub D")
                .connect("Hub C", "Hub E")
                .connect("Hub D", "Hub E")
                // Chain
                .connect("Hub A", "Path 1")
                .connect("Path 1", "Path 2")
                .connect("Path 2", "Path 3")
                .connect("Path 3", "Path 4")
                // Ring
                .connect("Path 4", "Ring North")
                .connect("Ring North", "Ring East")
                .connect("Ring East", "Ring South")
                .connect("Ring South", "Ring West")
                .connect("Ring West", "Ring North")
                // Characters
                .addCharacters("Hub A", "King", "Advisor")
                .addCharacters("Hub B", "General")
                .addCharacters("Hub C", "Wizard")
                .addCharacters("Hub D", "Rogue")
                .addCharacters("Hub E", "Paladin")
                .addCharacters("Path 1", "Bandit")
                .addCharacters("Path 2", "Wolf")
                .addCharacters("Path 3", "Troll")
                .addCharacters("Path 4", "Demon Guard")
                .addCharacters("Ring North", "Boss", "Knight")
                .addCharacters("Ring East", "Orc")
                .addCharacters("Ring South", "Dragon")
                .addCharacters("Ring West", "Vampire")
                // Artifacts
                .addArtifacts("Hub A", "Crown", "Scepter")
                .addArtifacts("Path 1", "Sword")
                .addArtifacts("Path 3", "Torch", "Map")
                .addArtifacts("Ring North", "Boss Key")
                .addArtifacts("Ring South", "Magic Staff")
                .build();
    }


    public static IMaze createDoubleRingMaze() {
        return new CustomMazeDemo.CustomMazeBuilder()
                // First ring: forest loop
                .connectOneWay("Forest A", "Forest B")
                .connectOneWay("Forest B", "Forest C")
                .connectOneWay("Forest C", "Forest A")  // closes ring 1
                // Bridge/chain between rings
                .connectOneWay("Forest A", "Bridge")
                .connectOneWay("Bridge", "Dungeon Entry")
                // Second ring: dungeon loop
                .connectOneWay("Dungeon Entry", "Dungeon A")
                .connectOneWay("Dungeon A", "Dungeon B")
                .connectOneWay("Dungeon B", "Dungeon C")
                .connectOneWay("Dungeon C", "Dungeon Entry")  // closes ring 2
                // Characters
                .addCharacters("Forest A", "Elf", "Archer")
                .addCharacters("Forest B", "Wolf")
                .addCharacters("Forest C", "Druid")
                .addCharacters("Bridge", "Troll")
                .addCharacters("Dungeon Entry", "Skeleton Guard")
                .addCharacters("Dungeon A", "Demon", "Orc")
                .addCharacters("Dungeon B", "Vampire")
                .addCharacters("Dungeon C", "Lich King")
                // Artifacts
                .addArtifacts("Forest B", "Elven Bow", "Herbs")
                .addArtifacts("Bridge", "Toll Key")
                .addArtifacts("Dungeon B", "Dark Scroll", "Soul Gem")
                .addArtifacts("Dungeon C", "Lich Crown", "Necromancer Staff")
                .build();
    }

    // ─── Main Demo ────────────────────────────────────────────────────────────

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Polymorphia Maze Layout Demo ===\n");

        record Demo(String title, IMaze maze) {}

        List<Demo> demos = List.of(
//          new Demo("Sequential Maze", createSequentialMaze()),
//          new Demo("Ring Maze", createRingMaze()),
//          new Demo("Mixed Maze: chain → ring", createMixedMaze()),
//          new Demo("Double Ring Maze", createDoubleRingMaze()),
                new Demo("Complex Maze: cluster → chain → ring", createComplexMaze())
        );

        for (Demo demo : demos) {
            showDemo(demo.title(), demo.maze());
        }
    }

    private static void showDemo(String title, IMaze maze) throws InterruptedException {
        IRoomLayoutStrategy strategy = new MixedLayoutStrategy(MazeLayout.buildAdjacency(maze));

        MazeObserver observer = MazeObserver.getNewBuilder("Polymorphia - " + title)
                .useImageRooms()
                .setLayoutStrategy(strategy)
                .setDimension(1200)
                .setWidth(1600)
                .setHeight(700)
                .setRoomDimension(100)
                .setDelayInSecondsAfterUpdate(1)
                .build();

        observer.update(maze, title);
        Thread.sleep(5000);
    }
}
