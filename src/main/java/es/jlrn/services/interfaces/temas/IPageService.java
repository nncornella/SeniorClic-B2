package es.jlrn.services.interfaces.temas;

import java.util.List;

import es.jlrn.presentation.dto.temas.PageDTO;

public interface IPageService {
//
    List<PageDTO> getPagesBySubtopicId(Long subtopicId);
    PageDTO getPageById(Long id);
    //
    PageDTO getPage(Long id);
    List<PageDTO> getAllPages();
    PageDTO createPage(PageDTO dto);
    PageDTO updatePage(Long id, PageDTO dto);
    void deletePage(Long id);
}
