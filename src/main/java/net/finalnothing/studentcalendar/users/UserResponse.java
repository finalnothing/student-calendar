package net.finalnothing.studentcalendar.users;

import lombok.Data;
import net.finalnothing.studentcalendar.model.User;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;

    public UserResponse(User user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
    }
}
