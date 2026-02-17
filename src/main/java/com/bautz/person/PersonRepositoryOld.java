package com.bautz.person;

import com.bautz.util.StringUtil;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class PersonRepositoryOld implements PanacheMongoRepository<Person> {

    private final Logger LOGGER = LoggerFactory.getLogger(PersonRepositoryOld.class.getName());

    public void upsertByName(Person person) {
        Bson filter = Filters.eq("name", StringUtil.normalizeName(person.getName()));

        Bson update = Updates.combine(
            Updates.set("name", StringUtil.normalizeName(person.getName())),
            Updates.set("birth", person.getBirthDate()),
            Updates.set("status", person.getStatus()),
            Updates.set("lastUpdated", person.getLastUpdated())
        );

        UpdateResult result = mongoCollection().updateOne(filter, update, new UpdateOptions().upsert(true));
        LOGGER.debug("Update result: {}", result);
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