package com.bautz.config.mongodb;

import java.time.LocalDate;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class LocalDateCodec implements Codec<LocalDate> {

    @Override
    public void encode(BsonWriter writer, LocalDate value, EncoderContext encoderContext) {
        writer.writeString(value.toString()); // "2018-02-03"
    }

    @Override
    public LocalDate decode(BsonReader reader, DecoderContext decoderContext) {
        return LocalDate.parse(reader.readString());
    }

    @Override
    public Class<LocalDate> getEncoderClass() {
        return LocalDate.class;
    }
}