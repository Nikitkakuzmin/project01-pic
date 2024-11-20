package kz.nik.project01userservice.client;


import kz.nik.project01userservice.dto.UserCreateDto;
import kz.nik.project01userservice.dto.UserSignInDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final Keycloak keycloak;
    private final RestTemplate restTemplate;


    @Value("${keycloak.url}")
    private String url;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;


    public UserRepresentation addUser(UserCreateDto userCreateDto) {

        UserRepresentation newUser = new UserRepresentation();
        newUser.setEmail(userCreateDto.getEmail());
        newUser.setEmailVerified(true);
        newUser.setUsername(userCreateDto.getUsername());
        newUser.setFirstName(userCreateDto.getFirstName());
        newUser.setLastName(userCreateDto.getLastName());
        newUser.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userCreateDto.getPassword());
        credential.setTemporary(false);
        newUser.setCredentials(List.of(credential));

        Response response = keycloak
                .realm(realm)
                .users()
                .create(newUser);

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            log.error("Error on creating user, status: {}", response.getStatus());
            throw new RuntimeException("Failed to create user in keycloak, status = " + response.getStatus());
        }

        List<UserRepresentation> search =
                keycloak
                        .realm(realm)
                        .users()
                        .search(userCreateDto.getUsername());

        return search.get(0);

    }

    public String signIn(UserSignInDto userSignInDto) {

        String tokenEndpoint = url + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", userSignInDto.getUsername());
        formData.add("password", userSignInDto.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate
                .postForEntity(tokenEndpoint, new HttpEntity<>(formData, headers), Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error on signing user, status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to sign in in user = " + userSignInDto.getUsername());
        }
        return (String) responseBody.get("access_token");
    }

    public void changePassword(String username, String newPassword){
        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .search(username);
        if(users.isEmpty()){
            log.error("User not found with username: {}", username);
            throw new RuntimeException("User not found with username : " + username);
        }

        UserRepresentation userRepresentation = users.get(0);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(newPassword);
        credentialRepresentation.setTemporary(false);

        keycloak
                .realm(realm)
                .users()
                .get(userRepresentation.getId())
                .resetPassword(credentialRepresentation);
        log.info("Password changed successfully for username: {}", username);
    }

    public void addRoleToUser(String username, String roleName) {
        UserRepresentation user = getUserByUsername(username);
        RoleRepresentation role = getRoleByName(roleName);

        if (user != null && role != null) {
            keycloak.realm(realm)
                    .users()
                    .get(user.getId())
                    .roles()
                    .realmLevel()
                    .add(Collections.singletonList(role));
            log.info("Role '{}' added to user '{}'", roleName, username);
        } else {
            log.error("Failed to add role '{}' to user '{}'", roleName, username);
        }
    }

    public void removeRoleFromUser(String username, String roleName) {
        UserRepresentation user = getUserByUsername(username);
        RoleRepresentation role = getRoleByName(roleName);

        if (user != null && role != null) {
            keycloak.realm(realm)
                    .users()
                    .get(user.getId())
                    .roles()
                    .realmLevel()
                    .remove(Collections.singletonList(role));
            log.info("Role '{}' removed from user '{}'", roleName, username);
        } else {
            log.error("Failed to remove role '{}' from user '{}'", roleName, username);
        }
    }

    private UserRepresentation getUserByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .search(username);

        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            log.error("User '{}' not found", username);
            return null;
        }
    }

    private RoleRepresentation getRoleByName(String roleName) {
        List<RoleRepresentation> roles = keycloak.realm(realm)
                .roles()
                .list();

        for (RoleRepresentation role : roles) {
            if (role.getName().equals(roleName)) {
                return role;
            }
        }

        log.error("Role '{}' not found", roleName);
        return null;
    }

    public void removeUser(String username) {
        UserRepresentation user = getUserByUsername(username);
        if (user != null) {
            keycloak.realm(realm).users().delete(user.getId());
            log.info("User '{}' removed from Keycloak", username);
        } else {
            log.error("Failed to remove user '{}' from Keycloak. User not found.", username);
        }
    }

}





