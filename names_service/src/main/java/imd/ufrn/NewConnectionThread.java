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
        System.out.println("[NEWCONNECTION_THREAD] running main loop of newConnectionThread");

        while (true) {
            System.out.println("[NEWCONNECTION_THREAD] Will try to accept new client");
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println(
                        "[NEWCONNECTION_THREAD] succesfuly accepted new socket, client port: " + clientSocket.getPort()
                                + ". Will call callback function.");
                callbackFunctionClientSocketAccepted.accept(clientSocket);
                System.out.println("[NEWCONNECTION_THREAD] finished accepting new socket");
            } catch (Exception e) {
                System.out.println("[NEWCONNECTION_THREAD] error accepting new clientSocket. Will continue to try");
                e.printStackTrace();
            }
        }
    }

}
