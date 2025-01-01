package org.example.clientapp.client;

import lombok.RequiredArgsConstructor;
import org.example.clientapp.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class UserRestClientImpl implements UserRestClient {

    private final RestClient restClient;

    @Override
    public User add(User user) {
        return this.restClient
                .post()
                .uri("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .body(User.class);
    }
}
