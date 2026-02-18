package com.bautz.person.dto;

import java.time.Instant;

import org.bson.types.ObjectId;

public record PersonResponseDTO (ObjectId id, String name, Instant lastUpdated) { }
