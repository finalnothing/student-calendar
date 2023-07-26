package net.finalnothing.studentcalendar.courses;

import net.finalnothing.studentcalendar.model.Course;
import net.finalnothing.studentcalendar.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {
    Optional<Course> findById(long id);

    List<Course> findAllByCreatedBy_Id(long createdBy);
}
