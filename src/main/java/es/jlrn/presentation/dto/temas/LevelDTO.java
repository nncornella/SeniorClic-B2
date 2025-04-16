package es.jlrn.presentation.dto.temas;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelDTO {
//  
    private Long id;
    
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String name;
    
    @NotNull(message = "La colección es obligatoria")
    private Long collectionId; // Se utiliza el id para relacionar el DTO con la entidad Collection

    // Si requieres transferir información de los libros, podrías incluir una lista de BookDTO
    @Valid
    private List<BookDTO> books;
}
