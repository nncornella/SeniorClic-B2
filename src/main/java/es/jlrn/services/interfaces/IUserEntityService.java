package es.jlrn.services.interfaces;

import java.util.List;
import java.util.Optional;

import es.jlrn.presentation.dto.user.CreateUserDTO;
import es.jlrn.presentation.dto.user.InactiveUserDTO;
import es.jlrn.presentation.dto.user.RegisterUserDTO;
import es.jlrn.presentation.dto.user.UpdateUserDTO;
import es.jlrn.presentation.dto.user.UserEntityDTO;

public interface IUserEntityService {
//
    Optional<UserEntityDTO> getUserId(Long id);
    Optional<UserEntityDTO> getUserName(String username);
    List<UserEntityDTO> getActiveUsers();  // lista de usuarios activos
    List<InactiveUserDTO> getInactiveUsers(); // lista de usuarios inactivos
    void changeUsername(Long id, String newUsername); //cambia el nombre de usuario
    void changePassword(Long id, String newPassword); // Cambia el password
    void changeUserRole(Long userId, String newRoleName); // Cambia el rol con sus permissions
    void addUserRole(Long userId, String newRoleName); // añade un nuevo rol mas
    void removeUserRole(Long userId, String roleName); // elimina un rol
    UserEntityDTO registerRoleUser(RegisterUserDTO dto);
    UserEntityDTO createUser(CreateUserDTO dto);
    UserEntityDTO updateUser(CreateUserDTO dto, Long id);
    UserEntityDTO updateUserFields(Long userId, UpdateUserDTO updateDTO);
    void reactivateUser(Long id);
    void desactivateUser(Long id);
    void deleteUser(Long id);
    void logicDeleteUser(Long id);
    
}
