package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.controller.payload.CurriculumRequest;
import de.dreamteam.todolist.controller.payload.CurriculumResponse;
import de.dreamteam.todolist.controller.payload.ToDoCurriculumRequest;
import de.dreamteam.todolist.controller.payload.ToDoCurriculumResponse;
import de.dreamteam.todolist.entity.Curriculum;
import de.dreamteam.todolist.entity.ToDoCurriculum;
import de.dreamteam.todolist.entity.User;
import de.dreamteam.todolist.service.CurriculumService;
import de.dreamteam.todolist.service.ToDoCurriculumService;
import de.dreamteam.todolist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo-list-api/curriculums")
public class CurriculumController {

    private final CurriculumService curriculumService;
    private final UserService userService;
    private final ToDoCurriculumService toDoCurriculumService;

    public CurriculumController(CurriculumService curriculumService, UserService userService, ToDoCurriculumService toDoCurriculumService) {
        this.curriculumService = curriculumService;
        this.userService = userService;
        this.toDoCurriculumService = toDoCurriculumService;
    }

    @PostMapping
    public ResponseEntity<CurriculumResponse> createCurriculum(@Valid @RequestBody CurriculumRequest curriculumRequest) {
        // Benutzer nach ID abrufen
        User user = userService.getUserById(curriculumRequest.userId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Erstellen eines Curriculum-Objekts
        Curriculum curriculum = Curriculum.builder()
                .title(curriculumRequest.title())
                .user(user)
                .build();

        Curriculum createdCurriculum = curriculumService.createCurriculum(curriculum);
        CurriculumResponse response = CurriculumResponse.builder()
                .id(createdCurriculum.getId())
                .title(createdCurriculum.getTitle())
                .userId(createdCurriculum.getUser().getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{curriculumId}/add-todo")
    public ResponseEntity<ToDoCurriculumResponse> addToDoToCurriculum(
            @PathVariable Long curriculumId,
            @Valid @RequestBody ToDoCurriculumRequest request) {

        ToDoCurriculum association = toDoCurriculumService.addToDoToCurriculum(
                curriculumId,
                request.todoId(),
                request.startDate(),
                request.endDate()
        );

        ToDoCurriculumResponse response = ToDoCurriculumResponse.builder()
                .id(association.getId())
                .curriculumId(association.getCurriculum().getId())
                .todoId(association.getToDo().getId())
                .startDate(association.getStartDate())
                .endDate(association.getEndDate())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Curriculum>> getAllCurriculum() {
        List<Curriculum> curriculumList = curriculumService.getAllCurriculum();
        return ResponseEntity.ok(curriculumList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curriculum> getCurriculumById(@PathVariable Long id) {
        return curriculumService.getCurriculumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{curriculumId}/todos")
    public ResponseEntity<List<ToDoCurriculumResponse>> getTasksForCurriculum(@PathVariable Long curriculumId) {
        // Abrufen des Lehrplans anhand der ID oder Auslösen einer Ausnahme, wenn nicht gefunden
        Curriculum curriculum = curriculumService.getCurriculumById(curriculumId)
                .orElseThrow(() -> new RuntimeException("Curriculum not found with id: " + curriculumId));

        // Abrufen der ToDoCurriculum-Zuordnungsliste
        List<ToDoCurriculumResponse> responses = curriculum.getToDoCurriculumList().stream()
                .map(association -> ToDoCurriculumResponse.builder()
                        .id(association.getId())
                        .curriculumId(association.getCurriculum().getId())
                        .todoId(association.getToDo().getId())
                        .startDate(association.getStartDate())
                        .endDate(association.getEndDate())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Curriculum> updateCurriculum(@PathVariable Long id, @Valid @RequestBody Curriculum curriculumDetails) {
        return curriculumService.updateCurriculum(id, curriculumDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{curriculumId}/update-todo/{toDoId}")
    public ResponseEntity<ToDoCurriculumResponse> updateToDoDates(
            @PathVariable Long curriculumId,
            @PathVariable Long toDoId,
            @Valid @RequestBody ToDoCurriculumRequest toDoCurriculumRequest) {

        ToDoCurriculum updatedAssociation = toDoCurriculumService.updateToDoCurriculumDates(
                curriculumId,
                toDoId,
                toDoCurriculumRequest.startDate(),
                toDoCurriculumRequest.endDate()
        );

        ToDoCurriculumResponse response = ToDoCurriculumResponse.builder()
                .id(updatedAssociation.getId())
                .curriculumId(updatedAssociation.getCurriculum().getId())
                .todoId(updatedAssociation.getToDo().getId())
                .startDate(updatedAssociation.getStartDate())
                .endDate(updatedAssociation.getEndDate())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
        curriculumService.deleteCurriculum(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{curriculumId}/remove-todo/{toDoId}")
    public ResponseEntity<Void> removeTaskFromCurriculum(
            @PathVariable Long curriculumId,
            @PathVariable Long toDoId) {
        toDoCurriculumService.removeToDoFromCurriculum(curriculumId, toDoId);
        return ResponseEntity.noContent().build();
    }
}

