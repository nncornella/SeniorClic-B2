package es.jlrn.services.impl.temas;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.persistence.model.temas.Page;
import es.jlrn.persistence.model.temas.Subtopic;
import es.jlrn.persistence.repositories.temas.PageRepository;
import es.jlrn.persistence.repositories.temas.SubtopicRepository;
import es.jlrn.presentation.dto.temas.PageDTO;
import es.jlrn.services.interfaces.temas.IPageService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class PageServiceimpl implements IPageService{
//    
    private final PageRepository pageRepository;
    private final SubtopicRepository subtopicRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public List<PageDTO> getPagesBySubtopicId(Long subtopicId) {
    //
        return subtopicRepository.findById(subtopicId)
                .map(st -> st.getPages().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getPageById(Long id) {
    //
        return pageRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO getPage(Long id) {
    //
        Page page = pageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Página no encontrada"));
        return convertToDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PageDTO> getAllPages() {
    //
        return pageRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());     
        }

    @Override
    @Transactional
    public PageDTO createPage(PageDTO dto) {
    //
        Page page = convertToEntity(dto);
        Subtopic subtopic = subtopicRepository.findById(dto.getSubtopicId())
                .orElseThrow(() -> new RuntimeException("Subtema no encontrado"));
        page.setSubtopic(subtopic);
        return convertToDTO(pageRepository.save(page));    
    }

    @Override
    @Transactional
    public PageDTO updatePage(Long id, PageDTO dto) {
    //
        Page page = pageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Página no encontrada"));

        page.setPageNumber(dto.getPageNumber());
        page.setTitle(dto.getTitle());
        page.setContent(dto.getContent());

        if (!page.getSubtopic().getId().equals(dto.getSubtopicId())) {
        Subtopic subtopic = subtopicRepository.findById(dto.getSubtopicId())
                .orElseThrow(() -> new RuntimeException("Subtema no encontrado"));
        page.setSubtopic(subtopic);
        }

        return convertToDTO(pageRepository.save(page));    
    }

    @Override
    @Transactional
    public void deletePage(Long id) {
    //
        if (!pageRepository.existsById(id)) {
            throw new RuntimeException("Página no encontrada");
        }
        pageRepository.deleteById(id);    
    }


    // Mapeos

    public PageDTO convertToDTO(Page page) {
        return modelMapper.map(page, PageDTO.class);
    }

    public Page convertToEntity(PageDTO dto) {
        return modelMapper.map(dto, Page.class);
    }

    
}