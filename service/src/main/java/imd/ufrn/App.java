package imd.ufrn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import imd.services.CreateCat;
import imd.services.DeleteCat;
import imd.services.ReadCat;
import imd.services.UpdateCat;

public class App {
    public static void main(String[] args) {
        try {
            Socket clientNamesService = new Socket("127.0.0.1", 8888);
            ServerSocket server = new ServerSocket(9999);
            Socket client;

            PrintWriter write = new PrintWriter(clientNamesService.getOutputStream(), true);
            BufferedReader read = new BufferedReader(new InputStreamReader(clientNamesService.getInputStream()));

            subscriveService(clientNamesService, read, write);

            clientNamesService.close();

            while (true) {
                try {
                    System.out.println();
                    System.out.println("---------------- new loop iteration ----------------");
                    System.out.println();
                    System.out.println("waiting to accept client connection");
                    client = server.accept();
                    System.out.println("connection succesfuly done with port: " + client.getPort());
                    write = new PrintWriter(client.getOutputStream(), true);
                    read = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    System.out.println("trying to readLine");
                    String request = read.readLine();
                    System.out.println("readLine succesfull, read: \"" + request + "\"");
                    List<String> reqParameters = Arrays.asList(request.split(";"));

                    String serviceName = reqParameters.get(0);
                    String responseToSend = "";
                    switch (serviceName) {
                        case "CreateCat":
                            System.out.println("create cat request");
                            responseToSend = CreateCat.registerCat(reqParameters);
                            System.out.println("request processed. Will send response");
                            break;
                        case "UpdateCat":
                            System.out.println("update cat request");
                            responseToSend = UpdateCat.updateCat(reqParameters);
                            System.out.println("request processed. Will send response");
                            break;
                        case "DeleteCat":
                            System.out.println("delete cat request");
                            responseToSend = DeleteCat.deleteCat(reqParameters);
                            System.out.println("request processed. Will send response");
                            break;
                        case "ReadCat":
                            System.out.println("read cat request");
                            responseToSend = ReadCat.readCat(reqParameters);
                            System.out.println("request processed. Will send response");
                            break;
                        default:
                            System.out
                                    .println("request malformed. Could not identify method with name: " + serviceName);
                    }
                    if (responseToSend != "") {
                        System.out.println("will send response to client with content: \"" + responseToSend + "\"");
                        sendResponseToClient(write, responseToSend);
                        System.out.println("response sent");
                    }
                    client.close();
                    System.out.println("connection closed. Will restart listening to client");
                } catch (IOException e) {

                }
            }
        } catch (IOException e) {

        }

    }

    public static void subscriveService(Socket clientNamesService, BufferedReader read, PrintWriter write)
            throws IOException {
        String host = clientNamesService.getInetAddress().getHostAddress();

        Integer port = 9999;

        String message = "subscribeService;catService;" + host + ";" + port;

        write.println(message);
    }

    private static void sendResponseToClient(PrintWriter write, String response) {
        write.println(response);
    }
}
