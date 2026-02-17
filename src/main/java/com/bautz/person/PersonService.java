package com.bautz.person;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class PersonService {

    @Inject
    PersonRepository personRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    public PersonResponseDTO persist(PersonRequestDTO requestDto) {
        LOGGER.info("Validating POST request: {}", requestDto);
        validate(requestDto);
        Person entity = toEntity(requestDto);
        LOGGER.info("Persisting person: {}", entity);
        personRepository.upsertByName(entity);
        return toResponse(entity);
    }

    private PersonResponseDTO toResponse(Person entity) {
        return new PersonResponseDTO(entity.getId(), entity.getName(), entity.getBirthDate(), entity.getStatus(), entity.getLastUpdated());
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
}
