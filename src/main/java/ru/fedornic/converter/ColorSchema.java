package ru.fedornic.converter;

import ru.fedornic.interfaces.TextColorSchema;

public class ColorSchema implements TextColorSchema {
    private final Character[] schema;
    public static final Character[] BASIC_SCHEMA = {'#', '$', '@', '%', '*', '+', '-', '\''};

    public ColorSchema() {
        schema = BASIC_SCHEMA;
    }

    public ColorSchema(Character[] chars) {
        schema = chars;
    }

    @Override
    public char convert(int color) {
        return schema[(int) ((color - Byte.MIN_VALUE) / ((Byte.MAX_VALUE - Byte.MIN_VALUE + 1d)/schema.length))];
    }
}