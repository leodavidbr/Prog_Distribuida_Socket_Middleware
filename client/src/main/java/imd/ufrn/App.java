package imd.ufrn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            SocketStub stubClient = new SocketStub(message -> handleMessageRecieved(message));

            while (true) {
                System.out.println("\n*** Cat Management System ***");
                System.out.println("1. Register Cat");
                System.out.println("2. Update Cat");
                System.out.println("3. Read Cat Details");
                System.out.println("4. Delete Cat");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                String choice = reader.readLine();
                switch (choice) {
                    case "1":
                        System.out.print("Enter id: ");
                        String id = reader.readLine();
                        System.out.print("Enter name: ");
                        String name = reader.readLine();
                        System.out.print("Enter color: ");
                        String color = reader.readLine();
                        System.out.print("Enter owner: ");
                        String owner = reader.readLine();
                        String parameters = id + ";" + name + ";" + color + ";" + owner;
                        try {
                            stubClient.sendMessage("CreateCat", parameters);
                            System.out.println("Cat saved!");
                        } catch (RuntimeException e) {
                            System.out.println("Failed to save cat");

                        }
                        break;
                    case "2":
                        System.out.print("Enter id (for update): ");
                        String idUpdate = reader.readLine();
                        System.out.print("Enter name (for update): ");
                        String nameUpdate = reader.readLine();
                        System.out.print("Enter color (for update): ");
                        String colorUpdate = reader.readLine();
                        System.out.print("Enter owner (for update): ");
                        String ownerUpdate = reader.readLine();
                        String parametersUpdate = idUpdate + ";" + nameUpdate + ";" + colorUpdate + ";" + ownerUpdate;
                        try {
                            stubClient.sendMessage("UpdateCat", parametersUpdate);
                            System.out.println("Cat updated!");
                        } catch (RuntimeException e) {
                            System.out.println("Failed to update cat");

                        }
                        break;
                    case "3":
                        System.out.print("Enter id (to read): ");
                        String idRead = reader.readLine();
                        try {
                            stubClient.sendMessage("ReadCat", idRead);
                        } catch (RuntimeException e) {
                            System.out.println("Failed to read cat");

                        }
                    case "4":
                        System.out.print("Enter id (to delete): ");
                        String idDelete = reader.readLine();
                        try {
                            stubClient.sendMessage("DeleteCat", idDelete);
                            System.out.println("Cat deleted");
                        } catch (RuntimeException e) {
                            System.out.println("Failed to read cat");

                        }
                        break;
                    case "5":
                        // stubClient.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleMessageRecieved(String message) {

    }
}
