package controller;

import io.javalin.Javalin;
import repository.UserRepository;

import java.util.Map;

public class SteamAuthController {

    public static void register(Javalin app) {

        app.get("/auth/steam", ctx -> {

            String discordIdStr = ctx.queryParam("discordId");

            if (discordIdStr == null || discordIdStr.isBlank()) {
                ctx.status(400).result("Missing discordId");
                return;
            }

            long discordId;

            try {
                discordId = Long.parseLong(discordIdStr);
            } catch (Exception e) {
                ctx.status(400).result("Invalid discordId");
                return;
            }

            String steamUrl =
                    "https://steamcommunity.com/openid/login" +
                            "?openid.ns=http://specs.openid.net/auth/2.0" +
                            "&openid.mode=checkid_setup" +
                            "&openid.identity=http://specs.openid.net/auth/2.0/identifier_select" +
                            "&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select" +
                            "&openid.return_to=http://localhost:8080/auth/steam/callback?discordId=" + discordId +
                            "&openid.realm=http://localhost:8080/";

            ctx.redirect(steamUrl);
        });


        app.get("/auth/steam/callback", ctx -> {

            try {
                Map<String, String> params = ctx.queryParamMap()
                        .entrySet()
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().get(0)
                        ));

                String discordIdStr = ctx.queryParam("discordId");
                String claimedId = params.get("openid.claimed_id");

                if (discordIdStr == null || claimedId == null) {
                    ctx.status(400).result("Missing data");
                    return;
                }

                long discordId = Long.parseLong(discordIdStr);

                String steamId = extractSteamId(claimedId);

                if (steamId == null) {
                    ctx.status(400).result("Invalid steamId");
                    return;
                }

                // save to DB
                UserRepository.updateSteamIdByDiscordId(discordId, steamId);


                ctx.contentType("text/html; charset=UTF-8");

                ctx.html("""
                        <h2>Steam успешно привязан ✅</h2>
                        <p>Теперь можешь закрыть вкладку</p>
                        """);


            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Internal server error: " + e.getMessage());
            }
        });
    }

    private static String extractSteamId(String claimedId) {

        if (claimedId == null) return null;

        try {
            return claimedId.substring(claimedId.lastIndexOf("/") + 1);
        } catch (Exception e) {
            return null;
        }
    }
}