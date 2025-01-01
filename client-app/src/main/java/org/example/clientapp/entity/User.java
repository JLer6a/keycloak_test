package org.example.clientapp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private boolean emailVerified;
    private List<Credential> credentials;

    @Data
    @Builder
    public static class Credential {
        private String type;
        private String value;
        @JsonProperty("temporary")
        private boolean isTemporary;
    }
}
