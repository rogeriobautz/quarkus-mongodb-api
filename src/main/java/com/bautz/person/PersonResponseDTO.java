package com.bautz.person;

import java.time.Instant;
import java.time.LocalDate;

import org.bson.types.ObjectId;

public record PersonResponseDTO (ObjectId id, String name, LocalDate birthDate, Status status, Instant lastUpdated) { }
