package fr.opoc.duel.arenas;

public enum ArenaState {
    PLAYING("Playing"),
    FREE("Free"),
    COUNTDOWN("Countdown"),
    NOTREADY("Setup"),
    ENDING("Ending");

    private final String name;

    ArenaState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
