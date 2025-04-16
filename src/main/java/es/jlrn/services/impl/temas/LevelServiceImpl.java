package es.jlrn.services.impl.temas;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.persistence.model.temas.Collection;
import es.jlrn.persistence.model.temas.LevelEntity;
import es.jlrn.persistence.repositories.temas.CollectionRepository;
import es.jlrn.persistence.repositories.temas.LearningLevelRepository;
import es.jlrn.presentation.dto.temas.BookDTO;
import es.jlrn.presentation.dto.temas.LevelDTO;
import es.jlrn.services.interfaces.temas.ILevelService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class LevelServiceImpl implements ILevelService{
//
    private final LearningLevelRepository levelRepository;
    private final CollectionRepository collectionRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByLevelId(Long levelId) {
        return levelRepository.findById(levelId)
            .map(level -> level.getBooks().stream().map(book -> {
                BookDTO dto = new BookDTO();
                dto.setId(book.getId());
                dto.setTitle(book.getTitle());
                return dto;
            }).collect(Collectors.toList()))
            .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LevelDTO> getLevelsByCollectionId(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .map(c -> c.getLevels().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public LevelDTO getLevelById(Long id) {
    //    
        Optional<LevelEntity> levelEntity = levelRepository.findById(id);
        if (levelEntity.isPresent()) {
            return convertToDTO(levelEntity.get());
        } else {
            throw new RuntimeException("Level no encontrado con id: " + id);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LevelDTO> getAllLevels() {
    //
        return levelRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LevelDTO createLevel(LevelDTO levelDTO) {
        // Mapear el DTO a entidad
        LevelEntity levelEntity = convertToEntity(levelDTO);
        
        // Recuperar y asignar la Collection a partir del collectionId del DTO
        if (levelDTO.getCollectionId() != null) {
            Optional<Collection> collectionOpt = collectionRepository.findById(levelDTO.getCollectionId());
            if (collectionOpt.isPresent()) {
                levelEntity.setCollection(collectionOpt.get());
            } else {
                throw new RuntimeException("Collection no encontrada con id: " + levelDTO.getCollectionId());
            }
        }
        
        LevelEntity savedEntity = levelRepository.save(levelEntity);
        return convertToDTO(savedEntity);
    }

    @Override
    @Transactional
    public LevelDTO updateLevel(Long id, LevelDTO levelDTO) {
    //    
        Optional<LevelEntity> optionalLevel = levelRepository.findById(id);
        if (optionalLevel.isPresent()) {
            LevelEntity levelToUpdate = optionalLevel.get();
            
            // Actualizar campos simples
            levelToUpdate.setName(levelDTO.getName());
            
            // Actualizar la relación con Collection si es necesario
            if (levelDTO.getCollectionId() != null &&
                (levelToUpdate.getCollection() == null ||
                !levelToUpdate.getCollection().getId().equals(levelDTO.getCollectionId()))) {
                Optional<Collection> collectionOpt = collectionRepository.findById(levelDTO.getCollectionId());
                if (collectionOpt.isPresent()) {
                    levelToUpdate.setCollection(collectionOpt.get());
                } else {
                    throw new RuntimeException("Collection no encontrada con id: " + levelDTO.getCollectionId());
                }
            }
            
            LevelEntity updatedEntity = levelRepository.save(levelToUpdate);
            return convertToDTO(updatedEntity);
        } else {
            throw new RuntimeException("Level no encontrado con id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteLevel(Long id) {
        if (levelRepository.existsById(id)) {
            levelRepository.deleteById(id);
        } else {
            throw new RuntimeException("Level no encontrado con id: " + id);
        }
    }
    // Mapeos

    // Método de conversión de entidad a DTO
    private LevelDTO convertToDTO(LevelEntity levelEntity) {
        LevelDTO levelDTO = modelMapper.map(levelEntity, LevelDTO.class);
        return levelDTO;
    }
    
    // Conversión de DTO a entidad (sin asignar la colección, que se hará manualmente)
    private LevelEntity convertToEntity(LevelDTO levelDTO) {
        LevelEntity levelEntity = modelMapper.map(levelDTO, LevelEntity.class);
        return levelEntity;
    }
}
