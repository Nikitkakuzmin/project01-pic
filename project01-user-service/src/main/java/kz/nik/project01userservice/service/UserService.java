package kz.nik.project01userservice.service;


import kz.nik.project01userservice.dto.UserCreateDto;
import kz.nik.project01userservice.dto.UserDto;

import kz.nik.project01userservice.dto.UserSignInDto;


import kz.nik.project01userservice.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface UserService {


    List<UserDto> getUsers(String token);

    UserDto getUser(Long id, String token);


    void addUser(UserCreateDto userCreateDto);


    String signIn(UserSignInDto userSignInDto);

    void changePassword(String email, String newPassword);

    void addRoleToUser(String username, String roleName, String token);

    void removeRoleFromUser(String username, String roleName, String token);

    void removeUser(String username, String token,Role role);
    UserDto getCurrentUser(String token);


}
