package com.bautz.person;

import java.io.StringWriter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriter;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for {@link Person} entities.
 * <p>
 * Uses MongoDB's {@code $set} operator for upsert operations, which allows updating
 * specific fields without replacing the entire document.
 *
 * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/update/set/">MongoDB $set operator</a>
 */
@ApplicationScoped
public class PersonRepository implements PanacheMongoRepository<Person> {

    /**
     * Upserts a {@link Person} based on the {@code name} field.
     * <p>
     * If a person with the same name already exists, their data will be updated.
     * Otherwise, a new person will be inserted. The {@code lastUpdated} timestamp
     * is always set to the current time.
     *
     * @param person the person to upsert
     * @return the persisted person, including the generated {@code _id} and {@code lastUpdated}
     */
    public Person upsertByName(Person person) {
        person.setLastUpdated(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        Bson filter = Filters.eq(Person.MONGODB_NAME, person.getName());
        Bson update = new Document("$set", toDocument(person));

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .upsert(true)
                .returnDocument(ReturnDocument.AFTER);

        return mongoCollection().findOneAndUpdate(filter, update, options);
    }

    /**
     * Converts a {@link Person} to a BSON {@link Document} using the collection's codec registry,
     * ensuring that {@code @BsonProperty} field name mappings are respected.
     * The {@code _id} field is removed to avoid overwriting it during updates.
     *
     * @param person the person to convert
     * @return a {@link Document} representing the person without the {@code _id} field
     */
    private Document toDocument(Person person) {
        CodecRegistry registry = mongoCollection().getCodecRegistry();
        Encoder<Person> encoder = registry.get(Person.class);

        StringWriter stringWriter = new StringWriter();
        encoder.encode(new JsonWriter(stringWriter), person, EncoderContext.builder().build());

        Document document = Document.parse(stringWriter.toString());
        document.remove("_id");
        return document;
    }

    /**
     * Finds a {@link Person} by name.
     *
     * @param name the name to search for
     * @return the person, or {@code null} if not found
     */
    public Person findByName(String name) {
        return find(Person.MONGODB_NAME, name).firstResult();
    }

    /**
     * Returns all people with {@link Status#LIVING} status.
     *
     * @return list of living people
     */
    public List<Person> findAlive() {
        return list(Person.MONGODB_STATUS, Status.LIVING);
    }
}