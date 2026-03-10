package Util;

import model.FacebookUser;
import org.cloudinary.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FacebookOAuthUltis {
    private static final String APP_ID = "767353226134083";
    private static final String APP_SECRET = "98f7827dde4f40bbbb5c38fe0a4e43f2";
    private static final String REDIRECT_URI = "http://localhost:8080/Web_war_exploded/login-facebook";

    public static String getToken(String code) throws Exception {

        String link =
                "https://graph.facebook.com/v18.0/oauth/access_token?"
                        + "client_id=" + APP_ID
                        + "&redirect_uri=" + REDIRECT_URI
                        + "&client_secret=" + APP_SECRET
                        + "&code=" + code;

        URL url = new URL(link);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream is = conn.getInputStream();
        String result = new String(is.readAllBytes());

        JSONObject json = new JSONObject(result);

        return json.getString("access_token");
    }

    public static FacebookUser getUserInfo(String accessToken) throws Exception {

        String link =
                "https://graph.facebook.com/me?fields=id,name,email&access_token="
                        + accessToken;

        URL url = new URL(link);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream is = conn.getInputStream();
        String result = new String(is.readAllBytes());

        JSONObject json = new JSONObject(result);
        if (json.has("error")) {
            return null;
        }

        FacebookUser user = new FacebookUser();

        user.setName(json.getString("name"));
        user.setEmail(json.optString("email"));

        return user;
    }
}
