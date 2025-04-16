package es.jlrn.services.impl.temas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.persistence.model.temas.Collection;
import es.jlrn.persistence.model.temas.LevelEntity;
import es.jlrn.persistence.repositories.temas.CollectionRepository;
import es.jlrn.presentation.dto.temas.CollectionDTO;
import es.jlrn.presentation.dto.temas.LevelDTO;
import es.jlrn.services.interfaces.temas.ICollectionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements ICollectionService{
//    
    private final CollectionRepository collectionRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CollectionDTO> getAllCollections() {
    //    
        return collectionRepository.findAll().stream().map(collection -> {
            CollectionDTO dto = new CollectionDTO();
            dto.setId(collection.getId());
            dto.setName(collection.getName());

            List<LevelDTO> levels = collection.getLevels().stream().map(level -> {
                LevelDTO levelDTO = new LevelDTO();
                levelDTO.setId(level.getId());
                levelDTO.setName(level.getName());
                return levelDTO;
            }).collect(Collectors.toList());

            dto.setLevels(levels);
            return dto;
        }).collect(Collectors.toList());

        // return collectionRepository.findAll()
        // .stream()
        // .map(this::convertToDTO)
        // .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionDTO getCollectionById(Long id) {
    //    
        Optional<Collection> collectionOptional = collectionRepository.findById(id);
        if (collectionOptional.isPresent()) {
            return convertToDTO(collectionOptional.get());
        } else {
            throw new RuntimeException("Collection no encontrada con id: " + id);
        }
    }

    @Override
    @Transactional
    public CollectionDTO createCollection(CollectionDTO collectionDTO) {
    //
        Collection collection = convertToEntity(collectionDTO);
        Collection savedCollection = collectionRepository.save(collection);
        return convertToDTO(savedCollection);
    }

    @Override
    @Transactional
    public CollectionDTO updateCollection(Long id, CollectionDTO collectionDTO) {
    //    
        Optional<Collection> optionalCollection = collectionRepository.findById(id);
        if (optionalCollection.isPresent()) {
            Collection collectionToUpdate = optionalCollection.get();
            
            // Actualizar la propiedad simple
            collectionToUpdate.setName(collectionDTO.getName());
            
            // Procesar la actualización de los niveles (levels)
            // Crear un mapa de los niveles existentes por ID para facilitar la búsqueda
            Map<Long, LevelEntity> existingLevelsMap = collectionToUpdate.getLevels()
                .stream()
                .collect(Collectors.toMap(LevelEntity::getId, level -> level));

            // Lista para guardar los niveles actualizados o nuevos
            List<LevelEntity> updatedLevels = new ArrayList<>();

            // Iterar sobre la lista de LevelDTO enviados en la petición
            for (LevelDTO levelDTO : collectionDTO.getLevels()) {
                if (levelDTO.getId() != null) {
                    // Actualización de un nivel existente
                    LevelEntity existingLevel = existingLevelsMap.get(levelDTO.getId());
                    if (existingLevel != null) {
                        existingLevel.setName(levelDTO.getName());
                        // Si hay otras propiedades, actualízalas aquí
                        updatedLevels.add(existingLevel);
                    } else {
                        // Opcional: lanzar excepción o crear nuevo registro
                        // Por ejemplo, se puede crear un nuevo nivel si no se encuentra:
                        LevelEntity newLevel = new LevelEntity();
                        newLevel.setName(levelDTO.getName());
                        newLevel.setCollection(collectionToUpdate);
                        updatedLevels.add(newLevel);
                    }
                } else {
                    // Nuevo nivel sin ID (se crea)
                    LevelEntity newLevel = new LevelEntity();
                    newLevel.setName(levelDTO.getName());
                    newLevel.setCollection(collectionToUpdate); // Establecer la relación bidireccional
                    updatedLevels.add(newLevel);
                }
            }
        
            // Actualizar la lista de niveles de la colección:
            // 1. Se limpia la lista actual.
            // 2. Se agregan los niveles que vienen en el DTO.
            // Aquellos niveles que ya no estén en la lista se eliminarán (orphanRemoval=true)
            collectionToUpdate.getLevels().clear();
            collectionToUpdate.getLevels().addAll(updatedLevels);
            
            // Persistir los cambios
            Collection updatedCollection = collectionRepository.save(collectionToUpdate);
            return convertToDTO(updatedCollection);
        } else {
            throw new RuntimeException("Collection no encontrada con id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteCollection(Long id) {
    //
        if(collectionRepository.existsById(id)) {
            collectionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Collection no encontrada con id: " + id);
        } 
    }

    // mapeos

    // Mapea de entity a DTO utilizando ModelMapper
    private CollectionDTO convertToDTO(Collection collection) {
        return modelMapper.map(collection, CollectionDTO.class);
    }
    
    // Mapea de DTO a entity utilizando ModelMapper
    private Collection convertToEntity(CollectionDTO collectionDTO) {
        return modelMapper.map(collectionDTO, Collection.class);
    }
}
