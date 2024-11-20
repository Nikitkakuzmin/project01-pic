package kz.nik.project01userservice.dto;



import kz.nik.project01userservice.model.Role;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles;


}
