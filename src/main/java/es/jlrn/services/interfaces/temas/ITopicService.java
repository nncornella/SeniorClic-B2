package es.jlrn.services.interfaces.temas;

import java.util.List;

import es.jlrn.presentation.dto.temas.TopicDTO;

public interface ITopicService {
//
    List<TopicDTO> getTopicsByChapterId(Long chapterId);
    TopicDTO getTopicById(Long id);
    //
    TopicDTO getTopic(Long id);
    List<TopicDTO> getAllTopics();
    TopicDTO createTopic(TopicDTO dto);
    TopicDTO updateTopic(Long id, TopicDTO dto);
    void deleteTopic(Long id);
}    
