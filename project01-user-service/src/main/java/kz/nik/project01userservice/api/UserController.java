package kz.nik.project01userservice.api;


import jakarta.servlet.http.HttpServletRequest;


import kz.nik.project01userservice.dto.UserCreateDto;
import kz.nik.project01userservice.dto.UserDto;
import kz.nik.project01userservice.dto.UserSignInDto;


import kz.nik.project01userservice.model.Role;
import kz.nik.project01userservice.service.UserService;

import kz.nik.project01userservice.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserDto> getUsers(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return userService.getUsers(token);
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("isAuthenticated()")
    public UserDto getUser(HttpServletRequest httpRequest, @PathVariable(name = "id") Long id) {
        String token = httpRequest.getHeader("Authorization");
        return userService.getUser(id, token);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token) {

        UserDto currentUser = userService.getCurrentUser(token);

        if (currentUser != null) {

            return ResponseEntity.ok(currentUser);
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/add-user")
    public void addUser(@RequestBody UserCreateDto userCreateDto) {
        userService.addUser(userCreateDto);
    }


    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignInDto userSignInDto) {

        return new ResponseEntity<>(userService.signIn(userSignInDto), HttpStatus.OK);
    }

    @PostMapping(value = "/update-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody UserDto userDto) {
        try {
            userService.changePassword(userDto.getEmail(), userDto.getPassword());
            return ResponseEntity.ok("Password changes successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error on changing password");
        }
    }

    @PostMapping(value = "/update-own-password/{ownPassword}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeOwnPassword(@PathVariable(name = "ownPassword") String ownPassword, HttpServletRequest httpRequest) {
        try {

            String currentUser = UserUtil.getCurrentUserName();
            if (currentUser != null) {
                userService.changePassword(currentUser, ownPassword);
                return ResponseEntity.ok("Password changes successfully");
            }

        } catch (RuntimeException e) {
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error on changing password");
    }

    @PostMapping("/add-role")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> addRoleToUser(@RequestParam String username, @RequestParam String roleName,
                                                HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        userService.addRoleToUser(username, roleName, token);
        return ResponseEntity.ok("Role '" + roleName + "' added to user '" + username + "'");
    }

    @PostMapping("/remove-role")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> removeRoleFromUser(@RequestParam String username, @RequestParam String roleName,
                                                     HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        userService.removeRoleFromUser(username, roleName, token);
        return ResponseEntity.ok("Role '" + roleName + "' removed from user '" + username + "'");
    }

    @PostMapping("/remove-user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> removeUser(@RequestParam String username, HttpServletRequest httpRequest, Role role) {
        String token = httpRequest.getHeader("Authorization");
        userService.removeUser(username, token, role);
        return ResponseEntity.ok("User '" + username + "' removed");
    }
}
