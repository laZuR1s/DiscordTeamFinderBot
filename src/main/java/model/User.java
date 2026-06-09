package model;

public class User {

    private String discordId;
    private String username;
    private String game;
    private String rank;
    private String steamId;

    public User(String discordId, String username, String game, String rank) {
        this.discordId = discordId;
        this.username = username;
        this.game = game;
        this.rank = rank;
    }

    public String getDiscordID() {
        return discordId;
    }

    public void setDiscordID(String discordID) {
        this.discordId = discordID;
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

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }
}
