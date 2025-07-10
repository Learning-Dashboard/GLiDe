package edu.upc.gessi.glidebackend.service.impl;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import edu.upc.gessi.glidebackend.exception.AuthorizationException;
import edu.upc.gessi.glidebackend.exception.MissingInformationException;
import edu.upc.gessi.glidebackend.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${google.client.id}")
    private String googleClientId;

    @Override
    public String getTokenMail(String idToken) {
        if(idToken.isBlank())
            throw new MissingInformationException("No idToken was provided");

        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            String token = idToken.startsWith("Bearer ") ? idToken.substring(7).trim() : idToken;
            System.out.println("Stripped ID Token: " + token);
            GoogleIdToken googleIdToken = verifier.verify(token);
            System.out.println("Google ID Token: " + googleIdToken);
            System.out.println("Google Client ID: " + googleClientId);
            System.out.println("Google ID Token Audience: " + googleIdToken.getPayload().getAudience());
            System.out.println("Google ID Token Issuer: " + googleIdToken.getPayload().getIssuer());
            if (googleIdToken != null) {
                Payload payload = googleIdToken.getPayload();
                System.out.println("Payload: " + payload);
                System.out.println("Email: " + payload.getEmail());
                return payload.getEmail();
            } else {
                throw new AuthorizationException("Token verification failed: Invalid ID token");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new AuthorizationException("Issue verifying idToken" + e.getMessage());
        }
    }
}
