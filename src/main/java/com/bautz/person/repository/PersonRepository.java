package com.bautz.person.repository;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bautz.person.Person;
import com.bautz.person.Status;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for {@link Person} entities.
 * <p>
 * Uses MongoDB's {@code $set} operator for upsert operations, which allows
 * updating
 * specific fields without replacing the entire document.
 *
 * @see <a href=
 *      "https://www.mongodb.com/docs/manual/reference/operator/update/set/">MongoDB
 *      $set operator</a>
 */
@ApplicationScoped
public class PersonRepository implements PanacheMongoRepository<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepository.class);

    /**
     * Upserts a {@link Person} based on the {@code name} field.
     * <p>
     * If a person with the same name already exists, their data will be updated.
     * Otherwise, a new person will be inserted. The {@code lastUpdated} timestamp
     * is always set to the current time.
     *
     * @param person the person to upsert
     * @return the persisted person, including the generated {@code _id} and
     *         {@code lastUpdated}
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
     * Converts a {@link Person} to a BSON {@link Document} using the collection's
     * codec registry,
     * ensuring that {@code @BsonProperty} field name mappings are respected.
     * The {@code _id} field is removed to avoid overwriting it during updates.
     *
     * @param person the person to convert
     * @return a {@link Document} representing the person without the {@code _id}
     *         field
     */
    private Document toDocument(Person person) {

        // 1. Pega o registry de codecs que o MongoDB conhece
        // É aqui que estão registrados seu LocalDateCodec, OrdinalEnumCodec, etc.
        CodecRegistry registry = mongoCollection().getCodecRegistry();

        // 2. Pega o encoder específico para a classe Person dentro desse registry
        // É ele que sabe transformar um objeto Person em BSON
        Encoder<Person> encoder = registry.get(Person.class);

        // 3. JsonWriter é um "destino" para onde o encoder vai escrever
        // StringWriter é o buffer que vai acumular o resultado em texto
        StringWriter stringWriter = new StringWriter();

        // 4. Executa a serialização: Person → JSON string
        // O encoder usa o @BsonProperty e seus codecs customizados aqui
        // Resultado em stringWriter:
        // {"NAME":"João","birth":"1995-01-01","STATUS":1,...}
        encoder.encode(new JsonWriter(stringWriter), person, EncoderContext.builder().build());

        // 5. Converte a JSON string de volta para um Document (Map<String, Object>) do
        // MongoDB
        // Agora temos um Document com os nomes de campo corretos do MongoDB
        Document document = Document.parse(stringWriter.toString());

        // 6. Remove o _id para não sobrescrever o _id existente no banco durante um
        // update
        document.remove("_id");

        LOGGER.info("Converted Person to Document: {}", document.toJson());

        return document;
    }

    /**
     * Finds a {@link Person} by name.
     *
     * @param name the name to search for
     * @return the person, or {@code null} if not found
     */
    public Person findByName(String name) {
        return find("name", name).firstResult();
    }

    /**
     * Returns all people with {@link Status#LIVING} status.
     *
     * @return list of living people
     */
    public List<Person> findAlive() {
        return list(Person.MONGODB_STATUS, Status.LIVING);
    }

    /**
     * Sets the status of a person identified by name.
     *
     * @param name   the name of the person to update
     * @param status the new status to set
     * @return the updated person, or {@code null} if no person with the given name
     *         was found
     */
    public Person setStatus(String name, Status status) {
        Bson filter = Filters.eq(Person.MONGODB_NAME, name);
        Bson update = new Document("$set", new Document(Person.MONGODB_STATUS, status));

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER);

        return mongoCollection().findOneAndUpdate(filter, update, options);
    }
}