package es.jlrn.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.jlrn.presentation.dto.person.CreatePersonDTO;
import es.jlrn.presentation.dto.person.PersonDTO;
import es.jlrn.presentation.dto.person.UpdatePersonDTO;
import es.jlrn.services.exceptions.EntityNotFoundException;
import es.jlrn.services.interfaces.IPersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
//
    //private final IPersonService personService;
    private final IPersonService personService;

    //

    // Endpoint para obtener una persona por ID
    @GetMapping("/find/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        Optional<PersonDTO> personDTO = personService.getPersonId(id);
        return personDTO.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para obtener todas las personas
    @GetMapping("findAll")
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        List<PersonDTO> personDTOs = personService.getAllPersons();
        return ResponseEntity.ok(personDTOs);
    }

    // Endpoint para crear una persona
    @PostMapping("/create")
    public ResponseEntity<PersonDTO > createPerson(@Valid @RequestBody CreatePersonDTO createPersonDTO) {
        try {
            PersonDTO createdPerson = personService.crearPersona(createPersonDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para actualizar una persona
    @PutMapping("/update/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@Valid @RequestBody UpdatePersonDTO updatePersonDTO, @PathVariable Long id) {
        try {
            PersonDTO updatedPerson = personService.updatePersona(updatePersonDTO, id);
            return ResponseEntity.ok(updatedPerson);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para eliminar una persona
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        try {
            personService.deletePersona(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para actualizar solo el nombre
    @PatchMapping("/{id}/name")
    public ResponseEntity<PersonDTO> updateName(@PathVariable Long id, @RequestParam String name) {
        PersonDTO updatedPerson = personService.updateName(id, name);
        return ResponseEntity.ok(updatedPerson);
    }

    // Endpoint para actualizar solo los apellidos
    @PatchMapping("/{id}/surnames")
    public ResponseEntity<PersonDTO> updateSurnames(@PathVariable Long id, @RequestParam String surnames) {
        PersonDTO updatedPerson = personService.updateSurnames(id, surnames);
        return ResponseEntity.ok(updatedPerson);
    }

    // Endpoint para actualizar solo el correo electrónico
    @PatchMapping("/{id}/email")
    public ResponseEntity<PersonDTO> updateEmail(@PathVariable Long id, @RequestParam String email) {
        PersonDTO updatedPerson = personService.updateEmail(id, email);
        return ResponseEntity.ok(updatedPerson);
    }

    // Endpoint para actualizar solo la localidad
    @PatchMapping("/{id}/locality")
    public ResponseEntity<PersonDTO> updateLocality(@PathVariable Long id, @RequestParam String locality) {
        PersonDTO updatedPerson = personService.updateLocality(id, locality);
        return ResponseEntity.ok(updatedPerson);
    }

    // Endpoint para actualizar solo la provincia
    @PatchMapping("/{id}/province")
    public ResponseEntity<PersonDTO> updateProvince(@PathVariable Long id, @RequestParam String province) {
        PersonDTO updatedPerson = personService.updateProvince(id, province);
        return ResponseEntity.ok(updatedPerson);
    }

}
