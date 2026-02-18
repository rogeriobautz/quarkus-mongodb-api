package com.bautz.person.dto;

import java.time.LocalDate;

import com.bautz.person.Status;

public record PersonRequestDTO (String name, LocalDate birthDate, Status status) { }
