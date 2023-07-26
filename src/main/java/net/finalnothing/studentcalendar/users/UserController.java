package net.finalnothing.studentcalendar.users;

import net.finalnothing.studentcalendar.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(UserResponse::new).toList();
    }

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .build();
        userRepository.save(user);
        return new UserResponse(user);
    }

    @GetMapping("/me")
    public UserResponse details(User user) throws Exception {
        return new UserResponse(user);
    }
}
