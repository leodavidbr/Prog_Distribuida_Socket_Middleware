package imd.ufrn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import imd.ufrn.model.ReadWriteSocket;

public class SocketStub implements Runnable {
    Consumer<String> callbackFunctionMessageRecieved;
    static final String nameServiceHost = "127.0.0.1";
    static final int nameServicePort = 8888;
    BufferedReader serviceReader;

    ReadWriteSocket nameServiceSocket;

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
            System.out.println("waiting for response from nameService");
            while (response == "") {
                response = nameServiceSocket.getReadStream().readLine();
                List<String> responseParams = Arrays.asList(response.split(";"));
                address.setHost(responseParams.get(0));
                address.setPort(Integer.parseInt(responseParams.get(1)));
            }
        } catch (Exception e) {
            System.out.println("problem while reading nameService response");
            e.printStackTrace();
        }
        return address;
    }

    private void connectSocketSendRecieveFromService(Address serviceAddress, String request) {
        try {
            // Connect
            System.out.println(
                    "connecting to service: host: " + serviceAddress.getHost() + " port: " + serviceAddress.getPort());
            Socket serviceSocket;
            PrintWriter write;
            serviceSocket = new Socket(serviceAddress.getHost(), serviceAddress.getPort());

            write = new PrintWriter(serviceSocket.getOutputStream(), true);
            serviceReader = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
            System.out.println("succesfully connected to service. Writing to service");

            // Send
            write.println(request);
            System.out.println("wrote to service");

        } catch (Exception e) {
            System.out.println("could not initialize connection with name service");
            e.printStackTrace();
        }
    }

    private void initializeConnectionWithNameService() {
        try {
            Socket clientNamesServiceSocket = new Socket(nameServiceHost, nameServicePort);

            PrintWriter write = new PrintWriter(clientNamesServiceSocket.getOutputStream(), true);
            BufferedReader read = new BufferedReader(new InputStreamReader(clientNamesServiceSocket.getInputStream()));

            this.nameServiceSocket = new ReadWriteSocket(clientNamesServiceSocket, read, write);
        } catch (Exception e) {
            System.out.println("could not initialize connection with name service");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        getMessageLoop();
    }

    private void getMessageLoop() {
        String message = "";
        // try {
        while (true) {
            message = getMessage();
            System.out.println("Message recieved from client: \"" + message + "\"");
            handleMessageRecieved(message);
        }
    }

    private String getMessage() {
        String message = "";
        while (message == "") {
            try {
                if (serviceReader == null) {
                    System.out.println("serviceReader null");
                } else {
                    message = serviceReader.readLine();
                }
            } catch (Exception e) {
                System.out.println("failed to read message");
                e.printStackTrace();
            }
        }

        return message;
    }
}
