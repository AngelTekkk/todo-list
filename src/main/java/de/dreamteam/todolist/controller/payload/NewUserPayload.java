package de.dreamteam.todolist.controller.payload;

import jakarta.validation.constraints.*;

public record NewUserPayload(

        @NotEmpty(message = "{user.create.errors.first_name_is_empty}")
        @Size(min = 3, max = 255, message = "{user.create.errors.first_name_is_invalid}")
        String firstName,

        @NotEmpty(message = "{user.create.errors.last_name_is_empty}")
        @Size(min = 3, max = 255, message = "{user.create.errors.last_name_is_invalid}")
        String lastName,

        @NotEmpty(message = "{user.create.errors.username_is_empty}")
        @Size(min = 5, max = 255, message = "{user.create.errors.username_is_invalid}")
        String username,

        @NotEmpty(message = "{user.create.errors.email_is_empty}")
        @Email(message = "{user.create.errors.email_is_invalid}")
        String email,

        @NotEmpty(message = "{user.create.errors.password_is_empty}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{8,}$",
                message = "{user.create.errors.password_is_invalid}")
        String password) {
}
