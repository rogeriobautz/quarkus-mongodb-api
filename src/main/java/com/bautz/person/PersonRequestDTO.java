package com.bautz.person;

import java.time.LocalDate;

public record PersonRequestDTO (String name, LocalDate birthDate, Status status) { }
