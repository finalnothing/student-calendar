package net.finalnothing.studentcalendar.events;

import lombok.Data;
import net.finalnothing.studentcalendar.model.Task;

@Data
public class TaskInEventResponse {
    private Long id;
    private String title;
    private Boolean completed;

    public TaskInEventResponse(Task task) {
        id = task.getId();
        title = task.getTitle();
        completed = task.getCompleted();
    }
}
