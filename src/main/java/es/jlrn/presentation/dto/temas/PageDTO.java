package es.jlrn.presentation.dto.temas;

import jakarta.validation.constraints.Min;
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
public class PageDTO {
//    
    private Long id;

    @NotNull(message = "El número de página no puede ser nulo")
    @Min(value = 1, message = "El número de página debe ser mayor o igual a 1")
    private int pageNumber;

    @NotNull(message = "El título no puede ser nulo")
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 1, max = 150, message = "El título debe tener entre 1 y 150 caracteres")
    private String title;

    @NotNull(message = "El contenido no puede ser nulo")
    private String content;

    @NotNull(message = "El ID del subtema es obligatorio")
    private Long subtopicId;
}
