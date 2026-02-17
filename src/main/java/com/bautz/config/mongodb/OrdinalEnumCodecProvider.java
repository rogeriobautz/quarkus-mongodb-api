package com.bautz.config.mongodb;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrdinalEnumCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {

        if (!clazz.isEnum() || !clazz.isAnnotationPresent(OrdinalEnum.class)) {
            return null;
        }

        return (Codec<T>) new OrdinalEnumCodec(clazz);
    }
}
