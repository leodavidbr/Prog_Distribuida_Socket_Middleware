package imd.ufrn.interfaces;

import java.util.function.Consumer;

import imd.ufrn.model.MessageRecieved;

// offers the sendMessage method and 
// calls the callbackFunctionMessageRecieved function when a 
// new message is recieved from the server
// has to be initialized before use.
public abstract class BaseCommunicationWithServerController implements Runnable {

    protected Consumer<MessageRecieved> callbackFunctionMessageRecieved;

    public BaseCommunicationWithServerController(Consumer<MessageRecieved> callbackFunctionMessageRecieved) {
        this.callbackFunctionMessageRecieved = callbackFunctionMessageRecieved;
    }

    protected abstract boolean initialize();

    public abstract void sendMessage(String mensagem, int clientIdx);
}
