package net.finalnothing.studentcalendar.tasks;

import net.finalnothing.studentcalendar.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByEvent_Course_CreatedBy_Id(long id);
}
