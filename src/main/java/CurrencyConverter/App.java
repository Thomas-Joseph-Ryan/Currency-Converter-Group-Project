/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package CurrencyConverter;

import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

public class App {

    private static DatabaseManager DBM;

    public static void main(String[] args) throws SQLException {

        //Creating/Opening the database.
        DBM = new DatabaseManager("jdbc:sqlite:src/main/resources/currencyConverter.db");
        DBM.connectToDatabase();
        DBM.createCurrencyTable();
        if (DBM.count("Code_id", "Currency") == 0) {
            DBM.insertDefaultCurrencyValues();

        }

        DBM.createHistoryTable();
        if (DBM.count("Currency", "History") == 0) {
            DBM.initialiseHistoryTable();
        }


        DBM.createPopularCurrencyTable();
        if (DBM.count("Code_id", "Popular") == 0) {
            DBM.insertDefaultPopularCurrencies();
        }


        //Start program
        checkUserOrAdmin();

    }

    public static void checkUserOrAdmin() throws SQLException {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("---------------------------");
            System.out.print("> Are you a user or admin? ");
            String user_input = input.nextLine();
            if (user_input.equalsIgnoreCase("user")) {
                System.out.println("Hello User");
                User user = new User(DBM);
                user.userRun();
            } else if (user_input.equalsIgnoreCase("admin")) {
                System.out.println("Welcome Admin!");
                Admin admin = new Admin(DBM);
                admin.adminRun();
            } else if (user_input.equalsIgnoreCase("exit")) {
                return;
            } else {
                System.out.println("> INVALID INPUT, TRY AGAIN!");
            }
        }
    }
}
