package Service;

import model.SteamProfile;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class SteamService {

    private final String apiKey;
    private final OkHttpClient client = new OkHttpClient();

    public SteamService(String apiKey) {
        this.apiKey = apiKey;
    }


    public SteamProfile getProfile(String steamId) {
        String url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/"
                + "?key=" + apiKey
                + "&steamids=" + steamId;

        JSONObject json = getJson(url);

        JSONObject player = json
                .getJSONObject("response")
                .getJSONArray("players")
                .getJSONObject(0);

        SteamProfile steamProfile = new SteamProfile();

        steamProfile.setNickname(player.getString("personaname"));
        steamProfile.setAvatarUrl(player.getString("avatarfull"));

        return steamProfile;
    }

    public int getSteamLevel(String steamId) {

        String url = "https://api.steampowered.com/IPlayerService/GetSteamLevel/v1/"
                + "?key=" + apiKey
                + "&steamid=" + steamId;

        JSONObject json = getJson(url);

        return json
                .getJSONObject("response")
                .getInt("player_level");
    }

    public int getHoursInGame(String steamId, int appId) {

        String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/"
                + "?key=" + apiKey
                + "&steamid=" + steamId
                + "&include_appinfo=1"
                + "&include_played_free_games=true";

        JSONObject json = getJson(url);

        JSONArray games = json
                .getJSONObject("response")
                .getJSONArray("games");

        if (games == null) {
            return 0;
        }

        for (int i = 0; i < games.length(); i++) {
            JSONObject game = games.getJSONObject(i);

            if (game.getInt("appid") == appId) {
                return game.getInt("playtime_forever") / 60;
            }
        }
        return 0;
    }

    private JSONObject getJson(String url) {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Steam API error: " + response.code());
            }

            String body = response.body().string();
            return new JSONObject(body);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
