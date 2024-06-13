package org.solideinc.solidequerydistributor.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;

public class JsonHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_DIR = "data";

    private JsonHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T readJson(TypeReference<T> typeReference, String file) throws IOException {
        File validatedFile = validateFilePath(file);
        return objectMapper.readValue(validatedFile, typeReference);
    }

    public static void writeJson(Object obj, String file) throws IOException {
        File validatedFile = validateFilePath(file);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(validatedFile, obj);
    }

    public static void removeFile(String file) throws IOException {
        File validatedFile = validateFilePath(file);
        if (!validatedFile.delete()) {
            throw new IOException("Failed to delete file: " + file);
        }
    }

    private static File validateFilePath(String filePath) throws IOException {
        try {
            File file = Paths.get(BASE_DIR, filePath).normalize().toFile();
            if (!file.getCanonicalPath().startsWith(new File(BASE_DIR).getCanonicalPath())) {
                throw new IOException("Invalid file path: " + filePath);
            }
            return file;
        } catch (InvalidPathException e) {
            throw new IOException("Invalid file path: " + filePath, e);
        }
    }
}
