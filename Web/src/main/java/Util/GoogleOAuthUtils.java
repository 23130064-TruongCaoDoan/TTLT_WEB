package Util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import model.GoogleUser;

import java.util.Collections;

public class GoogleOAuthUtils {

    private static final String CLIENT_ID = "846603349467";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/Web_war_exploded/login-google";

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