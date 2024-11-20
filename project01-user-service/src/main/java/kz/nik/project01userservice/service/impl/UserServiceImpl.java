package kz.nik.project01userservice.service.impl;



import kz.nik.project01userservice.client.KeycloakClient;
import kz.nik.project01userservice.dto.UserCreateDto;
import kz.nik.project01userservice.dto.UserDto;
import kz.nik.project01userservice.dto.UserSignInDto;

import kz.nik.project01userservice.exception.UserNotFoundException;
import kz.nik.project01userservice.mapper.UserMapper;

import kz.nik.project01userservice.model.Role;
import kz.nik.project01userservice.model.User;
import kz.nik.project01userservice.repository.UserRepository;
import kz.nik.project01userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakClient keycloakClient;



    @Override
    public List<UserDto> getUsers(String token) {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto getUser(Long id, String token) {
        return userMapper.toDto(userRepository.findById(id).orElse(null));
    }
    public UserDto getCurrentUser(String token) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDto currentUserDto = new UserDto();
        currentUserDto.setUsername(username);

        return currentUserDto;
    }




    @Override
    public void addUser(UserCreateDto userCreateDto) {
        log.info("Creating user with username: {}", userCreateDto.getEmail());


        keycloakClient.addUser(userCreateDto);


        saveUserToDatabase(userCreateDto);
    }

    private void saveUserToDatabase(UserCreateDto userCreateDto) {

        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setUsername(userCreateDto.getUsername());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setPassword(userCreateDto.getPassword());


        userRepository.save(user);
    }
    public String signIn(UserSignInDto userSignInDto) {
        return keycloakClient.signIn(userSignInDto);
    }

    public void changePassword(String email, String newPassword) {
        keycloakClient.changePassword(email, newPassword);
    }


    @Override
    public void addRoleToUser(String username, String roleName,String token) {
        keycloakClient.addRoleToUser(username, roleName);
        updateUserRolesInDatabase(username, roleName, true);
    }

    @Override
    public void removeRoleFromUser(String username, String roleName,String token) {
        keycloakClient.removeRoleFromUser(username, roleName);
        updateUserRolesInDatabase(username, roleName, false);
    }

    private void updateUserRolesInDatabase(String username, String roleName, boolean addRole) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Role role = Role.valueOf(roleName.toUpperCase());
            if (addRole) {
                user.getRoles().add(role);
            } else {
                user.getRoles().remove(role);
            }
            userRepository.save(user);
        } else {

            throw new UserNotFoundException("User not found with username: " + username);

        }
    }

    @Override
    public void removeUser(String username,String token,Role role) {

        keycloakClient.removeUser(username);

        userRepository.deleteByUsername(username);
    }



}
