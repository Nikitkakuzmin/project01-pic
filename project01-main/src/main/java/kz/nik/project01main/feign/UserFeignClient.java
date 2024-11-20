package kz.nik.project01main.feign;

import jakarta.servlet.http.HttpServletRequest;
import kz.nik.project01main.config.FeignConfig;
import kz.nik.project01main.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-feign-client",url = "http://localhost:8001",configuration =  FeignConfig.class)
public interface UserFeignClient {




    @GetMapping(value ="/user")
    List<UserDTO> getUsers(@RequestHeader("Authorization") String token);

    @GetMapping(value ="/user/{id}")
    UserDTO getUser(@PathVariable(name = "id") Long id,@RequestHeader("Authorization") String token);

    @PostMapping(value ="/user/add-user")
    UserDTO addUser(UserDTO userDTO);


    @PostMapping(value = "/user/sign-in")
   String signIn(@RequestBody UserDTO userDTO);

    @PostMapping("/user/add-role")
     ResponseEntity<String> addRoleToUser(@RequestParam String username, @RequestParam String roleName,
                                          @RequestHeader("Authorization") String token);

    @PostMapping("user/remove-role")
    ResponseEntity<String> removeRoleFromUser(@RequestParam String username, @RequestParam String roleName,
                                              @RequestHeader("Authorization") String token);

    @PostMapping("/user/remove-user")
    ResponseEntity<String> removeUser(@RequestParam String username, @RequestHeader("Authorization") String token);
}
