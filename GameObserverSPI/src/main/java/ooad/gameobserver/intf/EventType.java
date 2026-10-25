package ooad.gameobserver.intf;

public enum EventType {
    TREASURE_ACQUIRED,
    FIGHT_OCCURRED,
    CHARACTER_DIED,
    CHARACTER_MOVED,
    CHARACTER_ATE,
    CHARACTER_SWITCHED_STRATEGY,
    FINISHED_TURN,
    GAME_OVER,
    ARTIFACT_TAKEN,
    NEW_DIE_ACQUIRED,
    TREASURE_MAP_ACQUIRED,
    // This is used to notify Polymorphia (which must be observing itself) to add a new reincarnated character
    ADD_CHARACTER,
    ALL,
}
