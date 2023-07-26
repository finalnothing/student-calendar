package net.finalnothing.studentcalendar.events;

import net.finalnothing.studentcalendar.model.Event;
import net.finalnothing.studentcalendar.model.Task;
import net.finalnothing.studentcalendar.model.User;
import net.finalnothing.studentcalendar.tasks.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventRepository eventRepository;
    private final TaskRepository taskRepository;

    public EventController(EventRepository eventRepository, TaskRepository taskRepository) {
        this.eventRepository = eventRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<EventResponse> list(@RequestParam LocalDate from, @RequestParam LocalDate to, User user) {
        var events = eventRepository.findAllByCourse_CreatedBy_Id(user.getId()).stream();
        if (from != null) {
            events = events.filter(event -> event.getOccursOn().isAfter(from.minusDays(1)));
        }
        if (to != null) {
            events = events.filter(event -> event.getOccursOn().isBefore(to.plusDays(1)));
        }
        return events.map(EventResponse::new).toList();
    }

    private Event findEvent(long id, User user) throws Exception {
        var event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "event not found"));
        if (event.getCourse().getCreatedBy() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "insufficient permissions");
        }
        return event;
    }

    @GetMapping("/{id}")
    public EventResponse details(@PathVariable long id, User user) throws Exception {
        var event = findEvent(id, user);
        return new EventResponse(event);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody UpdateEventRequest request, User user) throws Exception {
        var event = findEvent(id, user);
        event.setOriginalOccursOn(event.getOccursOn());
        event.setOccursOn(request.getOccursOn());
        event.setName(request.getName());
        event.setFromTime(request.getFromTime());
        event.setToTime(request.getToTime());
        event.setLocation(request.getLocation());
        event.setChanged(true);
        eventRepository.save(event);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, User user) throws Exception {
        // Reset the event to its original (unchanged) state.
        var event = findEvent(id, user);
        event.setOccursOn(event.getOriginalOccursOn());
        event.setName(event.getCourse().getName());
        event.setFromTime(event.getCourse().getFromTime());
        event.setToTime(event.getCourse().getToTime());
        event.setLocation(event.getCourse().getLocation());
        event.setChanged(false);
        eventRepository.save(event);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskInEventResponse> listTasks(@PathVariable long id, User user) throws Exception {
        var event = findEvent(id, user);
        return event.getTasks().stream().map(TaskInEventResponse::new).toList();
    }

    @PostMapping("/{id}/tasks")
    public TaskInEventResponse addTask(@PathVariable long id, @RequestBody AddTaskRequest request, User user) throws Exception {
        var event = findEvent(id, user);
        var task = Task.builder()
                .event(event)
                .title(request.getTitle())
                .build();
        taskRepository.save(task);
        return new TaskInEventResponse(task);
    }
}
