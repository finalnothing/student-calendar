package net.finalnothing.studentcalendar.events;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class AddTaskRequest implements Serializable {
    private String title;
}
