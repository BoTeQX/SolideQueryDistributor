package org.solideinc.solidequerydistributor.Util;

import java.util.HashMap;
import java.util.Map;

public class SolideAPI {
    private static final Map<String, String> promptResponses = new HashMap<>();

    static {
        promptResponses.put("bestand", "Hier is je bestand: ....");
        promptResponses.put("test", "tesxtdasd");
    }

    public static String sendPrompt(String prompt) {
        prompt = prompt.toLowerCase();
        for (Map.Entry<String, String> entry : promptResponses.entrySet()) {
            if (prompt.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
