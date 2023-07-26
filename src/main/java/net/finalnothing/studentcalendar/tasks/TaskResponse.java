package net.finalnothing.studentcalendar.tasks;

import net.finalnothing.studentcalendar.model.Task;

public class TaskResponse extends TaskInListResponse {
    private EventInTaskResponse event;

    public TaskResponse(Task task) {
        super(task);
        event = new EventInTaskResponse(task.getEvent());
    }
}
