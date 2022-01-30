package evolution.enums;

public enum GameStatus {
    RUNNING("running"),
    COMPLETED("completed");

    public final String value;

    GameStatus(String value) {
        this.value = value;
    }

}
