package model;

public class SteamProfile {

    private String nickname;
    private String avatarUrl;
    private int level;
    private int hoursInGame;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHoursInGame() {
        return hoursInGame;
    }

    public void setHoursInGame(int hoursInGame) {
        this.hoursInGame = hoursInGame;
    }
}
