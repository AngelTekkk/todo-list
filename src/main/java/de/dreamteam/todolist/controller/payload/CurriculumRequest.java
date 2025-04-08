package de.dreamteam.todolist.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CurriculumRequest(

        @NotNull(message = "Das Titelfeld darf nicht null sein.")
        @Size(max = 255, message = "Das Titelfeld darf nicht länger als 255 Zeichen sein.")
        String title,

        @NotNull(message = "Die Benutzer-ID darf nicht null sein.")
        Long userId
) {}

