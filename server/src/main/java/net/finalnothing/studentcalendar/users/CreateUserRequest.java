package net.finalnothing.studentcalendar.users;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateUserRequest implements Serializable {
    private String email;
    private String password;
    private String username;
}
