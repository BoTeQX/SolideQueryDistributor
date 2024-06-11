package org.solideinc.solidequerydistributor.Classes;

public class Message {
    private String text;
    private Boolean isAnswer;

    public Message() {
        // Default constructor
    }

    public Message(String message, Boolean isAnswer) {
        this.text = message;
        this.isAnswer = isAnswer;
    }

    public String getMessage() {
        return text;
    }

    public void setMessage(String message) {
        this.text = message;
    }

    public Boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(Boolean answer) {
        isAnswer = answer;
    }
}
