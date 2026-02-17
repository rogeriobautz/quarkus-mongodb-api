package com.bautz.person;

import java.time.LocalDate;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import com.bautz.util.StringUtil;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MongoEntity(collection = "persons")
public class Person {

    public Person(String name, LocalDate birthDate, Status status) {
        this.name = StringUtil.normalizeName(name);
        this.birthDate = birthDate;
        this.status = status;
    }
    
    public ObjectId id;

    @EqualsAndHashCode.Include
    public String name;

    // will be persisted as a 'birth' field in MongoDB
    @BsonProperty("birth")
    @EqualsAndHashCode.Include
    public LocalDate birthDate;
    
    public Status status;

    public void setName(String name) {
        this.name = StringUtil.normalizeName(name);
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }     

}
