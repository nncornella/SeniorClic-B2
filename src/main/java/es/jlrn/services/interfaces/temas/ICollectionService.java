package es.jlrn.services.interfaces.temas;

import java.util.List;

import es.jlrn.presentation.dto.temas.CollectionDTO;

public interface ICollectionService {
//
    CollectionDTO getCollectionById(Long id);
    List<CollectionDTO> getAllCollections();
    //
    CollectionDTO createCollection(CollectionDTO collectionDTO);
    CollectionDTO updateCollection(Long id, CollectionDTO collectionDTO);
    void deleteCollection(Long id);
}
