package es.jlrn.presentation.dto.temas;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDTO {
//    
    private Long id;

    @NotNull(message = "El título no puede ser nulo")
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 1, max = 150, message = "El título debe tener entre 1 y 150 caracteres")
    private String title;

    @NotNull(message = "El ID de la lección es obligatorio")
    private Long lessonId;

    // opcional: para incluir los temas si se desea
    private List<TopicDTO> topics;
}
