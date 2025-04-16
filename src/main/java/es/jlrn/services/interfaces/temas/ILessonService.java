package es.jlrn.services.interfaces.temas;

import java.util.List;

import es.jlrn.presentation.dto.temas.LessonDTO;

public interface ILessonService {
//
    List<LessonDTO> getLessonsByBookId(Long bookId);
    LessonDTO getLessonById(Long id);
    //
    LessonDTO getLesson(Long id);
    List<LessonDTO> getAllLessons();
    LessonDTO createLesson(LessonDTO dto);
    LessonDTO updateLesson(Long id, LessonDTO dto);
    void deleteLesson(Long id);
}
