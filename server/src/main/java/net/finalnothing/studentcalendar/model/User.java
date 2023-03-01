package net.finalnothing.studentcalendar.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "users") // because hibernate does not escape the keyword "user"
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "username is required")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "email is required")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "password is required")
    private String password;
}
