package net.finalnothing.studentcalendar.security;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;
}
