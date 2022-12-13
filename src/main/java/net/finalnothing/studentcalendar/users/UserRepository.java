package net.finalnothing.studentcalendar.users;

import net.finalnothing.studentcalendar.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);
}