package es.jlrn.presentation.controller.temas;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.jlrn.presentation.dto.temas.ChapterDTO;
import es.jlrn.presentation.dto.temas.LessonDTO;
import es.jlrn.services.interfaces.temas.IChapterService;
import es.jlrn.services.interfaces.temas.ILessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/lessons") 
@RequiredArgsConstructor
class LessonController {
//    
    private final ILessonService lessonService;
    private final IChapterService chapterService;

    
    @GetMapping("/{id}/chapters")
    public ResponseEntity<List<ChapterDTO>> getChapters(@PathVariable Long id) {
        return ResponseEntity.ok(chapterService.getChaptersByLessonId(id));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLesson(id));
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getAllLesson() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(@Valid @RequestBody LessonDTO dto) {
        return ResponseEntity.ok(lessonService.createLesson(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonDTO dto) {
        return ResponseEntity.ok(lessonService.updateLesson(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

}