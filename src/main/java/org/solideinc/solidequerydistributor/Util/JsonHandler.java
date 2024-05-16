package org.solideinc.solidequerydistributor.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JsonHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void writeJson(Object data, String file) throws IOException {
        objectMapper.writeValue(new File("data/" + file), data);
    }

    public static <T> T readJson(Class<T> valueType, String file) throws IOException {
        return objectMapper.readValue(new File("data/" + file), valueType);
    }
}
