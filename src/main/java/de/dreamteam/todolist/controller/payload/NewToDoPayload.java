package de.dreamteam.todolist.controller.payload;


import de.dreamteam.todolist.entity.Project;
import de.dreamteam.todolist.entity.ToDoCurriculum;
import de.dreamteam.todolist.entity.User;
import de.dreamteam.todolist.model.ToDoStatus;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public record NewToDoPayload(
        @NotEmpty
        @Size(min = 5, max = 255)
        String title,

        @NotEmpty
        @Size(min = 5, max = 255)
        String description,

        @FutureOrPresent
        LocalDate endDate,

        @FutureOrPresent
        LocalDate startDate,

        @NotNull
        ToDoStatus status,

        @Nullable
        Long projectId,

        @Nullable
        List<Long> curriculumIds,

        @Nullable
        List<Long> userIds
) {}
