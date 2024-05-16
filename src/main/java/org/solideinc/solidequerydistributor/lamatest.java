package org.solideinc.solidequerydistributor;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult;
import io.github.amithkoujalgi.ollama4j.core.types.OllamaModelType;
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;

import java.io.IOException;

public class lamatest {

    public static void main(String[] args) {

        String host = "http://localhost:11434/";
        OllamaAPI ollamaAPI = new OllamaAPI(host);

        ollamaAPI.setVerbose(true);
        ollamaAPI.setRequestTimeoutSeconds(999);

        boolean isOllamaServerReachable = ollamaAPI.ping();

        System.out.println("Is Ollama server alive: " + isOllamaServerReachable);

        try {
            OllamaResult result = ollamaAPI.generate(OllamaModelType.LLAMA3, "Who are you?", new OptionsBuilder().build());
            System.out.println(result.getResponse());
        } catch (OllamaBaseException | IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("An error occurred while generating the response: " + e.getMessage());
        }
    }
}
