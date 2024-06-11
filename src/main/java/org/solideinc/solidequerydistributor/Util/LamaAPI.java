package org.solideinc.solidequerydistributor.Util;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult;
import io.github.amithkoujalgi.ollama4j.core.types.OllamaModelType;
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import org.solideinc.solidequerydistributor.Controllers.MainController;

import java.io.IOException;

public class LamaAPI {
    private static final String HOST = "http://localhost:11434/";
    private static OllamaAPI ollamaAPI;

    private LamaAPI() {
        throw new IllegalStateException("Utility class");
    }

    public static void connectToHost() {
        ollamaAPI = new OllamaAPI(HOST);
        ollamaAPI.setVerbose(true);
        ollamaAPI.setRequestTimeoutSeconds(90);
    }

    public static boolean isConnected() {
        try {
            if (ollamaAPI == null || !ollamaAPI.ping() || MainController.isOfflineMode()) {
                System.out.println("Not connected");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Not connected due to error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static String sendPrompt(String prompt) throws OllamaBaseException, IOException, InterruptedException {
        if (ollamaAPI == null || !isConnected() || MainController.isOfflineMode())
            return "SERVER OFFLINE";

        OllamaResult result = ollamaAPI.generate(OllamaModelType.LLAMA3, prompt, new OptionsBuilder().build());
        return result.getResponse();
    }
}
