package es.jlrn.persistence.model.temas;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pages")
public class Page {
//    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El número de página no puede ser nulo")
    @Min(value = 1, message = "El número de página debe ser mayor o igual a 1")
    private int pageNumber;

    @NotNull(message = "El título no puede ser nulo")
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 1, max = 150, message = "El título debe tener entre 1 y 150 caracteres")
    private String title;

    @Lob
    @NotNull(message = "El contenido no puede ser nulo")
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subtopic_id")
    @NotNull(message = "El subtema es obligatorio")
    private Subtopic subtopic;

}
