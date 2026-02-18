package com.bautz.config.mongodb;

import java.time.LocalDate;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import com.bautz.config.mongodb.codec.LocalDateCodec;
import com.bautz.config.mongodb.codec.OrdinalEnumCodec;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {

        if (clazz.isEnum() && clazz.isAnnotationPresent(OrdinalEnum.class)) {
            return (Codec<T>) new OrdinalEnumCodec(clazz);
        }
        
        if (clazz == LocalDate.class) {
            return (Codec<T>) new LocalDateCodec();
        }
        
        return null;

    }
}
