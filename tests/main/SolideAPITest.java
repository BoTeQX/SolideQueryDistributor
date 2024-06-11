package main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.solideinc.solidequerydistributor.Classes.User;
import org.solideinc.solidequerydistributor.Controllers.LoginController;
import org.solideinc.solidequerydistributor.Util.SolideAPI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class SolideAPITest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = Mockito.mock(User.class);
        LoginController.setLoggedInUser(user);
    }

    @Test
    public void testSetPromptsBasedOnLanguagePreferenceDutch() {
        when(user.getLanguagePreference()).thenReturn("nl");

        SolideAPI.setPromptsBasedOnLanguagePreference();

        String response = SolideAPI.sendPrompt("hallo");
        assertEquals("Hallo! Hoe kan ik je helpen?", response);

        response = SolideAPI.sendPrompt("wat is je leeftijd");
        assertEquals("Ik ben een computerprogramma, dus ik heb geen leeftijd. Hoe kan ik je helpen?", response);
    }

    @Test
    public void testSetPromptsBasedOnLanguagePreferenceEnglish() {
        when(user.getLanguagePreference()).thenReturn("en");

        SolideAPI.setPromptsBasedOnLanguagePreference();

        String response = SolideAPI.sendPrompt("hello");
        assertEquals("Hello! How can I help you?", response);

        response = SolideAPI.sendPrompt("what is your age");
        assertEquals("I'm a computer program, so I don't have an age. How can I help you?", response);
    }

    @Test
    public void testSetPromptsBasedOnLanguagePreferenceUnknownLanguage() {
        when(user.getLanguagePreference()).thenReturn("es");

        SolideAPI.setPromptsBasedOnLanguagePreference();

        String response = SolideAPI.sendPrompt("hola");
        assertNull(response);
    }

    @Test
    public void testSetPromptsBasedOnLanguagePreferenceEmptyLanguage() {
        when(user.getLanguagePreference()).thenReturn("");

        SolideAPI.setPromptsBasedOnLanguagePreference();

        String response = SolideAPI.sendPrompt("hello");
        assertNull(response);
    }

    @Test
    public void testSendPromptWithNullLanguage() {
        when(user.getLanguagePreference()).thenReturn(null);

        SolideAPI.setPromptsBasedOnLanguagePreference();

        String response = SolideAPI.sendPrompt("hello");
        assertNull(response);
    }

    @Test
    public void testSendPromptWithEmptyPrompt() {
        when(user.getLanguagePreference()).thenReturn("en");

        SolideAPI.setPromptsBasedOnLanguagePreference();

        String response = SolideAPI.sendPrompt("");
        assertNull(response);
    }

    @Test
    public void testSendPromptWithPartialMatch() {
        when(user.getLanguagePreference()).thenReturn("nl");

        SolideAPI.setPromptsBasedOnLanguagePreference();

        String response = SolideAPI.sendPrompt("hallo, hoe gaat het?");
        assertEquals("Goed, dank je! Hoe kan ik je helpen?", response);
    }
}
