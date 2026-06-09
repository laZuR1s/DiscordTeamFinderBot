package mapper;

public class GameMapper {
    public static int getAppId(String game) {
        return switch (game) {
            case "CS2" -> 730;
            case "Dota 2" -> 570;
            case "Europa Universalis IV" -> 236850;
            case "Deadlock" -> 1422450;
            default -> 0;
        };
    }

}
