package es.jlrn.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import es.jlrn.persistence.model.Person;
import es.jlrn.persistence.model.RoleEntity;
import es.jlrn.persistence.model.UserEntity;
import es.jlrn.persistence.repositories.PersonRepository;
import es.jlrn.persistence.repositories.RoleEntityRepository;
import es.jlrn.persistence.repositories.UserEntityRepository;
import es.jlrn.presentation.dto.user.CreateUserDTO;
import es.jlrn.presentation.dto.user.InactiveUserDTO;
import es.jlrn.presentation.dto.user.RegisterUserDTO;
import es.jlrn.presentation.dto.user.UpdateUserDTO;
import es.jlrn.presentation.dto.user.UserEntityDTO;
import es.jlrn.services.interfaces.IUserEntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional // 👈 todos los métodos son transaccionales por defecto
public class UserEntityServiceImpl implements IUserEntityService {
//
    private final PersonRepository personRepository; // Cambia esto por tu repositorio de UserEntity
    private final UserEntityRepository userRepository;
    private final RoleEntityRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper(); // Initialize ModelMapper

    @Transactional(readOnly = true)
    @Override
    public Optional<UserEntityDTO> getUserId(Long id) {
        //
        // Buscar el usuario por ID
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Mapear el usuario a UserEntityDTO (usando ModelMapper o manualmente)
        UserEntityDTO userDTO = modelMapper.map(user, UserEntityDTO.class);

        return Optional.ofNullable(userDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserEntityDTO> getUserName(String username) {
        // Buscar el usuario por username
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Mapear el usuario a UserEntityDTO (usando ModelMapper o manualmente)
        UserEntityDTO userDTO = modelMapper.map(user, UserEntityDTO.class);

        return Optional.ofNullable(userDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserEntityDTO> getActiveUsers() {
        return userRepository.findAllByActivoTrue()
                .stream()
                .map(user -> modelMapper.map(user, UserEntityDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<InactiveUserDTO> getInactiveUsers() {
        return userRepository.findAllByActivoFalse()
                .stream()
                .map(this::mapToInactiveDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserEntityDTO registerRoleUser(RegisterUserDTO dto) {
    //
        // Comprobar si el nombre de usuario ya está en uso
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        // Comprobar si existe la persona
        Person person = personRepository.findById(dto.getPersonID())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada"));

        // Obtener el rol por defecto ROLE_USER
        RoleEntity defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Rol por defecto 'ROLE_USER' no encontrado"));

        // Validar que el rol tiene permisos asignados
        if (defaultRole.getPermissions() == null || defaultRole.getPermissions().isEmpty()) {
            throw new IllegalArgumentException("El rol por defecto debe tener permisos asignados.");
        }

        // Crear el usuario con el rol por defecto
        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // Aquí deberías cifrar la contraseña
                .person(person)
                .roles(Set.of(defaultRole))
                .activo(dto.isActivo())
                .build();

        UserEntity saved = userRepository.save(user);

        // Convertir a DTO y devolver
        return modelMapper.map(saved, UserEntityDTO.class);
    }

    @Override
    public UserEntityDTO createUser(CreateUserDTO dto) {
    //
        // comprueba si existe el usuario
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        // comprueba si existe la persona
        Person person = personRepository.findById(dto.getPersonID())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada"));

        // comprueba los roles
        RoleEntity role = roleRepository.findByNameIn(Set.of(dto.getRoleName()))
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        // comprueba los permisos
        if (role.getPermissions() == null || role.getPermissions().isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener permisos asignados.");
        }

        // ✅ Mejor usar un constructor manual para tener control total
        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .password(dto.getPassword()) // 👈 Aquí cifras la contraseña
                .email(person.getEmail())
                .person(person)
                .roles(Set.of(role))
                .activo(dto.isActivo())
                .build();

        UserEntity saved = userRepository.save(user);

        // Map back to DTO
        return modelMapper.map(saved, UserEntityDTO.class);
    }

    @Override
    public UserEntityDTO updateUser(CreateUserDTO dto, Long id) {
        //
        // Buscar si existe el Usuario
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar si el nombre de usuario ya está en uso (solo si el nombre de
        // usuario cambia)
        if (!existingUser.getUsername().equals(dto.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        // Busca la Person
        Person person = personRepository.findById(dto.getPersonID())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada"));

        // Recoge los Roles
        RoleEntity role = roleRepository.findByName(dto.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        // Comprobar los permissions
        if (role.getPermissions() == null || role.getPermissions().isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener permisos asignados.");
        }

        // Actualizamos los datos del Usuario
        existingUser.setUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        existingUser.setPerson(person);
        // se usa HashSet para que la coleccion no se immutable como lo es con solo
        // Set.of()
        existingUser.setRoles(new HashSet<>(Set.of(role))); // ✅ Mutable
        existingUser.setActivo(dto.isActivo());

        // 6. Guardar y retornar DTO
        UserEntity updated = userRepository.save(existingUser);
        return modelMapper.map(updated, UserEntityDTO.class);
    }

    @Override
    public UserEntityDTO updateUserFields(Long userId, UpdateUserDTO updateDTO) {
        //
        // Buscar si existe el Usuario
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Si se proporciona un nuevo username, lo actualizamos
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().equals(existingUser.getUsername())) {
            // Verificar si el nuevo username ya está en uso
            if (userRepository.existsByUsername(updateDTO.getUsername())) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
            }
            existingUser.setUsername(updateDTO.getUsername()); // Actualizar el username
        }

        // Evitar la actualización de la persona, si se intenta actualizar, lanzamos una
        // excepción
        if (updateDTO.getPersonID() != null && !updateDTO.getPersonID().equals(existingUser.getPerson().getId())) {
            throw new IllegalArgumentException("La persona asociada al usuario no se puede actualizar.");
        }

        // Si se proporciona una nueva contraseña, la actualizamos
        if (updateDTO.getPassword() != null) {
            if (!passwordEncoder.matches(updateDTO.getPassword(), existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(updateDTO.getPassword())); // Solo se actualiza si
                                                                                           // cambia
            }
        }

        // Si se proporciona un nuevo estado de 'enabled', lo actualizamos
        if (updateDTO.getActivo() != null) {
            existingUser.setActivo(updateDTO.getActivo());
        }

        // Si se proporciona un nuevo rol, lo actualizamos
        if (updateDTO.getRoleName() != null) {
            RoleEntity role = roleRepository.findByName(updateDTO.getRoleName())
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

            // Comprobar los permisos del rol
            if (role.getPermissions() == null || role.getPermissions().isEmpty()) {
                throw new IllegalArgumentException("El rol debe tener permisos asignados.");
            }

            // Actualiza los roles con un conjunto mutable
            existingUser.setRoles(new HashSet<>(Set.of(role)));
        }

        // Guardar el usuario actualizado
        UserEntity updatedUser = userRepository.save(existingUser);

        // Mapear a DTO de respuesta y devolver
        return modelMapper.map(updatedUser, UserEntityDTO.class);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        userRepository.delete(user);
    }

    @Override
    public void logicDeleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!user.isEnabled()) {
            throw new IllegalStateException("El usuario ya está deshabilitado.");
        }

        user.setActivo(false);
        userRepository.save(user);
    }

    @Override
    public void reactivateUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (user.isActivo()) {
            throw new IllegalStateException("El usuario ya está activo.");
        }

        user.setActivo(true);
        userRepository.save(user);
    }

    @Override
    public void desactivateUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!user.isActivo()) {
            throw new IllegalStateException("El usuario ya está desactivado.");
        }

        user.setActivo(false);
        userRepository.save(user);
    }

    @Override
    public void changeUsername(Long id, String newUsername) {
        // Buscar el usuario
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar si el nuevo username ya está en uso
        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        // Cambiar el username
        user.setUsername(newUsername);

        // Guardar el usuario con el nuevo username
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        // Buscar el usuario
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Encriptar la nueva contraseña
        String encodedPassword = passwordEncoder.encode(newPassword);

        // Cambiar la contraseña
        user.setPassword(encodedPassword);

        // Guardar el usuario con la nueva contraseña
        userRepository.save(user);
    }

    @Override
    public void addUserRole(Long userId, String newRoleName) {
    //
        // Buscar el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        // Buscar el nuevo rol
        RoleEntity newRole = roleRepository.findByName(newRoleName)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + newRoleName));

        // Validar que el rol tenga permisos
        if (newRole.getPermissions() == null || newRole.getPermissions().isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso asignado.");
        }

        // Verificar si el usuario ya tiene este rol
        boolean yaTieneRol = user.getRoles().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase(newRoleName));

        if (yaTieneRol) {
            throw new IllegalStateException("El usuario ya tiene asignado ese rol.");
        }

        // Agregar el nuevo rol sin eliminar los anteriores
        user.getRoles().add(newRole);

        // Guardar cambios
        userRepository.save(user);

    }

    @Override
    public void changeUserRole(Long userId, String newRoleName) {
        // Buscar el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Buscar el nuevo rol
        RoleEntity newRole = roleRepository.findByName(newRoleName)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        // Verificar que el nuevo rol tenga permisos
        if (newRole.getPermissions() == null || newRole.getPermissions().isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener permisos asignados.");
        }

        // Verificar si el usuario ya tiene este rol
        boolean yaTieneRol = user.getRoles().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase(newRoleName));

        if (yaTieneRol) {
            throw new IllegalStateException("El usuario ya tiene asignado ese rol.");
        }

        // Cambiar el rol del usuario
        user.setRoles(new HashSet<>(Set.of(newRole))); // Usamos HashSet para asegurar la mutabilidad

        // Guardar el usuario con el nuevo rol
        userRepository.save(user);
    }

    @Override
    public void removeUserRole(Long userId, String roleName) {
        //
        // Buscar al usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        // Verificar si el usuario tiene más de un rol
        if (user.getRoles().size() <= 1) {
            throw new IllegalStateException("El usuario debe tener al menos un rol.");
        }

        // Buscar el rol que se quiere eliminar
        RoleEntity roleToRemove = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleName));

        // Verificar si el usuario tiene ese rol
        if (!user.getRoles().contains(roleToRemove)) {
            throw new IllegalStateException("El usuario no tiene este rol asignado.");
        }

        // Eliminar el rol del conjunto de roles del usuario
        user.getRoles().remove(roleToRemove);

        // Guardar los cambios en la base de datos
        userRepository.save(user);
    }

   //
   
    private InactiveUserDTO mapToInactiveDTO(UserEntity user) {
        InactiveUserDTO dto = new InactiveUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPersonName(user.getPerson().getName()); // Ajusta según tu modelo de persona
        return dto;
    }

}
