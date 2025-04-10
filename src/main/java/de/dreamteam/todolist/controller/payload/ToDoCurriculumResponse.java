package de.dreamteam.todolist.controller.payload;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ToDoCurriculumResponse(
        Long id,
        Long curriculumId,
        Long todoId,
        LocalDate startDate,
        LocalDate endDate
) {}
