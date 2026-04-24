package model;

public class Application {

    private final int id;
    private final String title;
    private final String description;
    private final int playersNeeded;
    private final long userId;
    private final String game;

    public Application(int id, String title, String description, int playersNeeded, long userId, String game) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.playersNeeded = playersNeeded;
        this.userId = userId;
        this.game = game;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPlayersNeeded() {
        return playersNeeded;
    }

    public long getUserId() {
        return userId;
    }

    public String getGame() {
        return game;
    }
}
