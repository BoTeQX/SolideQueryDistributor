package org.solideinc.solidequerydistributor.Classes;

public class Message {
    private String message;
    private Boolean isAnswer;

    public Message() {
        // Default constructor
    }

    public Message(String message, Boolean isAnswer) {
        this.message = message;
        this.isAnswer = isAnswer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(Boolean answer) {
        isAnswer = answer;
    }
}