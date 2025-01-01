package org.example.clientapp.service;

import org.example.clientapp.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class KeycloakService {

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    private final RestTemplate restTemplate;

    public KeycloakService() {
        this.restTemplate = new RestTemplate();
    }

    public void createUser(User userRequestDto) {
        String accessToken = getAdminAccessToken();
        String url = String.format("http://localhost:8082/admin/realms/%s/users", realm);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<User> entity = new HttpEntity<>(userRequestDto, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }

    private String getAdminAccessToken() {
        String url = String.format("%s/protocol/openid-connect/token", keycloakServerUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String body = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s&username=%s&password=%s",
                clientId, clientSecret, adminUsername, adminPassword);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return parseAccessToken(response.getBody());
    }

    private String parseAccessToken(String responseBody) {
        Pattern pattern = Pattern.compile("\"access_token\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(responseBody);
        String token = null;
        if (matcher.find()) {
            token = matcher.group(1);
            System.out.println("Access Token: " + token);
        } else {
            System.out.println("Token not found.");
        }
        return token;
    }
}