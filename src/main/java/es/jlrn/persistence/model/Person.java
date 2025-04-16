package es.jlrn.persistence.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name="personas", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Person {
//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    @Column(name = "surnames", nullable = false, length = 100)
    private String surnames;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "La localidad no puede estar vacía")
    @Size(max = 100, message = "La localidad no puede exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String locality;

    @NotBlank(message = "La provincia no puede estar vacía")
    @Size(max = 100, message = "La provincia no puede exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String province;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")  // usar en DTOs
    @PastOrPresent(message = "La fecha de inicio no puede ser en el futuro")
    @Column(name = "start_date", nullable = false, columnDefinition = "DATE")
    private LocalDate startDate;

    @PastOrPresent(message = "La fecha de último acceso no puede ser en el futuro")
    @Column(name = "last_access_date", nullable = false, columnDefinition = "DATE")
    private LocalDate lastAccessDate;
}

