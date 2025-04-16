package es.jlrn.services.interfaces;

import java.util.List;
import java.util.Optional;

import es.jlrn.presentation.dto.person.CreatePersonDTO;
import es.jlrn.presentation.dto.person.PersonDTO;
import es.jlrn.presentation.dto.person.UpdatePersonDTO;

public interface IPersonService {
//
    Optional<PersonDTO> getPersonId(Long id);
    List<PersonDTO> getAllPersons();
    PersonDTO crearPersona(CreatePersonDTO dto);
    PersonDTO updatePersona(UpdatePersonDTO dto, Long id);
    void deletePersona(Long id);
    // modificar los capos
    PersonDTO updateName(Long id, String name);
    PersonDTO updateSurnames(Long id, String surnames);
    PersonDTO updateEmail(Long id, String email);
    PersonDTO updateLocality(Long id, String locality);
    PersonDTO updateProvince(Long id, String province);
}
