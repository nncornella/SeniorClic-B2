package es.jlrn.configuration.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.jlrn.persistence.model.PermissionsEntity;
import es.jlrn.persistence.model.Person;
import es.jlrn.persistence.model.RoleEntity;
import es.jlrn.persistence.model.UserEntity;
import es.jlrn.persistence.repositories.PermissionsEntityRepository;
import es.jlrn.persistence.repositories.PersonRepository;
import es.jlrn.persistence.repositories.RoleEntityRepository;
import es.jlrn.persistence.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
//
    @Value("${custom.seed-data:false}")  // Valor por defecto en caso de que no esté configurado
    private boolean seedData;

    private final RoleEntityRepository roleRepository;
    private final PermissionsEntityRepository permissionsRepository;
    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository; // Repositorio de personas

    // Definir permisos y roles por defecto
    private static final List<String> DEFAULT_PERMISSIONS = List.of(
            "USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
            "POST_READ", "POST_CREATE", "POST_UPDATE", "POST_DELETE",
            "REPORT_VIEW", "REPORT_EXPORT", "ADMIN_PANEL_ACCESS"
    );

    @Override
    public void run(String... args) throws Exception {
        if (seedData) {
            log.info("Se ha habilitado la siembra de datos. Se están inicializando roles, permisos y usuario administrador.");
            initPermissions();
            initRoles();
            initAdminUser();
        } else {
            log.info("La siembra de datos está deshabilitada. Se omite la inicialización de datos.");
        }
    }

    // Inicializa los permisos en la base de datos si no existen
    private void initPermissions() {
        Set<String> existingPermissions = permissionsRepository.findAll()
                .stream()
                .map(PermissionsEntity::getName)
                .collect(Collectors.toSet());

        Set<PermissionsEntity> permissionsToSave = DEFAULT_PERMISSIONS.stream()
                .filter(permission -> !existingPermissions.contains(permission))
                .map(permission -> PermissionsEntity.builder().name(permission).build())
                .collect(Collectors.toSet());

        if (!permissionsToSave.isEmpty()) {
            permissionsRepository.saveAll(permissionsToSave);
            permissionsToSave.forEach(p -> log.info("Creado permiso: {}", p.getName()));
        } else {
            log.info("No hay nuevos permisos para crear.");
        }

        log.info("Inicialización de permisos completada.");
    }

    // Inicializa los roles con sus permisos
    private void initRoles() {
        Map<String, Set<String>> rolePermissionsMap = new LinkedHashMap<>();
        rolePermissionsMap.put("ROLE_SUPER", Set.of(
                "USER_READ", "USER_WRITE", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
                "POST_READ", "POST_CREATE", "POST_UPDATE", "POST_DELETE",
                "REPORT_VIEW", "REPORT_EXPORT", "ADMIN_PANEL_ACCESS"));
        rolePermissionsMap.put("ROLE_ADMIN", Set.of(
                "USER_READ", "USER_WRITE", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
                "POST_READ", "POST_CREATE", "POST_UPDATE", "POST_DELETE",
                "REPORT_VIEW", "REPORT_EXPORT", "ADMIN_PANEL_ACCESS"));
        rolePermissionsMap.put("ROLE_USER", Set.of("USER_READ"));
        rolePermissionsMap.put("ROLE_DEVELOPER", Set.of("USER_READ", "USER_WRITE", "USER_CREATE", "USER_UPDATE"));
        rolePermissionsMap.put("ROLE_INVITED", Set.of("USER_READ"));

        Map<String, PermissionsEntity> permissionMap = permissionsRepository.findAll()
                .stream()
                .collect(Collectors.toMap(PermissionsEntity::getName, p -> p));

        Map<String, RoleEntity> existingRoles = roleRepository.findAll()
                .stream()
                .collect(Collectors.toMap(RoleEntity::getName, r -> r));

        List<RoleEntity> rolesToSave = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : rolePermissionsMap.entrySet()) {
            String roleName = entry.getKey();
            if (!existingRoles.containsKey(roleName)) {
                Set<PermissionsEntity> permissions = entry.getValue().stream()
                        .map(permissionMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                RoleEntity role = RoleEntity.builder()
                        .name(roleName)
                        .permissions(permissions)
                        .build();

                rolesToSave.add(role);
                log.info("Preparado rol: {} con permisos {}", roleName, permissions.stream().map(PermissionsEntity::getName).collect(Collectors.toList()));
            } else {
                log.info("El rol {} ya existe, omitiendo creación.", roleName);
            }
        }

        if (!rolesToSave.isEmpty()) {
            roleRepository.saveAll(rolesToSave);
            log.info("Roles guardados.");
        } else {
            log.info("No hay nuevos roles para crear.");
        }

        log.info("Inicialización de roles con permisos completada.");
    }

    // Crea el usuario administrador si no existe
    private void initAdminUser() {
        String adminUsername = "pnry";
        String rawPassword = "123456"; // Cambiar por una contraseña más segura en producción

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            RoleEntity adminRole = roleRepository.findByName("ROLE_SUPER")
                    .orElseThrow(() -> new RuntimeException("ROLE_SUPER no encontrado"));

            UserEntity adminUser = UserEntity.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(rawPassword))
                    .activo(true)
                    .roles(Set.of(adminRole))
                    .build();

            Person adminPerson = Person.builder()
                    .name("nino")
                    .surnames("Garci Mor")
                    .email("ni@sistema.com")
                    .locality("Cornella")
                    .province("Barcelona")
                    .startDate(LocalDate.now())
                    .lastAccessDate(LocalDate.now())
                    .build();

            adminPerson = personRepository.save(adminPerson);

            adminUser.setEmail(adminPerson.getEmail()); // asigna al usuari el email de la persona
            adminUser.setPerson(adminPerson);

            userRepository.save(adminUser);

            log.info("Usuario administrador creado: {}", adminUsername);
        } else {
            log.info("El usuario administrador ya existe.");
        }
    }
}


// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class DataInitializer implements CommandLineRunner{
// //
//     @Value("${custom.seed-data:false}")  // Valor por defecto en caso de que no esté configurado
//     private boolean seedData;

//     private final RoleEntityRepository roleRepository;
//     private final PermissionsEntityRepository permissionsRepository;
//     private final UserEntityRepository userRepository;
//     private final PasswordEncoder passwordEncoder;
//     private final PersonRepository personRepository; // Añadimos el repositorio de personas

//     // Definir permisos y roles por defecto
//     private static final List<String> DEFAULT_PERMISSIONS = List.of( "USER_READ",
//                                                                                  "USER_CREATE",
//                                                                                  "USER_UPDATE",
//                                                                                  "USER_DELETE",
//                                                                                  "POST_READ",
//                                                                                  "POST_CREATE",
//                                                                                  "POST_UPDATE",
//                                                                                  "POST_DELETE",
//                                                                                  "REPORT_VIEW",
//                                                                                  "REPORT_EXPORT",
//                                                                                  "ADMIN_PANEL_ACCESS");

//     // private static final Map<String, List<String>> ROLE_PERMISSIONS_MAP = Map.of(
//     //         "ROLE_SUPER", List.of("READ", "WRITE", "DELETE", "UPDATE", "CREATE"),
//     //         "ROLE_ADMIN", List.of("READ", "WRITE", "DELETE", "UPDATE", "CREATE"),
//     //         "ROLE_USER", List.of("READ"),
//     //         "ROLE_DEVELOPER", List.of("READ", "WRITE"),
//     //         "ROLE_INVITED", List.of("READ")
//     // );






//    //private final RoleEntityRepository roleRepository;
//    // private final PermissionsEntityRepository permissionsRepository;

//     // @Override
//     // public void run(String... args) throws Exception {
//     //     initPermissions();
//     //     initRoles();
//     // }

//     @Override
//     public void run(String... args) throws Exception {
//         if (seedData) {
//             log.info("Se ha habilitado la siembra de datos. Se están inicializando roles, permisos y usuario administrador.");
//             initPermissions();
//             initRoles();
//             initAdminUser();
//         } else {
//             log.info("La siembra de datos está deshabilitada. Se omite la inicialización de datos".);
//         }
//     }

//     private void initPermissions() {
//         Set<String> existingPermissions = permissionsRepository.findAll()
//                 .stream()
//                 .map(PermissionsEntity::getName)
//                 .collect(Collectors.toSet());

//         Set<PermissionsEntity> permissionsToSave = DEFAULT_PERMISSIONS.stream()
//                 .filter(permission -> !existingPermissions.contains(permission))
//                 .map(permission -> PermissionsEntity.builder().name(permission).build())
//                 .collect(Collectors.toSet());

//         if (!permissionsToSave.isEmpty()) {
//             permissionsRepository.saveAll(permissionsToSave);
//             permissionsToSave.forEach(p -> log.info("Permiso de creación: {}", p.getName()));
//         } else {
//             log.info("No hay nuevos permisos para crear.");
//         }

//         log.info("Inicialización de permisos completada.");
//     }

//     // private void initPermissions() {
//     //     List<String> defaultPermissions = List.of("CREATE", "UPDATE", "DELETE", "READ", "WRITE", "EXECUTE");
//     //     Set<String> existingPermissions = permissionsRepository.findAll()
//     //             .stream().map(PermissionsEntity::getName).collect(Collectors.toSet());

//     //     List<PermissionsEntity> toSave = defaultPermissions.stream()
//     //             .filter(p -> !existingPermissions.contains(p))
//     //             .map(p -> PermissionsEntity.builder().name(p).build())
//     //             .toList();

//     //     if (!toSave.isEmpty()) {
//     //         permissionsRepository.saveAll(toSave);
//     //         toSave.forEach(p -> log.info("Created permission: {}", p.getName()));
//     //     }
//     //     log.info("Permissions initialization completed.");
//     // }


//     private void initRoles() {
//     //    
//         // Mapa de roles con los permisos correspondientes
//         Map<String, Set<String>> rolePermissionsMap = new LinkedHashMap<>();
//         rolePermissionsMap.put("ROLE_SUPER", Set.of(
//                 "USER_READ", "USER_WRITE", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
//                 "POST_READ", "POST_CREATE", "POST_UPDATE", "POST_DELETE",
//                 "REPORT_VIEW", "REPORT_EXPORT", "ADMIN_PANEL_ACCESS"));
    
//         rolePermissionsMap.put("ROLE_ADMIN", Set.of(
//                 "USER_READ", "USER_WRITE", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
//                 "POST_READ", "POST_CREATE", "POST_UPDATE", "POST_DELETE",
//                 "REPORT_VIEW", "REPORT_EXPORT", "ADMIN_PANEL_ACCESS"));
    
//         rolePermissionsMap.put("ROLE_USER", Set.of("USER_READ"));
    
//         rolePermissionsMap.put("ROLE_DEVELOPER", Set.of(
//                 "USER_READ", "USER_WRITE", "USER_CREATE", "USER_UPDATE"));
    
//         rolePermissionsMap.put("ROLE_INVITED", Set.of("USER_READ"));
    
//         // Mapa de permisos existentes en la base de datos
//         Map<String, PermissionsEntity> permissionMap = permissionsRepository.findAll()
//                 .stream()
//                 .collect(Collectors.toMap(PermissionsEntity::getName, p -> p));
    
//         // Obtener los roles existentes para evitar duplicados
//         Map<String, RoleEntity> existingRoles = roleRepository.findAll()
//                 .stream()
//                 .collect(Collectors.toMap(RoleEntity::getName, r -> r));
    
//         List<RoleEntity> rolesToSave = new ArrayList<>();
    
//         // Iterar sobre el mapa de roles y permisos para crear nuevos roles
//         for (Map.Entry<String, Set<String>> entry : rolePermissionsMap.entrySet()) {
//             String roleName = entry.getKey();
    
//             // Verificar si el rol ya existe
//             if (!existingRoles.containsKey(roleName)) {
//                 // Obtener permisos asociados al rol
//                 Set<PermissionsEntity> permissions = entry.getValue().stream()
//                         .map(permissionMap::get)
//                         .filter(Objects::nonNull) // Asegurarse de que el permiso existe
//                         .collect(Collectors.toSet());
    
//                 // Crear el rol con los permisos asociados
//                 RoleEntity role = RoleEntity.builder()
//                         .name(roleName)
//                         .permissions(permissions)
//                         .build();
    
//                 rolesToSave.add(role);
//                 log.info("Prepared role: {} with permissions {}", roleName, permissions.stream().map(PermissionsEntity::getName).collect(Collectors.toList()));
//             } else {
//                 log.info("Role {} already exists, skipping creation.", roleName);
//             }
//         }
    
//         // Guardar los roles si hay alguno nuevo para agregar
//         if (!rolesToSave.isEmpty()) {
//             roleRepository.saveAll(rolesToSave);
//             log.info("Roles saved.");
//         } else {
//             log.info("No new roles to create.");
//         }
    
//         log.info("Roles initialization with permissions completed.");
//     }
    



//     // private void initRoles() {
//     //     Map<String, List<String>> rolePermissionsMap = new LinkedHashMap<>();
//     //     rolePermissionsMap.put("ROLE_SUPER", List.of(
//     //                                                     "USER_READ",
//     //                                                     "USER_WRITE",
//     //                                                     "USER_CREATE",
//     //                                                     "USER_UPDATE",
//     //                                                     "USER_DELETE",
//     //                                                     "POST_READ",
//     //                                                     "POST_CREATE",
//     //                                                     "POST_UPDATE",
//     //                                                     "POST_DELETE",
//     //                                                     "REPORT_VIEW",
//     //                                                     "REPORT_EXPORT",
//     //                                                     "ADMIN_PANEL_ACCESS"));

//     //     rolePermissionsMap.put("ROLE_ADMIN", List.of(
//     //                                                     "USER_READ",
//     //                                                     "USER_WRITE",
//     //                                                     "USER_CREATE",
//     //                                                     "USER_UPDATE",
//     //                                                     "USER_DELETE",
//     //                                                     "POST_READ",
//     //                                                     "POST_CREATE",
//     //                                                     "POST_UPDATE",
//     //                                                     "POST_DELETE",
//     //                                                     "REPORT_VIEW",
//     //                                                     "REPORT_EXPORT",
//     //                                                     "ADMIN_PANEL_ACCESS"));

//     //     rolePermissionsMap.put("ROLE_USER", List.of("USER_READ"));

//     //     rolePermissionsMap.put("ROLE_DEVELOPER", List.of(
//     //                                                     "USER_READ",
//     //                                                     "USER_WRITE",
//     //                                                     "USER_CREATE",
//     //                                                     "USER_UPDATE"));


//     //     rolePermissionsMap.put("ROLE_INVITED", List.of("USER_READ"));


//     //     Map<String, PermissionsEntity> permissionMap = permissionsRepository.findAll()
//     //             .stream().collect(Collectors.toMap(PermissionsEntity::getName, p -> p));

//     //     Map<String, RoleEntity> existingRoles = roleRepository.findAll()
//     //             .stream().collect(Collectors.toMap(RoleEntity::getName, r -> r));

//     //     List<RoleEntity> rolesToSave = new ArrayList<>();

//     //     for (Map.Entry<String, List<String>> entry : rolePermissionsMap.entrySet()) {
//     //         String name = entry.getKey();
//     //         if (!existingRoles.containsKey(name)) {
//     //             Set<PermissionsEntity> permissions = entry.getValue().stream()
//     //                     .map(permissionMap::get)
//     //                     .filter(Objects::nonNull)
//     //                     .collect(Collectors.toCollection(LinkedHashSet::new));
//     //             rolesToSave.add(RoleEntity.builder().name(name).permissions(permissions).build());
//     //             log.info("Prepared role: {} with permissions {}", name, permissions.stream().map(PermissionsEntity::getName).toList());
//     //         }
//     //     }

//     //     if (!rolesToSave.isEmpty()) {
//     //         roleRepository.saveAll(rolesToSave);
//     //         log.info("Roles saved.");
//     //     }

//     //     log.info("Roles initialization with permissions completed.");
//     // }

//      private void initAdminUser() {
//         String adminUsername = "admin";
//         String rawPassword = "admin123"; // Cambiar por una contraseña más segura en producción

//         // Verificamos si el usuario admin ya existe
//         if (userRepository.findByUsername(adminUsername).isEmpty()) {
//             // Creamos el rol ROLE_ADMIN si no existe
//             RoleEntity adminRole = roleRepository.findByName("ROLE_SUPER")
//                     .orElseThrow(() -> new RuntimeException("ROLE_SUPER not found"));

//             // Creamos el usuario admin
//             UserEntity adminUser = UserEntity.builder()
//                     .username(adminUsername)
//                     .password(passwordEncoder.encode(rawPassword)) // Encriptamos la contraseña
//                     .activo(true)
//                     .roles(Set.of(adminRole))  // Le asignamos el rol de admin
//                     .build();

//             // Creamos la persona asociada al admin
//             Person adminPerson = Person.builder()
//                     .name("Admin")
//                     .surnames("System")
//                     .email("admin@sistema.com")
//                     .locality("Admin Town")
//                     .province("Admin Province")
//                     .startDate(LocalDate.now()) // Fecha de inicio
//                     .lastAccessDate(LocalDate.now()) // Fecha de último acceso
//                     .build();

//             adminPerson = personRepository.save(adminPerson); // Guardamos la persona

//             adminUser.setPerson(adminPerson); // Asociamos la persona al usuario admin

//             // Guardamos el usuario admin
//             userRepository.save(adminUser);

//             log.info("Created admin user: {}", adminUsername);
//         } else {
//             log.info("Admin user already exists.");
//         }
//     }


// }
