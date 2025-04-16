package es.jlrn.services.impl.temas;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.persistence.model.temas.Chapter;
import es.jlrn.persistence.model.temas.Topic;
import es.jlrn.persistence.repositories.temas.ChapterRepository;
import es.jlrn.persistence.repositories.temas.TopicRepository;
import es.jlrn.presentation.dto.temas.TopicDTO;
import es.jlrn.services.interfaces.temas.ITopicService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class TopicServiceImpl implements ITopicService{
//
    private final TopicRepository topicRepository;
    private final ChapterRepository chapterRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public List<TopicDTO> getTopicsByChapterId(Long chapterId) {
    //
        return chapterRepository.findById(chapterId)
                .map(ch -> ch.getTopics().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public TopicDTO getTopicById(Long id) {
    //
        return topicRepository.findById(id).map(this::convertToDTO).orElse(null);
    }


    @Override
    @Transactional(readOnly = true)
    public TopicDTO getTopic(Long id) {
    //
        Topic topic = topicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        return convertToDTO(topic);    
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicDTO> getAllTopics() {
    //
        return topicRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());    
    }

    @Override
    @Transactional
    public TopicDTO createTopic(TopicDTO dto) {
    //
        Topic topic = convertToEntity(dto);
        Chapter chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> new RuntimeException("Capítulo no encontrado"));
        topic.setChapter(chapter);
        return convertToDTO(topicRepository.save(topic));    
    }

    @Override
    @Transactional
    public TopicDTO updateTopic(Long id, TopicDTO dto) {
    //
        Topic topic = topicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        topic.setTitle(dto.getTitle());

        if (!topic.getChapter().getId().equals(dto.getChapterId())) {
        Chapter chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> new RuntimeException("Capítulo no encontrado"));
        topic.setChapter(chapter);
        }

        return convertToDTO(topicRepository.save(topic));    
    }

    @Override
    @Transactional
    public void deleteTopic(Long id) {
    //
        if (!topicRepository.existsById(id)) {
            throw new RuntimeException("Tema no encontrado");
        }

        topicRepository.deleteById(id);
    }


    // Mapeos

    public TopicDTO convertToDTO(Topic topic) {
        return modelMapper.map(topic, TopicDTO.class);
    }

    public Topic convertToEntity(TopicDTO dto) {
        return modelMapper.map(dto, Topic.class);
    }

}