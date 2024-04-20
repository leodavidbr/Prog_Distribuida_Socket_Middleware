package imd.ufrn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
                    client = server.accept();
                    write = new PrintWriter(client.getOutputStream(), true);
                    read = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    String request = read.readLine();
                    String[] reqParameters = request.split(";");
                    String serviceName = reqParameters[0];
                    switch (serviceName) {
                        case "CreateCat":
                            CreateCat.registerCat(reqParameters[1]);
                            break;
                        case "UpdateCat":
                            UpdateCat.updateCat(reqParameters[1]);
                            break;
                        case "DeleteCat":
                            DeleteCat.deleteCat(reqParameters[1]);
                            break;
                        case "ReadCat":
                            ReadCat.readCat(reqParameters[1]);
                            break;
                        default:
                    }
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
}
