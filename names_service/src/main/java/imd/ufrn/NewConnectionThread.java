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
        System.out.println("running main loop of newConnectionThread");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("succesfuly accepted new socket, client port: " + clientSocket.getPort()
                        + ". Will call callback function.");
                callbackFunctionClientSocketAccepted.accept(clientSocket);
            } catch (Exception e) {
                System.out.println("error accepting new clientSocket. Will continue to try");
            }
        }
    }

}
