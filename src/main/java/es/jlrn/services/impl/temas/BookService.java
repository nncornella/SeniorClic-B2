package es.jlrn.services.impl.temas;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.persistence.model.temas.Book;
import es.jlrn.persistence.model.temas.LevelEntity;
import es.jlrn.persistence.repositories.temas.BookRepository;
import es.jlrn.persistence.repositories.temas.LearningLevelRepository;
import es.jlrn.presentation.dto.temas.BookDTO;
import es.jlrn.services.interfaces.temas.IBookService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class BookService implements IBookService{
//    
    private final BookRepository bookRepository;
    private final LearningLevelRepository levelRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Book> findByLevel(Long id) { 
        return bookRepository.findByLevelId(id); 
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByLevelId(Long levelId) {
    //    
        return levelRepository.findById(levelId)
                .map(level -> level.getBooks().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    // ***
    

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBook(Long id) {
    //
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        return convertToDTO(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {
    //
        return bookRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());    
    }

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
    //
        Book book = convertToEntity(bookDTO);

        LevelEntity level = levelRepository.findById(bookDTO.getLevelId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
        book.setLevel(level);

        Book saved = bookRepository.save(book);
        return convertToDTO(saved);      
    }

    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
    //
          Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        existing.setTitle(bookDTO.getTitle());

        if (!existing.getLevel().getId().equals(bookDTO.getLevelId())) {
            LevelEntity level = levelRepository.findById(bookDTO.getLevelId())
                    .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
            existing.setLevel(level);
        }

        Book updated = bookRepository.save(existing);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
    //
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado");
        }
        bookRepository.deleteById(id);
    }

    // Mapeos

    public BookDTO convertToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public Book convertToEntity(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        return book;
    }
}
