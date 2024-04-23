package imd.ufrn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import imd.ufrn.model.ReadWriteSocket;

public class SocketStub implements Runnable {
    Consumer<String> callbackFunctionMessageRecieved;
    static final String nameServiceHost = "127.0.0.1";
    static final int nameServicePort = 8888;
    // BufferedReader serviceReader;

    ReadWriteSocket nameServiceSocket;
    ReadWriteSocket serviceSocket;
    // private boolean connectedToNameService = false;

    public SocketStub(Consumer<String> callbackFunctionMessageRecieved) {
        System.out.println("[socket] entered socket constructor");
        this.callbackFunctionMessageRecieved = callbackFunctionMessageRecieved;
        initializeConnectionWithNameService();
        System.out.println("[socket] initialized correctly");
    }

    public void sendMessage(String serviceName, String requestParams) {
        System.out.println("[socket] sending message");
        Address serviceAddress = findService(serviceName);
        System.out.println(
                "[socket] found service, host: " + serviceAddress.getHost() + " and port: " + serviceAddress.getPort());
        connectSocketSendRecieveFromService(serviceAddress, requestParams);
        System.out.println("[socket] finished sending message");
    }

    private void handleMessageRecieved(String message) {
        System.out.println("[socket] recieved a message: \"" + message + "\". Callign callback function");
        callbackFunctionMessageRecieved.accept(message);
    }

    private Address findService(String serviceName) {
        String requestToNameService = "getService;" + serviceName;
        nameServiceSocket.getWriteStream().println(requestToNameService);
        System.out.println("[socket] sent request to nameService: \"" + requestToNameService + "\"");
        Address address = new Address();

        String response = "";
        try {
            System.out.println("[socket] waiting for response from nameService");
            while (response == "") {
                response = nameServiceSocket.getReadStream().readLine();
                List<String> responseParams = Arrays.asList(response.split(";"));
                address.setHost(responseParams.get(0));
                address.setPort(Integer.parseInt(responseParams.get(1)));
            }
        } catch (Exception e) {
            System.out.println("[socket] problem while reading nameService response");
            e.printStackTrace();
        }
        return address;
    }

    private void connectSocketSendRecieveFromService(Address serviceAddress, String request) {
        try {
            // Connect
            System.out.println(
                    "[socket] connecting to service: host: " + serviceAddress.getHost() + " port: "
                            + serviceAddress.getPort());
            Socket serviceSocketTmp = new Socket(serviceAddress.getHost(), serviceAddress.getPort());
            PrintWriter write;
            BufferedReader reader;

            write = new PrintWriter(serviceSocketTmp.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(serviceSocketTmp.getInputStream()));
            this.serviceSocket = new ReadWriteSocket(serviceSocketTmp, reader, write);

            System.out.println("[socket] succesfully connected to service. Writing to service");

            // Send
            write.println(request);
            System.out.println("[socket] wrote to service");

        } catch (Exception e) {
            System.out.println("[socket] could not initialize connection with name service");
            e.printStackTrace();
        }
    }

    private void initializeConnectionWithNameService() {
        System.out.println("[socket] will try to connect to name service");
        try {
            Socket clientNamesServiceSocket = new Socket(nameServiceHost, nameServicePort);

            PrintWriter write = new PrintWriter(clientNamesServiceSocket.getOutputStream(), true);
            BufferedReader read = new BufferedReader(new InputStreamReader(clientNamesServiceSocket.getInputStream()));

            this.nameServiceSocket = new ReadWriteSocket(clientNamesServiceSocket, read, write);
        } catch (Exception e) {
            System.out.println("[socket] could not initialize connection with name service");
            e.printStackTrace();
        }
        System.out.println("[socket] connected to name service succesfully");
        // connectedToNameService = true;
    }

    @Override
    public void run() {
        getMessageLoop();
    }

    private void getMessageLoop() {
        String message = "";
        // try {
        System.out.println("[socket] entering get messageLoop");
        while (true) {
            message = getMessage();
            System.out.println("[socket] Message recieved from client: \"" + message + "\"");
            handleMessageRecieved(message);
        }
    }

    private String getMessage() {
        int count = 1;
        Random rand = new Random();
        String message = "";
        while (message == "") {
            try {
                if (serviceSocket == null) {
                    // Just so java doesnt optimize
                    if (rand.nextInt(count) == count) {
                        ++count;
                    }
                } else {
                    message = serviceSocket.getReadStream().readLine();
                }
            } catch (Exception e) {
                System.out.println("[socket] failed to read message");
                e.printStackTrace();
            }
        }

        System.out.println("[socket] got message from server: \"" + message + "\". Closing the connection");
        // Close old connection
        try {
            if (serviceSocket != null) {
                System.out.println("[socket] closing old connection");
                this.serviceSocket.getSocket().close();
                this.serviceSocket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
