package es.jlrn.services.impl.temas;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.persistence.model.temas.Book;
import es.jlrn.persistence.model.temas.Lesson;
import es.jlrn.persistence.repositories.temas.BookRepository;
import es.jlrn.persistence.repositories.temas.LessonRepository;
import es.jlrn.presentation.dto.temas.LessonDTO;
import es.jlrn.services.interfaces.temas.ILessonService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class LessonServiceImpl implements ILessonService{
//
    private final LessonRepository lessonRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    
    @Override
    @Transactional(readOnly = true)
    public List<LessonDTO> getLessonsByBookId(Long bookId) {
    //
        return bookRepository.findById(bookId)
                .map(book -> book.getLessons().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .orElseThrow();
    }
    
    @Override
    @Transactional(readOnly = true)
    public LessonDTO getLesson(Long id) {
    //    
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lección no encontrada"));
        return convertToDTO(lesson);

    }

    @Override
    @Transactional(readOnly = true)
    public LessonDTO getLessonById(Long id) {
    //
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lección no encontrada"));
        return convertToDTO(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonDTO> getAllLessons() {
    //
        return lessonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LessonDTO createLesson(LessonDTO dto) {
    //
        Lesson lesson = convertToEntity(dto);
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        lesson.setBook(book);
        return convertToDTO(lessonRepository.save(lesson));    
    }

    @Override
    @Transactional
    public LessonDTO updateLesson(Long id, LessonDTO dto) {
    //
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lección no encontrada"));

        lesson.setTitle(dto.getTitle());

        if (!lesson.getBook().getId().equals(dto.getBookId())) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        lesson.setBook(book);
        }

        return convertToDTO(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public void deleteLesson(Long id) {
    //
        if (!lessonRepository.existsById(id)) {
            throw new RuntimeException("Lección no encontrada");
        }
        lessonRepository.deleteById(id);    
    }    


    // Mapeos

    public LessonDTO convertToDTO(Lesson lesson) {
        return modelMapper.map(lesson, LessonDTO.class);
    }

    public Lesson convertToEntity(LessonDTO dto) {
        return modelMapper.map(dto, Lesson.class);
    }

    
}