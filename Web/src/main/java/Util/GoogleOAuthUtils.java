package Util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import model.GoogleUser;

import java.util.Collections;
import java.util.Properties;

public class GoogleOAuthUtils {

    private static String CLIENT_ID;
    private static String CLIENT_SECRET;
    private static String REDIRECT_URI = "http://localhost:8080/Web_war_exploded/login-google";

    static {
        try {
            Properties prop = new Properties();
            prop.load(GoogleOAuthUtils.class.getClassLoader().getResourceAsStream("google.properties"));
            CLIENT_ID = prop.getProperty("google.client.id");
            CLIENT_SECRET = prop.getProperty("google.client.secret");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getToken(String code) throws Exception {
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),
                        CLIENT_ID,
                        CLIENT_SECRET,
                        code,
                        REDIRECT_URI
                ).execute();
        return tokenResponse.getIdToken();
    }

    public static GoogleUser getUserInfo(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier =
                new GoogleIdTokenVerifier.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance())
                        .setAudience(Collections.singletonList(CLIENT_ID))
                        .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            GoogleUser user = new GoogleUser();
            user.setEmail(payload.getEmail());
            user.setName((String) payload.get("name"));
            return user;
        }

        return null;
    }
}