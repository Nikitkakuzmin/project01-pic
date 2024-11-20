package kz.nik.project01userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInDto {

    private String username;
    private String password;

}