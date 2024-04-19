package imd.ufrn.interfaces;

import java.util.function.Consumer;

// offers the sendMessage method and 
// calls the callbackFunctionMessageRecieved function when a 
// new message is recieved from the server
// has to be initialized before use.
public abstract class BaseCommunicationWithServerController implements Runnable {

    protected Consumer<String> callbackFunctionMessageRecieved;

    public BaseCommunicationWithServerController(Consumer<String> callbackFunctionMessageRecieved) {
        this.callbackFunctionMessageRecieved = callbackFunctionMessageRecieved;
    }

    protected abstract boolean initialize();

    public abstract void sendMessage(String mensagem);
}
