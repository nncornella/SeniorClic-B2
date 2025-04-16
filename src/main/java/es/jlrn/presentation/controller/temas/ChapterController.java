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
import es.jlrn.presentation.dto.temas.TopicDTO;
import es.jlrn.services.interfaces.temas.IChapterService;
import es.jlrn.services.interfaces.temas.ITopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/chapters") 
@RequiredArgsConstructor
public class ChapterController {
//    
    private final IChapterService chapterService;
    private final ITopicService topicService;
    

    @GetMapping("/{id}/topics")
    public ResponseEntity<List<TopicDTO>> getTopics(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopicsByChapterId(id));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ChapterDTO> getChapter(@PathVariable Long id) {
        return ResponseEntity.ok(chapterService.getChapter(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ChapterDTO>> getAllChapter() {
        return ResponseEntity.ok(chapterService.getAllChapters());
    }

    @PostMapping("/create")
    public ResponseEntity<ChapterDTO> createChapter(@Valid @RequestBody ChapterDTO dto) {
        return ResponseEntity.ok(chapterService.createChapter(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ChapterDTO> updateChapter(@PathVariable Long id, @Valid @RequestBody ChapterDTO dto) {
        return ResponseEntity.ok(chapterService.updateChapter(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

}