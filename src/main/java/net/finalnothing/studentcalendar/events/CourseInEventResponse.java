package net.finalnothing.studentcalendar.events;

import lombok.Data;
import net.finalnothing.studentcalendar.model.Course;

@Data
public class CourseInEventResponse {
    private Long id;
    private String name;
    private String color;

    public CourseInEventResponse(Course course) {
        id = course.getId();
        name = course.getName();
        color = course.getColor();
    }
}
