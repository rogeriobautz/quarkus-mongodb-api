/* 
    O Quarkus verifica por todos os beans que implementa CodecProvider por isso esta classe não é
    necessária se for este o único objetivo.
 */

// package com.bautz.config.mongodb;

// import org.bson.codecs.configuration.CodecRegistries;
// import org.bson.codecs.configuration.CodecRegistry;

// import com.mongodb.MongoClientSettings;

// import io.quarkus.mongodb.runtime.MongoClientCustomizer;
// import jakarta.enterprise.context.ApplicationScoped;

// @ApplicationScoped
// public class MongoCustomizer implements MongoClientCustomizer {

//     @Override
//     public MongoClientSettings.Builder customize(MongoClientSettings.Builder builder) {

//         CodecRegistry existingRegistry = builder.build().getCodecRegistry();

//         CodecRegistry customRegistry = CodecRegistries.fromRegistries(
//                 existingRegistry,
//                 CodecRegistries.fromProviders(new OrdinalEnumCodecProvider()));

//         builder.codecRegistry(customRegistry);
//         return builder;
//     }
// }
