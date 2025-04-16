package es.jlrn.services.interfaces.temas;

import java.util.List;

import es.jlrn.presentation.dto.temas.ChapterDTO;

public interface IChapterService {
//
    List<ChapterDTO> getChaptersByLessonId(Long lessonId);
    ChapterDTO getChapterById(Long id);
    //
    ChapterDTO getChapter(Long id);
    List<ChapterDTO> getAllChapters();
    ChapterDTO createChapter(ChapterDTO dto);
    ChapterDTO updateChapter(Long id, ChapterDTO dto);
    void deleteChapter(Long id);

}
