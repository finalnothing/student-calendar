package net.finalnothing.studentcalendar.users;

import net.finalnothing.studentcalendar.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
}
