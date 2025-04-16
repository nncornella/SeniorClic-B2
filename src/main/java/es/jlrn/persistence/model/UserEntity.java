package es.jlrn.persistence.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class UserEntity implements UserDetails{
    //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

     @Column(nullable = false)
     private boolean activo;
    
    @ManyToMany(fetch = FetchType.EAGER) 
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();

    // Un usuario debe tener obligatoriamente una persona
    @OneToOne(optional = false) // 👈 Evita que un usuario exista sin persona
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false, unique = true)
    private Person person;
    
    //

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    //
        return roles.stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                    .collect(Collectors.toSet());
        }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }
    @Override
    public boolean isAccountNonLocked() {
       return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}

/*
 @Id
    
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
 */