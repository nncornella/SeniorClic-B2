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

import es.jlrn.presentation.dto.temas.SubtopicDTO;
import es.jlrn.presentation.dto.temas.TopicDTO;
import es.jlrn.services.interfaces.temas.ISubtopicService;
import es.jlrn.services.interfaces.temas.ITopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/topics") 
@RequiredArgsConstructor
public class TopicController {
//    
    private final ITopicService topicService;
    private final ISubtopicService subtopicService;

    @GetMapping("/{id}/subtopics")
    public List<SubtopicDTO> getSubtopics(@PathVariable Long id) {
        return subtopicService.getSubtopicsByTopicId(id);
    }
   
    
    @GetMapping("/find/{id}")
    public ResponseEntity<TopicDTO> getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopic(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TopicDTO>> getAllTopic() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @PostMapping("/create")
    public ResponseEntity<TopicDTO> createTopic(@Valid @RequestBody TopicDTO dto) {
        return ResponseEntity.ok(topicService.createTopic(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TopicDTO> updateTopic(@PathVariable Long id, @Valid @RequestBody TopicDTO dto) {
        return ResponseEntity.ok(topicService.updateTopic(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}