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

import es.jlrn.presentation.dto.temas.PageDTO;
import es.jlrn.presentation.dto.temas.SubtopicDTO;
import es.jlrn.services.interfaces.temas.IPageService;
import es.jlrn.services.interfaces.temas.ISubtopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/subtopics") 
@RequiredArgsConstructor
class SubtopicController {
//    
    private final ISubtopicService subtopicService;
    private final IPageService pageService;

    @GetMapping("/{id}/pages")
    public List<PageDTO> getPages(@PathVariable Long id) {
        return pageService.getPagesBySubtopicId(id);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<SubtopicDTO> getSubtopic(@PathVariable Long id) {
        return ResponseEntity.ok(subtopicService.getSubtopic(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<SubtopicDTO>> getAllSubtopic() {
        return ResponseEntity.ok(subtopicService.getAllSubtopics());
    }

    @PostMapping("/create")
    public ResponseEntity<SubtopicDTO> createSubtopic(@Valid @RequestBody SubtopicDTO dto) {
        return ResponseEntity.ok(subtopicService.createSubtopic(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SubtopicDTO> updateSubtopic(@PathVariable Long id, @Valid @RequestBody SubtopicDTO dto) {
        return ResponseEntity.ok(subtopicService.updateSubtopic(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubtopic(@PathVariable Long id) {
        subtopicService.deleteSubtopic(id);
        return ResponseEntity.noContent().build();
    }

}