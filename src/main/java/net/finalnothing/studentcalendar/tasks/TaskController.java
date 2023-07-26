package net.finalnothing.studentcalendar.tasks;

import net.finalnothing.studentcalendar.events.EventRepository;
import net.finalnothing.studentcalendar.model.Task;
import net.finalnothing.studentcalendar.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    public TaskController(TaskRepository taskRepository, EventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<TaskInListResponse> list(User user) {
        return taskRepository.findAllByEvent_Course_CreatedBy_Id(user.getId())
                .stream().map(TaskInListResponse::new).toList();
    }

    @PostMapping
    public TaskResponse create(@RequestBody CreateTaskRequest request, User user) {
        var event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "event not found"));
        if (event.getCourse().getCreatedBy() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "insufficient permissions");
        }

        var task = Task.builder()
                .event(event)
                .title(request.getTitle())
                .build();
        taskRepository.save(task);
        return new TaskResponse(task);
    }

    private Task findTask(long id, User user) throws Exception {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "task not found"));
        if (task.getEvent().getCourse().getCreatedBy() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "insufficient permissions");
        }
        return task;
    }

    @GetMapping("/{id}")
    public TaskResponse details(@PathVariable long id, User user) throws Exception {
        return new TaskResponse(findTask(id, user));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody UpdateTaskRequest request, User user) throws Exception {
        var task = findTask(id, user);
        task.setTitle(request.getTitle());
        task.setCompleted(request.isCompleted());
        taskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, User user) throws Exception {
        var task = findTask(id, user);
        taskRepository.delete(task);
    }
}
