package imd.ufrn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            // create stub passing port
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
                        System.out.print("Enter name: ");
                        String name = reader.readLine();
                        System.out.print("Enter color: ");
                        String color = reader.readLine();
                        System.out.print("Enter owner: ");
                        String owner = reader.readLine();
                        // Call stub of createCat passing the parameters (name, color, owner);
                        break;
                    case "2":
                        System.out.print("Enter name (for update): ");
                        String nameUpdate = reader.readLine();
                        System.out.print("Enter color (for update): ");
                        String colorUpdate = reader.readLine();
                        System.out.print("Enter owner (for update): ");
                        String ownerUpdate = reader.readLine();
                        // Call stub of updateCat passing the parameters (nameUpdate, colorUpdate,
                        // ownerUpdate);
                        break;
                    case "3":
                        System.out.print("Enter name (to read): ");
                        String nameRead = reader.readLine();
                        // Call stub of readCat passing the parameter(nameRead);
                    case "4":
                        System.out.print("Enter name (to delete): ");
                        String nameDelete = reader.readLine();
                        // Call stub of deleteCat passing the parameter(nameRead);
                        break;
                    case "5":
                        // stub.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
