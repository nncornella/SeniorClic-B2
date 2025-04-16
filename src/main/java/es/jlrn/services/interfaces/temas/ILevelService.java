package es.jlrn.services.interfaces.temas;

import java.util.List;

import es.jlrn.presentation.dto.temas.BookDTO;
import es.jlrn.presentation.dto.temas.LevelDTO;

public interface ILevelService {
//
    List<BookDTO> getBooksByLevelId(Long levelId);
    List<LevelDTO> getLevelsByCollectionId(Long collectionId);
    //
    LevelDTO getLevelById(Long id);
    List<LevelDTO> getAllLevels();
    LevelDTO createLevel(LevelDTO levelDTO);
    LevelDTO updateLevel(Long id, LevelDTO levelDTO);
    void deleteLevel(Long id);

}
