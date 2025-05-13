package de.dreamteam.todolist.controller.payload;

import de.dreamteam.todolist.model.ToDoStatus;


import java.time.LocalDate;
import java.util.List;

public record UserTodoPayload(

        Long id,

        String title,

        String creator,

        String description,

        LocalDate endDate,

        LocalDate startDate,

        ToDoStatus status,

        Long projectId,

        List<Long> curriculumIds
) {
}