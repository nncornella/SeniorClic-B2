package es.jlrn.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.jlrn.persistence.model.Person;
import es.jlrn.persistence.model.UserEntity;
import es.jlrn.persistence.repositories.PersonRepository;
import es.jlrn.persistence.repositories.UserEntityRepository;
import es.jlrn.presentation.dto.person.CreatePersonDTO;
import es.jlrn.presentation.dto.person.PersonDTO;
import es.jlrn.presentation.dto.person.UpdatePersonDTO;
import es.jlrn.services.exceptions.EntityNotFoundException;
import es.jlrn.services.interfaces.IPersonService;
import es.jlrn.util.mappers.DTOValidator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements IPersonService {
    //
    private final PersonRepository personRepository;
    private final UserEntityRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final DTOValidator dtoValidator; // Validar DTO

    private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    //

    @Transactional(readOnly = true)
    @Override
    public Optional<PersonDTO> getPersonId(Long id) {
        return personRepository.findById(id)
                .map(person -> modelMapper.map(person, PersonDTO.class))
                .filter(personDTO -> dtoValidator.isValid(personDTO));
    }

    @Transactional(readOnly = true)
    @Override
    public List<PersonDTO> getAllPersons() {
        //
        List<Person> people = personRepository.findAll();
        return people.stream()
                .map(person -> modelMapper.map(person, PersonDTO.class))
                .filter(personDTO -> dtoValidator.isValid(personDTO))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PersonDTO crearPersona(CreatePersonDTO dto) {
        //
        // Validar existencia por email
        if (personRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe una persona con ese correo electrónico");
        }

        // Validar el DTO antes de mapear
        if (!dtoValidator.isValid(dto)) {
            throw new IllegalArgumentException("El DTO proporcionado no es válido");
        }

        // Mapear DTO a entidad
        Person persona = modelMapper.map(dto, Person.class);

        // Guardar persona
        Person personSaved = personRepository.save(persona);

        // Mapear entidad a DTO y retornar
        return modelMapper.map(personSaved, PersonDTO.class);
    }

    @Transactional
    @Override
    public PersonDTO updatePersona(UpdatePersonDTO dto, Long id) {
    //
        // Buscar la persona por ID, lanzar excepción específica si no se encuentra
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con el ID: " + id));


                if (dto.getName() != null) {
                    existingPerson.setName(dto.getName());
                }
        
                if (dto.getSurnames() != null) {
                    existingPerson.setSurnames(dto.getSurnames());
                }
        
                if (dto.getEmail() != null) {
                    existingPerson.setEmail(dto.getEmail());
                }
        
                if (dto.getLocality() != null) {
                    existingPerson.setLocality(dto.getLocality());
                }
        
                if (dto.getProvince() != null) {
                    existingPerson.setProvince(dto.getProvince());
                }
        
                // if (dto.getStartDate() != null) {
                //     existingPerson.setStartDate(dto.getStartDate());
                // }
        
                existingPerson.setLastAccessDate(LocalDate.now());
                
        
                Person updatedPerson = personRepository.save(existingPerson);
                return modelMapper.map(updatedPerson, PersonDTO.class);
    }

    @Transactional
    @Override
    public void deletePersona(Long id) {
    //
        // Buscar la persona
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con el ID: " + id));

        // Buscar y eliminar el usuario que tiene esta persona (si existe)
        Optional<UserEntity> userOptional = userRepository.findByPerson(existingPerson);
        userOptional.ifPresent(userRepository::delete); // Elimina el usuario primero

        // Ahora se puede eliminar la persona
        personRepository.delete(existingPerson);

        log.info("Persona con ID: {} y su usuario asociado han sido eliminados exitosamente.", id);    
    }

    @Transactional
    @Override
    public PersonDTO updateName(Long id, String name) {
    //
        Person existingPerson = personRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con el ID: " + id));

        existingPerson.setName(name);
        existingPerson.setLastAccessDate(LocalDate.now());  // Actualización de la fecha de último acceso
        Person updatedPerson = personRepository.save(existingPerson);

        return modelMapper.map(updatedPerson, PersonDTO.class);
    }

    @Transactional
    @Override
    public PersonDTO updateSurnames(Long id, String surnames) {
    //
        Person existingPerson = personRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con el ID: " + id));

        existingPerson.setSurnames(surnames);
        existingPerson.setLastAccessDate(LocalDate.now());  // Actualización de la fecha de último acceso
        Person updatedPerson = personRepository.save(existingPerson);

        return modelMapper.map(updatedPerson, PersonDTO.class);    
    }

    @Transactional
    @Override
    public PersonDTO updateEmail(Long id, String email) {
    //
        Person existingPerson = personRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con el ID: " + id));

        existingPerson.setEmail(email);
        existingPerson.setLastAccessDate(LocalDate.now());  // Actualización de la fecha de último acceso
        Person updatedPerson = personRepository.save(existingPerson);

        return modelMapper.map(updatedPerson, PersonDTO.class);    
    }

    @Transactional
    @Override
    public PersonDTO updateLocality(Long id, String locality) {
    //
        Person existingPerson = personRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con el ID: " + id));

        existingPerson.setLocality(locality);
        existingPerson.setLastAccessDate(LocalDate.now());  // Actualización de la fecha de último acceso
        Person updatedPerson = personRepository.save(existingPerson);

        return modelMapper.map(updatedPerson, PersonDTO.class);    
    }

    @Transactional
    @Override
    public PersonDTO updateProvince(Long id, String province) {
    //
        Person existingPerson = personRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con el ID: " + id));

        existingPerson.setProvince(province);
        existingPerson.setLastAccessDate(LocalDate.now());  // Actualización de la fecha de último acceso
        Person updatedPerson = personRepository.save(existingPerson);

        return modelMapper.map(updatedPerson, PersonDTO.class);    
            }
   
}
