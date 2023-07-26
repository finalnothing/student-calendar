package net.finalnothing.studentcalendar.courses;

import net.finalnothing.studentcalendar.events.EventRepository;
import net.finalnothing.studentcalendar.model.Course;
import net.finalnothing.studentcalendar.model.Event;
import net.finalnothing.studentcalendar.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseRepository courseRepository;
    private final EventRepository eventRepository;

    public CourseController(CourseRepository courseRepository, EventRepository eventRepository) {
        this.courseRepository = courseRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<CourseInListResponse> list(User user) {
        return courseRepository.findAllByCreatedBy_Id(user.getId()).stream().map(CourseInListResponse::new).toList();
    }

    private final Map<String, DayOfWeek> dayMapping = Map.of(
            "mon", DayOfWeek.MONDAY,
            "tue", DayOfWeek.TUESDAY,
            "wed", DayOfWeek.WEDNESDAY,
            "thu", DayOfWeek.THURSDAY,
            "fri", DayOfWeek.FRIDAY,
            "sat", DayOfWeek.SATURDAY,
            "sun", DayOfWeek.SUNDAY
    );

    private void createEventsForCourse(Course course) {
        var first = course.getValidFrom().with(TemporalAdjusters.next(dayMapping.get(course.getWeekday())));
        var last = course.getValidUntil().plusDays(1);
        List<Event> events = new ArrayList<>();
        first.datesUntil(last, Period.ofDays(7)).forEach((date) -> {
            var event = Event.builder()
                    .course(course)
                    .occursOn(date)
                    .name(course.getName())
                    .fromTime(course.getFromTime())
                    .toTime(course.getToTime())
                    .location(course.getLocation())
                    .build();
            eventRepository.save(event);
            events.add(event);
        });
        course.setEvents(events);
    }

    @PostMapping
    public CourseResponse create(@RequestBody CreateCourseRequest request, User user) {
        var course = Course.builder()
                .name(request.getName())
                .color(request.getColor())
                .weekday(request.getWeekday())
                .fromTime(request.getFromTime())
                .toTime(request.getToTime())
                .validFrom(request.getValidFrom())
                .validUntil(request.getValidUntil())
                .location(request.getLocation())
                .createdBy(user)
                .build();
        courseRepository.save(course);
        createEventsForCourse(course);
        return new CourseResponse(course);
    }

    @GetMapping("/{id}")
    public CourseResponse details(@PathVariable long id, User user) throws Exception {
        Optional<Course> course = courseRepository.findById(id);

        if (course.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found");
        }

        if (course.get().getCreatedBy() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "insufficient permissions");
        }

        return new CourseResponse(course.get());
    }

    private void updateCourseEvents(Course course) {
        course.getEvents().forEach((event -> {
            if (event.isChanged()) { return; }

            event.setName(course.getName());
            event.setFromTime(course.getFromTime());
            event.setToTime(course.getToTime());
            event.setLocation(course.getLocation());

            var newWeekday = dayMapping.get(course.getWeekday());
            event.setOccursOn(event.getOccursOn().with(newWeekday));

            eventRepository.save(event);

            // TODO: Create or delete events for changed border values.
        }));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody CreateCourseRequest request, User user) throws Exception {
        var course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found");
        }
        if (course.get().getCreatedBy() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "insufficient permissions");
        }

        var entity = course.get();
        entity.setName(request.getName());
        entity.setColor(request.getColor());
        entity.setWeekday(request.getWeekday());
        entity.setFromTime(request.getFromTime());
        entity.setToTime(request.getToTime());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidUntil(request.getValidUntil());
        entity.setLocation(request.getLocation());
        courseRepository.save(entity);

        updateCourseEvents(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, User user) throws Exception {
        var course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found");
        }
        if (course.get().getCreatedBy() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "insufficient permissions");
        }

        courseRepository.delete(course.get());
    }

    @GetMapping("/{id}/events")
    public List<EventInCourseResponse> listEvents(@PathVariable long id, User user) throws Exception {
        var course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found");
        }
        if (course.get().getCreatedBy() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "insufficient permissions");
        }

        return course.get().getEvents().stream().map(EventInCourseResponse::new).toList();
    }
}
