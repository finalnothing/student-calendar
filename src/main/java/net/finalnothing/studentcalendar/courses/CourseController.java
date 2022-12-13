package net.finalnothing.studentcalendar.courses;

import net.finalnothing.studentcalendar.model.Course;
import net.finalnothing.studentcalendar.model.User;
import net.finalnothing.studentcalendar.users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseController(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Course> list(User user) {
        return courseRepository.findAllByCreatedBy_Id(user.getId());
    }

    @PostMapping
    public Course create(@RequestBody CreateCourseRequest request, User user) {
        var course = Course.builder()
                .name(request.getName())
                .color(request.getColor())
                .weekday(request.getWeekday())
                .fromTime(request.getFromTime())
                .toTime(request.getToTime())
                .validUntil(request.getValidUntil())
                .location(request.getLocation())
                .createdBy(user)
                .build();
        courseRepository.save(course);
        return course;
    }

    @GetMapping("/{id}")
    public Course details(@PathVariable long id) throws Exception {
        Optional<Course> course = courseRepository.findById(id);

        if (course.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found");
        }

        return course.get();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        var course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found");
        }
        courseRepository.delete(course.get());
    }
}
