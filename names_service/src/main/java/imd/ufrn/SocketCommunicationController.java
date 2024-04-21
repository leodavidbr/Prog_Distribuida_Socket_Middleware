package imd.ufrn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import imd.ufrn.exceptions.CouldNotInitializeSocketException;
import imd.ufrn.interfaces.BaseCommunicationWithServerController;
import imd.ufrn.model.ClientSocket;
import imd.ufrn.model.MessageRecieved;

// offers the sendMessage method and 
// calls the callbackFunctionMessageRecieved function when a 
// new message is recieved from the server
public class SocketCommunicationController extends BaseCommunicationWithServerController {

    private ServerSocket serverSocket;
    private List<ClientSocket> clientSockets = new ArrayList<>();
    private NewConnectionThread newConnectionRunnable;
    private Thread newConnectionThread;

    private int port = 8888;

    public SocketCommunicationController(Consumer<MessageRecieved> callbackFunctionMessageRecieved, int port) {
        super(callbackFunctionMessageRecieved);
        this.port = port;
        boolean isInitializedCorrectly = initialize();
        if (!isInitializedCorrectly) {
            throw new CouldNotInitializeSocketException(
                    "Socket could not initialize correctly. With port: " + this.port);
        }
        System.out.println(" nameService initialized correctly with port: " + this.port);
    }

    public SocketCommunicationController(Consumer<MessageRecieved> callbackFunctionMessageRecieved) {
        super(callbackFunctionMessageRecieved);
        boolean isInitializedCorrectly = initialize();
        if (!isInitializedCorrectly) {
            throw new CouldNotInitializeSocketException(
                    "Socket could not initialize correctly. With port: " + this.port);
        }
        System.out.println(" nameService initialized correctly with port: " + this.port);
    }

    @Override
    public void run() {
        getMessageLoop();
    }

    private void getMessageLoop() {
        System.out.println("initilizing socket getMessageLoop");
        String message = "";
        try {
            while (true) {
                System.out.println();
                System.out.println("---------------- new loop iteration ----------------");
                System.out.println();
                System.out.println("sockets list size: " + clientSockets.size());
                while (clientSockets.size() == 0) {
                }
                for (int i = 0; i < clientSockets.size(); ++i) {
                    message = getMessage(i);
                    System.out.println("Message recieved from client number " + i + ": " + message);
                    MessageRecieved messageRecieved = new MessageRecieved(message, i);
                    callbackFunctionMessageRecieved.accept(messageRecieved);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read message from server");
            e.printStackTrace();
            return;
        }

    }

    @Override
    public synchronized void sendMessage(String message, int clientIdx) {
        System.out.println("sending message: \"" + message + "\" for socket number: " + clientIdx);
        if (message == null || message.length() == 0) {
            return;
        }

        ClientSocket clientSocket = clientSockets.get(clientIdx);
        clientSocket.getWriteStream().println(message);
    }

    @Override
    protected boolean initialize() {
        if (!initializeSocket())
            return false;
        System.out.println("socket initilized");

        System.out.println("initilizing newConnectionThread");
        newConnectionRunnable = new NewConnectionThread(serverSocket,
                newClientSocket -> handleNewClientSocket(newClientSocket));
        newConnectionThread = new Thread(newConnectionRunnable);
        newConnectionThread.start();
        System.out.println("initialized newConnectionThread");

        return true;
    }

    private void handleNewClientSocket(Socket socket) {
        Optional<ClientSocket> newClientSocket = initializeReadAndWriteStream(socket);
        if (newClientSocket.isPresent()) {
            System.out.println("the new socket was was initialized. Adding to the list");
            clientSockets.add(newClientSocket.get());

            System.out.println("added. Updated list: ");
            System.out.println("size: " + clientSockets.size());
            for (int i = 0; i < clientSockets.size(); ++i) {
                System.out.println("idx: " + i + " port: " + clientSockets.get(i).getSocket().getPort());
            }
        }
    }

    private boolean initializeSocket() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Failed to create the server socket.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Optional<ClientSocket> initializeReadAndWriteStream(Socket socket) {
        System.out.println("Initializing Read and Write stream for socket");
        ClientSocket newClientSocket = new ClientSocket(socket);

        try {
            newClientSocket.setReadStream(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        } catch (IOException e) {
            System.out.println("Failed to initialize readStream of new socket");
            e.printStackTrace();
            return Optional.empty();
        }
        try {
            newClientSocket.setWriteStream(new PrintWriter(socket.getOutputStream(), true));
        } catch (IOException e) {
            System.out.println("Problem initializing writeStream of new socket");
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(newClientSocket);
    }

    private String getMessage(int clientIdx) throws IOException {
        String message = "";
        ClientSocket clientSocket = clientSockets.get(clientIdx);
        System.out.println("will try to readLine from client of idx: " + clientIdx);
        while (message == "") {
            message = clientSocket.getReadStream().readLine();
        }
        System.out.println("finished readLine from client of idx: " + clientIdx + " with content: \"" + message + "\"");

        return message;
    }

}
