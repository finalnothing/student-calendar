package net.finalnothing.studentcalendar.courses;

import lombok.Data;
import net.finalnothing.studentcalendar.model.Course;

import java.util.List;

@Data
public class CourseResponse extends CourseInListResponse {
    private List<EventInCourseResponse> events;

    public CourseResponse(Course course) {
        super(course);
        events = course.getEvents().stream().map(EventInCourseResponse::new).toList();
    }
}
