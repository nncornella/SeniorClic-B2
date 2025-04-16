package es.jlrn.presentation.controller.temas;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.jlrn.presentation.dto.temas.BookDTO;
import es.jlrn.presentation.dto.temas.LevelDTO;
import es.jlrn.services.interfaces.temas.IBookService;
import es.jlrn.services.interfaces.temas.ILevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/levels") 
@RequiredArgsConstructor
class LevelController {
//    
    private final IBookService bookService;
    private final ILevelService levelService;


    @GetMapping("/{levelId}/books")
    public List<BookDTO> getBooksByLevel(@PathVariable Long levelId) {
        return  levelService.getBooksByLevelId(levelId);
    }

    @GetMapping("/{id}/books")
    public List<BookDTO> getBooks(@PathVariable Long id) {
        return bookService.getBooksByLevelId(id);
    }
    
    //

    // Obtener Level por ID
    @GetMapping("find/{id}")
    public ResponseEntity<LevelDTO> getLevel(@PathVariable Long id) {
        LevelDTO levelDTO = levelService.getLevelById(id);
        return ResponseEntity.ok(levelDTO);
    }
    
    // Obtener todos los Levels
    @GetMapping("/findAll")
    public ResponseEntity<List<LevelDTO>> getAllLevels() {
        List<LevelDTO> levels = levelService.getAllLevels();
        return ResponseEntity.ok(levels);
    }
    
    // Crear un nuevo Level
    @PostMapping("/create")
    public ResponseEntity<LevelDTO> createLevel(@Valid @RequestBody LevelDTO levelDTO) {
        LevelDTO createdLevel = levelService.createLevel(levelDTO);
        return ResponseEntity.ok(createdLevel);
    }

    // Actualizar Level
    @PutMapping("/update/{id}")
    public ResponseEntity<LevelDTO> updateLevel(@PathVariable Long id, @Valid @RequestBody LevelDTO levelDTO) {
        LevelDTO updatedLevel = levelService.updateLevel(id, levelDTO);
        return ResponseEntity.ok(updatedLevel);
    }
    
    // Eliminar Level
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        levelService.deleteLevel(id);
        return ResponseEntity.noContent().build();
    }



}