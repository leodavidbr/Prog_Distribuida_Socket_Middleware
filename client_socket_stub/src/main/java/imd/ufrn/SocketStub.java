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
    String nameServiceHost = "127.0.0.1";
    int nameServicePort = 8888;
    BufferedReader serviceReader;

    ReadWriteSocket nameServiceSocket;

    public SocketStub(Consumer<String> callbackFunctionMessageRecieved) {
        this.callbackFunctionMessageRecieved = callbackFunctionMessageRecieved;
        initializeConnectionWithNameService();
    }

    public void sendMessage(String serviceName, String request) {
        Address serviceAddress = findService(serviceName);
        connectSocketSendRecieveFromService(serviceAddress, request);
    }

    private void handleMessageRecieved(String message) {
        callbackFunctionMessageRecieved.accept(message);
    }

    private Address findService(String serviceName) {
        nameServiceSocket.getWriteStream().write("getService;" + serviceName);
        Address address = new Address();

        String response = "";
        try {
            while (response == "") {
                response = nameServiceSocket.getReadStream().readLine();
                List<String> responseParams = Arrays.asList(response.split(";"));
                address.setHost(responseParams.get(0));
                address.setPort(Integer.parseInt(responseParams.get(1)));
            }
        } catch (Exception e) {
            System.out.println("problem while reading nameService response");
        }
        return address;
    }

    private String connectSocketSendRecieveFromService(Address serviceAddress, String request) {
        String response = "";
        try {
            // Connect
            Socket serviceSocket;
            PrintWriter write;
            serviceSocket = new Socket(serviceAddress.getHost(), serviceAddress.getPort());

            write = new PrintWriter(serviceSocket.getOutputStream(), true);
            serviceReader = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));

            // Send
            write.write(request);

        } catch (Exception e) {
            System.out.println("could not initialize connection with name service");
            System.out.println(e.toString());
        }
        return response;
    }

    private void initializeConnectionWithNameService() {
        try {
            Socket clientNamesServiceSocket = new Socket(nameServiceHost, nameServicePort);

            PrintWriter write = new PrintWriter(clientNamesServiceSocket.getOutputStream(), true);
            BufferedReader read = new BufferedReader(new InputStreamReader(clientNamesServiceSocket.getInputStream()));

            this.nameServiceSocket = new ReadWriteSocket(clientNamesServiceSocket, read, write);
        } catch (Exception e) {
            System.out.println("could not initialize connection with name service");
            System.out.println(e.toString());
        }
    }

    @Override
    public void run() {
        getMessageLoop();
    }

    private void getMessageLoop() {
        String message;
        try {
            while (true) {
                message = getMessage();
                System.out.println("Message recieved from client:" + message);
                handleMessageRecieved(message);
            }
        } catch (IOException e) {
            System.out.println("Failed to read message from server");
            e.printStackTrace();
            return;
        }

    }

    private String getMessage() throws IOException {
        String message = "";
        message = serviceReader.readLine();

        return message;
    }
}
