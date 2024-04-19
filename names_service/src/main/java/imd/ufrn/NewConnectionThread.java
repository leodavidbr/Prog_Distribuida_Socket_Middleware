package imd.ufrn;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class NewConnectionThread implements Runnable {

    protected Consumer<Socket> callbackFunctionClientSocketAccepted;
    protected ServerSocket serverSocket;

    public NewConnectionThread(ServerSocket serverSocket, Consumer<Socket> callbackFunctionMessageRecieved) {
        this.callbackFunctionClientSocketAccepted = callbackFunctionMessageRecieved;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                callbackFunctionClientSocketAccepted.accept(clientSocket);
            } catch (Exception e) {
                System.out.println("error accepting new clientSocket. Will continue to try");
            }
        }
    }

}
