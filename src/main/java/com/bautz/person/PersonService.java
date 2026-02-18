package com.bautz.person;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bautz.person.dto.PersonRequestDTO;
import com.bautz.person.dto.PersonResponseDTO;
import com.bautz.person.repository.PersonRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class PersonService {

    @Inject
    PersonRepository personRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    public PersonResponseDTO persist(PersonRequestDTO requestDto) {
        validate(requestDto);
        Person persisted = personRepository.upsertByName(toEntity(requestDto));
        LOGGER.info("Person persisted {}", persisted);
        return toResponse(persisted);
    }

    private PersonResponseDTO toResponse(Person entity) {
        return new PersonResponseDTO(entity.getId(), entity.getName(), entity.getLastUpdated());
    }

    private void validate(PersonRequestDTO person) {
        if (person.name() == null || person.birthDate() == null || person.status() == null) {
            throw new BadRequestException("All fields are mandatory");
        }

        if(!List.of(Status.values()).contains(person.status())) {
            throw new BadRequestException("Invalid Status: " + person.status());
        }        
    }

    private Person toEntity(PersonRequestDTO personRequestDTO) {
        return new Person(personRequestDTO.name(), personRequestDTO.birthDate(), personRequestDTO.status());
    }

    /**
     * Changes the status of a person to DECEASED based on their name.
     * @param name
     * @return
     */
    public Person changeToDeceased(String name) {
        return personRepository.setStatus(name, Status.DECEASED);
    }

    /* Não funciona porque o panache não usa o CodecProvider customizados - como o para conversão de Enum em Ordinais */
    public long updateToDeceased(String name) {
        return personRepository.update("status", Status.DECEASED).where("name", name);
    }
}
