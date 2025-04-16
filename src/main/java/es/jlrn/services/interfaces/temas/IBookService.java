package es.jlrn.services.interfaces.temas;

import java.util.List;

import es.jlrn.persistence.model.temas.Book;
import es.jlrn.presentation.dto.temas.BookDTO;

public interface IBookService {
//
     List<Book> findByLevel(Long id);
     List<BookDTO> getBooksByLevelId(Long levelId);
     BookDTO getBookById(Long id);
     //
     BookDTO getBook(Long id);
     List<BookDTO> getAllBooks();
     BookDTO createBook(BookDTO bookDTO);
     BookDTO updateBook(Long id, BookDTO bookDTO);
     void deleteBook(Long id);
}
