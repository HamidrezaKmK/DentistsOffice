package view;

import controller.DataBaseQueryController;
import model.QueryType;

import java.util.Arrays;
import java.util.Scanner;

public class UserInterface {
    public static void main(String[] args) throws Exception {
        DataBaseQueryController dbcontroller = new DataBaseQueryController();
        dbcontroller.setUsername("postgres");
        dbcontroller.setPassword("dibimibi");
        dbcontroller.setUrl("jdbc:postgresql://localhost:5432/DentistOfficeDB");
        dbcontroller.connect();
        System.out.println("Connection successfully created!");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--- Query Handler ---");
            for (int i = 0; i < QueryType.values().length; i++) {
                System.out.format("[%2d] " + QueryType.values()[i] + "\n", i);
            }
            System.out.println("Choose one of the queries obove: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Query type is " + QueryType.values()[id] + ". Enter query arguments:");
            String line = scanner.nextLine();
            line = line.trim();
            String[] parts = line.split(" ");
            dbcontroller.handleQuery(QueryType.values()[id], parts);
            System.out.print("Do you wish to coninue? [y/n] ");
            String response = scanner.nextLine();
            if (response.charAt(0) == 'n')
                break;
        }
    }
}
