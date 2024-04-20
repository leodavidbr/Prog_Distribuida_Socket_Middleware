package imd.ufrn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import imd.ufrn.models.Cat;

public class CatService {
    private Map<String, Cat> cats = new HashMap<>();

    private void registerServices() {
        String nameServiceHost = "localhost";
        int nameServicePort = 9999;
        String[] services = { "CreateCat", "UpdateCat", "DeleteCat", "ReadCat" };

        try (Socket socket = new Socket(nameServiceHost, nameServicePort);
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            for (String service : services) {
                out.println("register");
                out.println(service);
            }
        } catch (IOException e) {
            System.out.println("Could not register services: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleMessage(String message) {
        String[] parts = message.split(";");
        String command = parts[0];
        switch (command) {
            case "CreateCat":
                createCat(parts[1], parts[2], parts[3], parts[4]);
                break;
            case "UpdateCat":
                updateCat(parts[1], parts[2], parts[3], parts[4]);
                break;
            case "DeleteCat":
                deleteCat(parts[1]);
                break;
            case "ReadCat":
                String catDetails = readCat(parts[1]);
                break;
            default:
        }
    }

    private String createCat(String id, String name, String color, String owner) {
        if (cats.containsKey(id)) {
            return "Error: Cat with id " + id + " already exists.";
        }
        Cat newCat = new Cat(id, name, color, owner);
        cats.put(id, newCat);
        return "Cat created: " + newCat.getName();
    }

    private String updateCat(String id, String name, String color, String owner) {
        if (!cats.containsKey(id)) {
            return "Error: No cat found with id " + id;
        }
        Cat existingCat = cats.get(id);
        existingCat.setName(name);
        existingCat.setColor(color);
        existingCat.setOwner(owner);
        return "Cat updated: " + name;
    }

    private String deleteCat(String id) {
        if (cats.remove(id) != null) {
            return "Cat deleted: " + id;
        } else {
            return "Error: No cat found with id " + id;
        }
    }

    private String readCat(String id) {
        Cat cat = cats.get(id);
        if (cat != null) {
            return "Cat Details - Name: " + cat.getName() + ", Color: " + cat.getColor() + ", Owner: " + cat.getOwner();
        } else {
            return "Error: No cat found with id " + id;
        }
    }

}
