package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.controller.payload.NewUserPayload;
import de.dreamteam.todolist.controller.payload.UpdateUserPayload;
import de.dreamteam.todolist.entity.User;
import de.dreamteam.todolist.repository.UserRepository;
import de.dreamteam.todolist.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("todo-list-api/users")
public class UserRestController {

    private final UserRepository userRepository;

    private final UserService userService;

    @GetMapping
    public User findUser(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUserPayload payload,
                                        BindingResult bindingResult)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.userService.saveUser(payload);
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserPayload payload,
                                        BindingResult bindingResult)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.userService.updateUser(payload);
            return ResponseEntity.noContent()
                    .build();
        }
    }
}
