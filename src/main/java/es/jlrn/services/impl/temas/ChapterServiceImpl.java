package es.jlrn.services.impl.temas;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.persistence.model.temas.Chapter;
import es.jlrn.persistence.model.temas.Lesson;
import es.jlrn.persistence.repositories.temas.ChapterRepository;
import es.jlrn.persistence.repositories.temas.LessonRepository;
import es.jlrn.presentation.dto.temas.ChapterDTO;
import es.jlrn.services.interfaces.temas.IChapterService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class ChapterServiceImpl implements IChapterService{
//

    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ChapterDTO> getChaptersByLessonId(Long lessonId) {
    //
        return lessonRepository.findById(lessonId)
                .map(lesson -> lesson.getChapters().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .orElseThrow();    
    }

    @Override
    @Transactional(readOnly = true)
    public ChapterDTO getChapterById(Long id) {
    //
        return chapterRepository.findById(id).map(this::convertToDTO).orElse(null);    
    }

    //
    
    @Override
    @Transactional(readOnly = true)
    public ChapterDTO getChapter(Long id) {
    //
        Chapter chapter = chapterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Capítulo no encontrado"));
        return convertToDTO(chapter);    
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChapterDTO> getAllChapters() {
    //
        return chapterRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChapterDTO createChapter(ChapterDTO dto) {
    //
        Chapter chapter = convertToEntity(dto);
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lección no encontrada"));
        chapter.setLesson(lesson);
        return convertToDTO(chapterRepository.save(chapter));    
    }

    @Override
    @Transactional
    public ChapterDTO updateChapter(Long id, ChapterDTO dto) {
    //
        Chapter chapter = chapterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Capítulo no encontrado"));

        chapter.setTitle(dto.getTitle());

        if (!chapter.getLesson().getId().equals(dto.getLessonId())) {
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lección no encontrada"));
        chapter.setLesson(lesson);
        }

        return convertToDTO(chapterRepository.save(chapter));
    }

    @Override
    @Transactional
    public void deleteChapter(Long id) {
    //
        if (!chapterRepository.existsById(id)) {
            throw new RuntimeException("Capítulo no encontrado");
        }
        chapterRepository.deleteById(id);    
    }

    // Mapeos

    public ChapterDTO convertToDTO(Chapter chapter) {
        return modelMapper.map(chapter, ChapterDTO.class);
    }

    public Chapter convertToEntity(ChapterDTO dto) {
        return modelMapper.map(dto, Chapter.class);
    }

    
}