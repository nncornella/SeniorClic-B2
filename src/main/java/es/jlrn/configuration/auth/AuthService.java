package es.jlrn.configuration.auth;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jlrn.configuration.jwt.JwtService;
import es.jlrn.persistence.model.Person;
import es.jlrn.persistence.model.RoleEntity;
import es.jlrn.persistence.model.UserEntity;
import es.jlrn.persistence.repositories.PersonRepository;
import es.jlrn.persistence.repositories.RoleEntityRepository;
import es.jlrn.persistence.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional // 👈 todos los métodos son transaccionales por defecto
public class AuthService {
//
    private final PersonRepository personRepository; // Cambia esto por tu repositorio de UserEntity
    private final UserEntityRepository userRepository;
    private final RoleEntityRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
    //    
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();
    }

    @Transactional
    public AuthResponse registerUser(RegisterUserRequest request)  {
    //
        // 1. Verificar si el username ya existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        // 2. Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        // 3. Buscar persona por ID
        Person person = personRepository.findById(request.getPersonID())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + request.getPersonID()));

        // añadimosel correo        
        request.setEmail(person.getEmail());        

        // 4. Validar que el email coincide con el de la persona (opcional)
        if (!request.getEmail().equalsIgnoreCase(person.getEmail())) {
            throw new IllegalArgumentException("El email no coincide con el de la persona.");
        }

        // 5. Cargar roles desde nombres
        Set<RoleEntity> roles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleName)))
                .peek(role -> {
                    if (role.getPermissions() == null || role.getPermissions().isEmpty()) {
                        throw new IllegalArgumentException("El rol '" + role.getName() + "' no tiene permisos asignados.");
                    }
                })
                .collect(Collectors.toSet());

        // 6. Crear usuario
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .roles(roles)
                .person(person)
                .activo(request.isActivo())
                .build();

        // 7. Guardar usuario
        userRepository.save(user);

        // 8. Generar token y retornar AuthResponse
        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token).build();
    }
  
    @Transactional
    public String resetPassword(ResetPasswordRequest requestDTO) {
    //    
        // 1. Buscar al usuario por su nombre de usuario
        UserEntity user = userRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 2. Validar la nueva contraseña (por ejemplo, longitud mínima de 8 caracteres)
        if (requestDTO.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }

        // 3. Cifrar la nueva contraseña
        String encodedPassword = passwordEncoder.encode(requestDTO.getNewPassword());

        // 4. Actualizar la contraseña del usuario
        user.setPassword(encodedPassword);

        // 5. Guardar el usuario con la nueva contraseña
        userRepository.save(user);

        return "Contraseña restablecida con éxito.";
    }
}