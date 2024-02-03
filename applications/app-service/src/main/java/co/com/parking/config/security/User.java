package co.com.parking.config.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String email;
    @JsonProperty("cognito:username")
    private String username;
    @JsonProperty("cognito:groups")
    private List<String> groups;
    @JsonProperty("cognito:roles")
    private List<String> roles;
}
