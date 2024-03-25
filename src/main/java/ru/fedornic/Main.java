package ru.fedornic;

import ru.fedornic.converter.Converter;
import ru.fedornic.converter.ColorSchema;
import ru.fedornic.interfaces.TextGraphicsConverter;
import ru.fedornic.server.Server;


public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new Converter();
        converter.setTextColorSchema(new ColorSchema(ColorSchema.BASIC_SCHEMA));
        converter.setMaxRatio(1.546d);

        // Создаём объект сервера
        Server server = new Server(converter);
        server.start();
    }
}