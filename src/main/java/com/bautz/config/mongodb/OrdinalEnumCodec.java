package com.bautz.config.mongodb;

import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class OrdinalEnumCodec<T extends Enum<T>> implements Codec<T> {

    private final Class<T> enumClass;

    public OrdinalEnumCodec(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        if (value == null) {
            writer.writeNull();
        } else {
            writer.writeInt32(value.ordinal());
        }
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        if (reader.getCurrentBsonType() == BsonType.NULL) {
            reader.readNull();
            return null;
        }

        int ordinal = reader.readInt32();
        T[] values = enumClass.getEnumConstants();

        if (ordinal < 0 || ordinal >= values.length) {
            throw new IllegalArgumentException(
                "Invalid ordinal " + ordinal + " for enum " + enumClass.getName()
            );
        }

        return values[ordinal];
    }

    @Override
    public Class<T> getEncoderClass() {
        return enumClass;
    }
}
