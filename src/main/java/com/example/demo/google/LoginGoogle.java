package com.example.demo.google;

import com.example.demo.entity.Users;
import com.example.demo.constant.constant;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class LoginGoogle {

    private static final String CLIENT_ID = constant.GOOGLE_CLIENT_ID;

    public static Users verifyIdToken(String idTokenString) throws IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Lỗi bảo mật khi xác minh token", e);
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            Users user = new Users();
            user.setEmail(payload.getEmail());
            user.setFull_name((String) payload.get("name"));
            user.setGender((String) payload.get("gender"));
            user.setRole("CUSTOMER");
            return user;
        } else {
            throw new IOException("Token không hợp lệ");
        }
    }

    public static GoogleIdToken.Payload verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        return (idToken != null) ? idToken.getPayload() : null;
    }
}
