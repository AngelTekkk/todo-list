package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.controller.payload.NewToDoPayload;
import de.dreamteam.todolist.entity.ToDo;
import de.dreamteam.todolist.repository.ToDoRepository;
import de.dreamteam.todolist.service.ToDoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo-list-api/todos")
public class ToDoRestController {

    private final ToDoService toDoService;

    private final ToDoRepository toDoRepository;

    @GetMapping
    public List<ToDo> getAllToDos() {
        try {
            return toDoService.getAllToDos();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @PostMapping
    public ResponseEntity<ToDo> createToDo(@Valid @RequestBody NewToDoPayload newToDoPayload) {
        ToDo newToDo = toDoService.createToDo(newToDoPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(newToDo);
    }

    @PatchMapping("{toDoId:\\d+}")
    public ResponseEntity<ToDo> editToDo(@PathVariable Long toDoId, @Valid @RequestBody NewToDoPayload editToDo) {
        try {
            ToDo toDoToEdit = toDoService.editToDo(editToDo, toDoRepository.findById(toDoId).get());
            return ResponseEntity.status(HttpStatus.OK).body(toDoToEdit);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @DeleteMapping("{toDoToDeleteId:\\d+}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable Long toDoToDeleteId) {
        toDoRepository.deleteById(toDoToDeleteId);
        return ResponseEntity.noContent().build();
    }
}
