package com.bautz.person;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class PersonRepository implements PanacheMongoRepository<Person> {

    private final Logger LOGGER = LoggerFactory.getLogger(PersonRepository.class.getName());

    // public void upsertByName(Person person) {
    //     // Usamos o engine de update do Panache com 'upsert'
    //     // Isso é uma única operação no banco (Atomic Upsert)
    //     update("name", person.getName(), "lastUpdated", person.getLastUpdated())
    //         .where("name", person.getName())
    //         .withUpsert()
    //         .execute();
    // }

    public void upsertByName(Person person) {
        Person existing = find("name", person.getName()).firstResult();

        if (existing != null) {
            LOGGER.debug("Person com nome {} já existe - Atualizando o registro.", person.getName());
            person.setId(existing.getId());
            update(person); 
        } else {
            LOGGER.debug("Person com nome {} não existe - Criando novo registro.", person.getName());
            persist(person);
        }
    }

    public Person findByName(String name) {
        return find("name", name).firstResult();
    }

    public List<Person> findAlive() {
        return list("status", Status.LIVING);
    }

    public void deleteLoics() {
        delete("name", "Loïc");
    }
}
