package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.entity.Curriculum;
import de.dreamteam.todolist.service.CurriculumService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {

    private final CurriculumService curriculumService;

    public CurriculumController(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @PostMapping
    public ResponseEntity<Curriculum> createCurriculum(@Valid @RequestBody Curriculum curriculum) {
        Curriculum createdCurriculum = curriculumService.createCurriculum(curriculum);
        return ResponseEntity.ok(createdCurriculum);
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

    @PutMapping("/{id}")
    public ResponseEntity<Curriculum> updateCurriculum(@PathVariable Long id, @Valid @RequestBody Curriculum curriculumDetails) {
        return curriculumService.updateCurriculum(id, curriculumDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
        curriculumService.deleteCurriculum(id);
        return ResponseEntity.noContent().build();
    }
}

