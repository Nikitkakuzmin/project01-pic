package kz.nik.project01main.api;

import jakarta.servlet.http.HttpServletRequest;
import kz.nik.project01main.dto.UserDTO;
import kz.nik.project01main.feign.UserFeignClient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFeignClient userFeignClient;

    @GetMapping()
    public List<UserDTO> getUsers(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return userFeignClient.getUsers(token);
    }

    @GetMapping(value = "{id}")
    public UserDTO getUser(@PathVariable(name = "id") Long id, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return userFeignClient.getUser(id, token);
    }


    @PostMapping(value ="/add-user")
    public void addUser(@RequestBody UserDTO userDTO) {
        userFeignClient.addUser(userDTO);
    }


    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userFeignClient.signIn(userDTO), HttpStatus.OK);
    }

    @PostMapping("/add-role")
    public ResponseEntity<String> addRoleToUser(@RequestParam String username, @RequestParam String roleName,
                                                HttpServletRequest httpRequest){
        String token = httpRequest.getHeader("Authorization");
        userFeignClient.addRoleToUser(username, roleName,token);
        return ResponseEntity.ok("Role '" + roleName + "' added to user '" + username + "'");
    }

    @PostMapping("/remove-role")
    public ResponseEntity<String> removeRoleFromUser(@RequestParam String username, @RequestParam String roleName,
                                                     HttpServletRequest httpRequest){
        String token = httpRequest.getHeader("Authorization");
        userFeignClient.removeRoleFromUser(username,roleName,token);
        return ResponseEntity.ok("Role '" + roleName + "' removed from user '" + username + "'");
    }


    @PostMapping("/remove-user")
    public ResponseEntity<String> removeUser(@RequestParam String username,HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        userFeignClient.removeUser(username,token);
        return ResponseEntity.ok("User '" + username + "' removed");
    }


}
