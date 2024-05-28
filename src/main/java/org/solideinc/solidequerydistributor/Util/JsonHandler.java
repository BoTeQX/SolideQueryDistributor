package org.solideinc.solidequerydistributor.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readJson(TypeReference<T> typeReference, String file) throws IOException {
        return objectMapper.readValue(new File("data/" + file), typeReference);
    }

    public static void writeJson(Object obj, String file) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("data/" + file), obj);
    }
}
