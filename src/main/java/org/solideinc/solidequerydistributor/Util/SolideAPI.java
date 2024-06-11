package org.solideinc.solidequerydistributor.Util;

import org.solideinc.solidequerydistributor.Controllers.LoginController;

import java.util.HashMap;
import java.util.Map;

public class SolideAPI {
    private static final Map<String, String> promptResponses = new HashMap<>();
    public static String userLanguagePreference;

    private SolideAPI() {
        throw new IllegalStateException("Utility class");
    }

    public static void setPromptsBasedOnLanguagePreference() {
        userLanguagePreference = LoginController.getLoggedInUser().getLanguagePreference();

        if (userLanguagePreference != null) {
            if (userLanguagePreference.equals("nl")) {
                promptResponses.clear();
                setDutchPrompts();
            } else if (userLanguagePreference.equals("en")){
                promptResponses.clear();
                setEnglishPrompts();
            }
        }
    }

    private static void setDutchPrompts() {
        promptResponses.put("bestand", "hier is het bestand voor je: [bestand\uD83D\uDCC4]");
        promptResponses.put("documentatie", "Hier is de gevraagde documentatie: \n\n''...........................................................\n ................................................\n ......................'' \n Bron: [documentatie link]");
        promptResponses.put("hallo", "Hallo! Hoe kan ik je helpen?");
        promptResponses.put("hoe gaat het", "Goed, dank je! Hoe kan ik je helpen?");
        promptResponses.put("wat is je naam", "Ik ben Solide, je persoonlijke assistent. Hoe kan ik je helpen?");
        promptResponses.put("wat is je leeftijd", "Ik ben een computerprogramma, dus ik heb geen leeftijd. Hoe kan ik je helpen?");
        promptResponses.put("wat is je favoriete kleur", "Ik ben een computerprogramma, dus ik heb geen favoriete kleur. Hoe kan ik je helpen?");
        promptResponses.put("bedankt", "Graag gedaan! Hoe kan ik je helpen?");
        promptResponses.put("tot ziens", "Tot ziens! Als je nog vragen hebt, weet je me te vinden.");
        promptResponses.put("goedemorgen", "Goedemorgen! Hoe kan ik je helpen?");
        promptResponses.put("goedemiddag", "Goedemiddag! Hoe kan ik je helpen?");
        promptResponses.put("goedenavond", "Goedenavond! Hoe kan ik je helpen?");
        promptResponses.put("solide", "built different");

    }

    private static void setEnglishPrompts() {
        promptResponses.put("file", "here is the file for you: [file\uD83D\uDCC4]");
        promptResponses.put("documentation", "Here is your requested documentation: \n\n''...........................................................\n ................................................\n ......................'' \n Source: [documentation link]");
        promptResponses.put("hello", "Hello! How can I help you?");
        promptResponses.put("how are you", "Good, thank you! How can I help you?");
        promptResponses.put("what is your name", "I'm Solide, your personal assistant. How can I help you?");
        promptResponses.put("what is your age", "I'm a computer program, so I don't have an age. How can I help you?");
        promptResponses.put("what is your favorite color", "I'm a computer program, so I don't have a favorite color. How can I help you?");
        promptResponses.put("thanks", "You're welcome! How can I help you?");
        promptResponses.put("goodbye", "Goodbye! If you have any more questions, you know where to find me.");
        promptResponses.put("good morning", "Good morning! How can I help you?");
        promptResponses.put("good afternoon", "Good afternoon! How can I help you?");
        promptResponses.put("good evening", "Good evening! How can I help you?");
        promptResponses.put("solide", "built different");
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
