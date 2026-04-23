package model;

public class User {

    private String discordID;
    private String username;
    private String game;
    private String rank;

    public User(String discordID, String username, String game, String rank) {
        this.discordID = discordID;
        this.username = username;
        this.game = game;
        this.rank = rank;
    }

    public String getDiscordID() {
        return discordID;
    }

    public void setDiscordID(String discordID) {
        this.discordID = discordID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
