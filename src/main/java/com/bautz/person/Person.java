package com.bautz.person;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import com.bautz.util.StringUtil;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MongoEntity(collection = "person")
public class Person {

    public Person(String name, LocalDate birthDate, Status status) {
        this.name = StringUtil.normalizeName(name);
        this.birthDate = birthDate;
        this.status = status;
        this.lastUpdated = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }
    
    private ObjectId id;

    @EqualsAndHashCode.Include
    private String name;

    @BsonProperty("birth")
    @EqualsAndHashCode.Include
    private LocalDate birthDate;
    
    private Status status;

    private Instant lastUpdated;

    /* Setters - alguns customizados */

    
    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = StringUtil.normalizeName(name);
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLastUpdated(Instant instant) {
        this.lastUpdated = instant;
    }

    /* Getters */

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }   

}
