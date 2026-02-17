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

    public PersonRequestDTO persist(PersonRequestDTO personRequestDTO) {
        LOGGER.info("Persisting person: {}", personRequestDTO);
        validate(personRequestDTO);
        personRepository.persist(toEntity(personRequestDTO));
        return personRequestDTO;
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
