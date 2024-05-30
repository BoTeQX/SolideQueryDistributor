package org.solideinc.solidequerydistributor.Classes;

public class Message {
    private String message;
    private Boolean isAnswer;

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

    public Boolean getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(Boolean answer) {
        isAnswer = answer;
    }
}
