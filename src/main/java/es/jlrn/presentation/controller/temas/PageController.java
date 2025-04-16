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

import es.jlrn.presentation.dto.temas.PageDTO;
import es.jlrn.services.interfaces.temas.IPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class PageController {
//
    private final IPageService pageService;
    
    @GetMapping("/find/{id}")
    public ResponseEntity<PageDTO> getPage(@PathVariable Long id) {
        return ResponseEntity.ok(pageService.getPage(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PageDTO>> getAllPage() {
        return ResponseEntity.ok(pageService.getAllPages());
    }
    
    @PostMapping("/create")
    public ResponseEntity<PageDTO> createPage(@Valid @RequestBody PageDTO dto) {
        return ResponseEntity.ok(pageService.createPage(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PageDTO> updatePage(@PathVariable Long id, @Valid @RequestBody PageDTO dto) {
        return ResponseEntity.ok(pageService.updatePage(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        pageService.deletePage(id);
        return ResponseEntity.noContent().build();
    }
}   