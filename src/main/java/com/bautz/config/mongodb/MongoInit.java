package com.bautz.config.mongodb;

import io.quarkus.runtime.StartupEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class MongoInit {

    @Inject
    MongoClient mongoClient;

    private final Logger LOGGER = LoggerFactory.getLogger(MongoInit.class.getName());

    /* O MongoDB é lazy, por isso este startup pra inicializar o cliente é necessário, inclusive pra
        verificar o health do banco de dadis
     */
    void onStart(@Observes StartupEvent ev) {
        // .into(new ArrayList<>()) converte o cursor preguiçoso em uma lista real
        String databases = mongoClient.listDatabaseNames()
                .into(new ArrayList<>())
                .stream()
                .collect(Collectors.joining(", "));

        LOGGER.info("MongoDB conectado! Databases: {}", databases);
    }
}