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

import es.jlrn.presentation.dto.temas.CollectionDTO;
import es.jlrn.presentation.dto.temas.LevelDTO;
import es.jlrn.services.interfaces.temas.ICollectionService;
import es.jlrn.services.interfaces.temas.ILevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {
//
    private final ICollectionService collectionService;
    private final ILevelService learningLevelService;

    // Obtener una colección por ID (GET)
    @GetMapping("/find/{id}")
    public ResponseEntity<CollectionDTO> getCollection(@PathVariable Long id) {
        CollectionDTO collection = collectionService.getCollectionById(id);
        return ResponseEntity.ok(collection);
    }

     // Obtener todas las colecciones (GET)
     @GetMapping("/findAll")
    public ResponseEntity<List<CollectionDTO>> getAllCollections() {
        List<CollectionDTO> collections = collectionService.getAllCollections();
        return ResponseEntity.ok(collections);
    }

    // Crear una nueva colección (POST)
    @PostMapping("/create")
    public ResponseEntity<CollectionDTO> createCollection(@Valid @RequestBody CollectionDTO collectionDTO) {
        CollectionDTO createdCollection = collectionService.createCollection(collectionDTO);
        return ResponseEntity.ok(createdCollection);
    }
    
    // Actualizar una colección (PUT)
    @PutMapping("/update/{id}")
    public ResponseEntity<CollectionDTO> updateCollection(@PathVariable Long id, 
                                                          @Valid @RequestBody CollectionDTO collectionDTO) {
        CollectionDTO updatedCollection = collectionService.updateCollection(id, collectionDTO);
        return ResponseEntity.ok(updatedCollection);
    }
    
    // Eliminar una colección (DELETE)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/levels")
    public List<LevelDTO> getLevelsByCollection(@PathVariable Long id) {
        return learningLevelService.getLevelsByCollectionId(id);
    }
}
