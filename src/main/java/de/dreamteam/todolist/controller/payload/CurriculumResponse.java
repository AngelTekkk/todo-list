package de.dreamteam.todolist.controller.payload;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CurriculumResponse(
        Long id,

        @Size(max = 255, message = "Das Titelfeld darf nicht länger als 255 Zeichen sein.")
        String title,

        Long userId
) {}

