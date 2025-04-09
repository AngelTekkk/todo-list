package de.dreamteam.todolist.service;

import de.dreamteam.todolist.entity.Curriculum;
import de.dreamteam.todolist.repository.CurriculumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;

    public CurriculumService(CurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    // Erstellung eines neuen Lehrplans (Curriculum)
    public Curriculum createCurriculum(Curriculum curriculum) {
        return curriculumRepository.save(curriculum);
    }

    // Eine Liste aller Lehrpläne erhalten
    public List<Curriculum> getAllCurriculum() {
        return curriculumRepository.findAll();
    }

    // Abrufen des Lehrplans nach ID
    public Optional<Curriculum> getCurriculumById(Long id) {
        return curriculumRepository.findById(id);
    }

    // Aktualisierung des Lehrplans auf id
    public Optional<Curriculum> updateCurriculum(Long id, Curriculum curriculumDetails) {
        return curriculumRepository.findById(id).map(existingCurriculum -> {
            existingCurriculum.setTitle(curriculumDetails.getTitle());
            // Aktualisieren der Benutzerzuordnung
            existingCurriculum.setUser(curriculumDetails.getUser());

            return curriculumRepository.save(existingCurriculum);
        });
    }

    // Löschen eines Lehrplans nach ID
    public void deleteCurriculum(Long id) {
        curriculumRepository.deleteById(id);
    }
}

