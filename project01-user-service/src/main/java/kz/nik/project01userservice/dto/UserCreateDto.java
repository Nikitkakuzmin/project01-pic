package kz.nik.project01userservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}