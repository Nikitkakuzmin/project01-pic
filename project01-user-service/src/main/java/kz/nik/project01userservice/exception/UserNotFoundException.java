package kz.nik.project01userservice.exception;

public class UserNotFoundException extends RuntimeException {
    private Long id;
    private String username;

    public UserNotFoundException(Long id){
        this.id = id;
    }
    public UserNotFoundException(String username){
        this.username = username;
    }

    @Override
    public String getMessage() {
        return "User with username " + username + " not found in users microservice";
    }
}
