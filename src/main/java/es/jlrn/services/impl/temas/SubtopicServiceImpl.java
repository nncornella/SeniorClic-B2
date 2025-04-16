package es.jlrn.services.impl.temas;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.persistence.model.temas.Subtopic;
import es.jlrn.persistence.model.temas.Topic;
import es.jlrn.persistence.repositories.temas.SubtopicRepository;
import es.jlrn.persistence.repositories.temas.TopicRepository;
import es.jlrn.presentation.dto.temas.SubtopicDTO;
import es.jlrn.services.interfaces.temas.ISubtopicService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class SubtopicServiceImpl implements ISubtopicService{
//    
    private final SubtopicRepository subtopicRepository;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public List<SubtopicDTO> getSubtopicsByTopicId(Long topicId) {
    //
        return topicRepository.findById(topicId)
                .map(topic -> topic.getSubtopics().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .orElseThrow();    
    }

    @Override
    @Transactional(readOnly = true)
    public SubtopicDTO getSubtopicById(Long id) {
    //
    return subtopicRepository.findById(id).map(this::convertToDTO).orElse(null);    
    }
    
    @Override
    @Transactional(readOnly = true)
    public SubtopicDTO getSubtopic(Long id) {
    //
        Subtopic subtopic = subtopicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subtopic no encontrado"));
        return convertToDTO(subtopic);    
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubtopicDTO> getAllSubtopics() {
    //
        return subtopicRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());    
    }

    @Override
    @Transactional
    public SubtopicDTO createSubtopic(SubtopicDTO dto) {
    //
        Subtopic subtopic = convertToEntity(dto);
        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic no encontrado"));
        subtopic.setTopic(topic);
        return convertToDTO(subtopicRepository.save(subtopic));    
    }

    @Override
    @Transactional
    public SubtopicDTO updateSubtopic(Long id, SubtopicDTO dto) {
    //
        Subtopic subtopic = subtopicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subtopic no encontrado"));

        subtopic.setTitle(dto.getTitle());

        if (!subtopic.getTopic().getId().equals(dto.getTopicId())) {
        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic no encontrado"));
        subtopic.setTopic(topic);
        }

        return convertToDTO(subtopicRepository.save(subtopic));
    }

    @Override
    @Transactional
    public void deleteSubtopic(Long id) {
    //
        if (!subtopicRepository.existsById(id)) {
            throw new RuntimeException("Subtopic no encontrado");
        }
        subtopicRepository.deleteById(id);    
    }


    // Mapeos

    public SubtopicDTO convertToDTO(Subtopic subtopic) {
        return modelMapper.map(subtopic, SubtopicDTO.class);
    }

    public Subtopic convertToEntity(SubtopicDTO dto) {
        return modelMapper.map(dto, Subtopic.class);
    }
    
}