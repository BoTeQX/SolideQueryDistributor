package org.solideinc.solidequerydistributor.Util;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult;
import io.github.amithkoujalgi.ollama4j.core.types.OllamaModelType;
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import io.github.amithkoujalgi.ollama4j.core.models.ModelDetail;
import java.io.IOException;

public class LamaAPI {
    private static final String host = "http://localhost:11434/";
    private static OllamaAPI ollamaAPI;
    public static void connectToHost() {
        ollamaAPI = new OllamaAPI(host);
        ollamaAPI.setVerbose(true);
        ollamaAPI.setRequestTimeoutSeconds(999);
    }

    public static boolean isConnected() {
        if (ollamaAPI == null)
            return false;

        return ollamaAPI.ping();
    }

    public static String sendPrompt(String prompt) {
        if (ollamaAPI == null || !isConnected())
            return "SERVER OFFLINE";

        try {
            OllamaResult result = ollamaAPI.generate(OllamaModelType.LLAMA3, prompt, new OptionsBuilder().build());
            return result.getResponse();
        } catch (OllamaBaseException | IOException | InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
