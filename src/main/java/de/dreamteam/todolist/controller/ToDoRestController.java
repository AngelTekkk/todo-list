package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.controller.payload.NewToDoPayload;
import de.dreamteam.todolist.controller.payload.NewUserPayload;
import de.dreamteam.todolist.controller.payload.UpdateToDoPayload;
import de.dreamteam.todolist.entity.ToDo;
import de.dreamteam.todolist.repository.ToDoRepository;
import de.dreamteam.todolist.service.ToDoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo-list-api/todos")
public class ToDoRestController {

    private final ToDoService toDoService;

    private final ToDoRepository toDoRepository;

    @GetMapping
    public List<NewToDoPayload> getAllToDos() {
        try {
            return toDoService.getAllToDos();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createToDo(@Valid @RequestBody NewToDoPayload payload) {
        toDoService.createToDo(payload);
        return ResponseEntity.status(HttpStatus.OK).body(payload);
//        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{toDoId:\\d+}")
    public ResponseEntity<?> updateToDo(@PathVariable Long toDoId, @Valid @RequestBody UpdateToDoPayload payload) {
        try {
            toDoService.updateToDo(payload, toDoRepository.findById(toDoId).orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(payload);
//            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @DeleteMapping("{toDoToDeleteId:\\d+}")
    public ResponseEntity<?> deleteToDo(@PathVariable Long toDoToDeleteId) {
        toDoRepository.deleteById(toDoToDeleteId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{toDoId:\\d+}/assign/project/{projectId:\\d+}")
    public ResponseEntity<?> assignToProject(@PathVariable Long toDoId, @PathVariable Long projectId) {
        ToDo toDo = toDoRepository.findById(toDoId).orElseThrow();
        toDoService.assignToProject(toDo, projectId);
        return ResponseEntity.noContent().build();
    }
}
