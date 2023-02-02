package CurrencyConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class Admin extends User {
    Scanner input = new Scanner(System.in);
    

    String help = "List of Commands:\n" +
            "get - prints a list of all currencies and their rates\n" +
            "convert - begins the conversion from one currency to another\n" +
            "table - prints a table of the most popular 4 currencies and their recent changes\n" +
            "history - returns the history of the exchange rate between 2 currencies\n" +
            "insert - allows admin to insert a new currency \n" +
            "update_popular - allows the admin to update popular currencies.\n" +
            "get_summary - returns the history of the exchange rate between 2 currencies aswell as the rate average, min, max, median and standard deviation\n" +
            "update_currency - allows the admin to update exchange rate from one currency to another\n"+
            "remove - allows the admin to remove a currency\n"+
            "exit - to exit ADMIN mode\n";
    public Admin(DatabaseManager DBM) {
        super(DBM);
    }

    public void adminRun() throws SQLException {
        // Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.print("> ");
            String name = input.nextLine();
            System.out.println();
            String[] split = name.split(" ");
            

            if (split[0].equalsIgnoreCase("insert")) {
                insertNewCurrency(split);
            }

            // "get" to retrieve all currencies from db
            else if (split[0].equalsIgnoreCase("get")) {
                getAllCurrencyRate();
            } else if (split[0].equalsIgnoreCase("remove")) {
                removeCurrency();
            } else if (split[0].equalsIgnoreCase("update_popular")) {
                updateMostPopular(null,null);
            }else if(split[0].equalsIgnoreCase("table")) {
                getPopularTable();
            }else if(split[0].equalsIgnoreCase("history")) {
                getHistory();
            }else if (split[0].equalsIgnoreCase("help")) {
                getHelp();
            }else if (split[0].equalsIgnoreCase("get_summary")) {
                getHistorySummary();
            } else if (split[0].equalsIgnoreCase("update_currency")) {
                updateCurrency(null, null, null);
            }else if (split[0].equalsIgnoreCase("convert")) {
                convert(null, null, 0);
            }else if (split[0].equalsIgnoreCase("exit")) {
                return;
            } else {
                System.out.println("> INVALID INPUT, TRY AGAIN!\n");
            }

            // "insert <currency_name> <rate>"" to add the currency to db
        }

    }

    @Override
    public void getHelp() {
        System.out.println(help);
    }

    // adds a new currency to the SQL database and to the hashmaps
    public void insertNewCurrency(String[] arg) throws SQLException {

        //This function is not finished
        ArrayList<String> currencies;
        ArrayList<Float> rates = new ArrayList<>();
        currencies = DBM.getCurrencies();
        System.out.printf("What is the name for the new currency? ");
        String newCurrencyName = input.nextLine().toUpperCase();
        DBM.initializeNewCurrency(newCurrencyName);
        
        for (int i=0;i < currencies.size();i++){
            System.out.print(String.format("Enter rate from %s to %s: ",currencies.get(i), newCurrencyName));
            String rate = input.nextLine();
            rates.add(Float.parseFloat(rate));
            DBM.initializeRatesNewCurrency(newCurrencyName,currencies.get(i),rate);
            DBM.insertHistory(currencies.get(i), newCurrencyName, rate);
        }

        DBM.lastInsert(rates,newCurrencyName);

    }

    public void removeCurrency() {
        System.out.print("What currency would you like to remove?");
        String currency = input.nextLine().toUpperCase();
        DBM.removeCurrency(currency);
        String query = String.format("DELETE  FROM Currencies WHERE name = '%s';", currency);
        ResultSet rS = DBM.executeQuery(query);
        System.out.println("Currency Removed successfully");
    }


    public void updateCurrency(String fromC1, String toC2, String newRate) {
        try {
            System.out.println("Current currencies:");
            ResultSet rs = DBM.executeQuery("SELECT Code_id FROM Currency");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            System.out.println();

            String from = fromC1;
            String to = toC2;
            String rate = newRate;
            String inverseRate;

            if (from == null && to == null && rate ==null) {

                while (true) {
                    System.out.print("Update\nFrom: ");
                    from = input.nextLine().toUpperCase();
                    System.out.println();
                    System.out.print("To: ");
                    to = input.nextLine().toUpperCase();
                    System.out.println();
                    System.out.printf("Current rate: %f\n", DBM.retrieveRate(from, to));
                    System.out.print("New rate: ");
                    rate = input.nextLine();
                    System.out.println();
                    inverseRate = String.valueOf(Math.round((1 / Float.parseFloat(rate)) * 100.0) / 100.0);
                    System.out.printf("You would like to update the rate - %s -> %s : %s, is this correct? (y/n)\n", from, to, rate);
                    if (input.nextLine().equalsIgnoreCase("y")) {
                        System.out.printf("Automatic new rate - %s -> %s : %s\n", to, from, inverseRate);
                        break;
                    } else {
                        System.out.println("Okay then, lets try again.");
                    }
                }
            } else {
                inverseRate = String.valueOf(Math.round((1 / Float.parseFloat(rate)) * 100.0) / 100.0);
            }

            String sql = String.format("UPDATE Currency SET '%s' = '%s' WHERE Code_id = '%s';", to, rate, from);
            DBM.executeUpdate(sql);
            sql = String.format("UPDATE Currency SET '%s' = '%s' WHERE Code_id = '%s';", from, inverseRate, to);
            DBM.executeUpdate(sql);
            System.out.println("Currency updated successfully");

            try{

                DBM.insertHistory(from, to, rate);
                System.out.println("Inserted update into history table");

            } catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            }


        } catch (Exception e) {
            System.out.println("Could not update currency successfully");
        }
    }

    public void updateMostPopular(String replace, String replaceWith) throws SQLException {
        System.out.println("Current popular currencies:");
        ResultSet rs = DBM.executeQuery("SELECT Code_id FROM Popular");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        if (replace == null && replaceWith == null){

            while(true) {
                System.out.println("Which one would you like to replace?");
                replace = input.nextLine();
                System.out.println("What would you like to replace it with?");
                replaceWith = input.nextLine();
                System.out.printf("You would like to replace %s with %s, is this correct? (y/n)\n", replace, replaceWith);
                if (input.nextLine().equalsIgnoreCase("y")) {
                    break;
                } else {
                    System.out.println("Okay then, lets try again.");
                }
            }
        }
        String sql = String.format("UPDATE Popular " +
                "SET Code_id = '%s' " +
                "WHERE Code_id = '%s';", replaceWith, replace);
        DBM.executeUpdate(sql);
        System.out.println("Popular currency updated");
    }
}