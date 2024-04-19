package imd.ufrn.model;

public class MessageRecieved {
    private String message;
    private int clientIdx;

    public MessageRecieved(String message, int clientIdx) {
        this.message = message;
        this.clientIdx = clientIdx;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getClientIdx() {
        return clientIdx;
    }

    public void setClientIdx(int clientIdx) {
        this.clientIdx = clientIdx;
    }
}
