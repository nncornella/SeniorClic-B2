package es.jlrn.services.interfaces.temas;

import java.util.List;

import es.jlrn.presentation.dto.temas.SubtopicDTO;

public interface ISubtopicService {
//
    List<SubtopicDTO> getSubtopicsByTopicId(Long topicId);
    SubtopicDTO getSubtopicById(Long id);
    //
    SubtopicDTO getSubtopic(Long id);
    List<SubtopicDTO> getAllSubtopics();
    SubtopicDTO createSubtopic(SubtopicDTO dto);
    SubtopicDTO updateSubtopic(Long id, SubtopicDTO dto);
    void deleteSubtopic(Long id);
}
