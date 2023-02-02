package CurrencyConverter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class User  {

    DatabaseManager DBM;

//    ArrayList<String> popularCurrencies;
    Scanner input = new Scanner(System.in);
    String help = "List of Commands:\n" +
            "get - prints a list of all currencies and their rates\n" +
            "convert - begins the conversion from one currency to another\n" +
            "table - prints a table of the most popular 4 currencies and their recent changes\n" +
            "history - returns the history of the exchange rate between 2 currencies\n" +
            "get_summary - returns the history of the exchange rate between 2 currencies aswell as the rate average, min, max, median and standard deviation\n" +
            "convert <from> <to> <amount> - Calculates the conversion from one currency to another \n" +
            "exit - to exit USER mode\n";


    public User(DatabaseManager DBM) {
        this.DBM = DBM;
    }

    public void userRun() throws SQLException {
        
        while (true) {
            
            System.out.println();
            System.out.print("> ");
            String name = input.nextLine();
            System.out.println();
            String[] split = name.split(" ");

            if (split[0].equalsIgnoreCase("get")) {
                getAllCurrencyRate();
            } else if(split[0].equalsIgnoreCase("help")) {
                getHelp();
            } else if(split[0].equalsIgnoreCase("table")) {
                getPopularTable();
            } else if(split[0].equalsIgnoreCase("history")) {
                getHistory();
            } else if(split[0].equalsIgnoreCase("convert")) {
                convert(null, null, 0);
            } else if(split[0].equalsIgnoreCase("get_summary")) {
                getHistorySummary();
            } else if(split[0].equalsIgnoreCase("exit")) {
                return;
            }else {
                System.out.println("> INVALID INPUT, TRY AGAIN!\n");
            }
        }

    }

    public void getHelp() {
        System.out.println(help);
    }

    public void getAllCurrencyRate() throws SQLException {
        String query = "SELECT * FROM Currency;";
        ResultSet rs = DBM.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnsNumber; i++) {
            System.out.print(rsmd.getColumnName(i));
            if (i != columnsNumber) {
                System.out.print("  ");
            }
        }
        System.out.print("\n");
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(columnValue);
                if (i != columnsNumber) {
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
    }

    public void convert(String c1, String c2,float amount) {

        if (c1 == null && c2 == null && amount == 0) {
            System.out.print("Currency from: ");
            c1 = input.nextLine().toUpperCase();
            System.out.print("\nCurrency to: ");
            c2 = input.nextLine().toUpperCase();
            System.out.print("\nAmount: ");
            amount = Float.parseFloat(input.nextLine());
        }

//        float finalAmount = amount * rate;
        float rate = DBM.retrieveRate(c1,c2);
        String output = String.format("RATE: 1 %s = %f %s",c1,rate,c2);
        System.out.println(output);
        output = String.format("%f %s = %f %s",amount,c1,amount*rate,c2);
        System.out.println(output);
        
    }

//    public void getTable() {
//
//        //Deprecated
//
//        try{
//            popularCurrencies = DBM.getPopularCurrencies();
//            StringBuilder sql = new StringBuilder();
//            String temp = "SELECT code_id, ";
//            sql.append(temp);
//
//            for (int i=0;i<popularCurrencies.size();i++){
//                if (i ==  popularCurrencies.size() - 1){
//                    temp = String.format("%s ",popularCurrencies.get(i));
//                }else{
//                    temp = String.format("%s,",popularCurrencies.get(i));
//                }
//
//                sql.append(temp);
//            }
//
//            temp = "FROM Currency WHERE ";
//            sql.append(temp);
//
//            for (int i=0;i<popularCurrencies.size();i++){
//                if (i ==  popularCurrencies.size() - 1){
//                    temp = String.format("code_id = '%s' ",popularCurrencies.get(i));
//                }else{
//                    temp = String.format("code_id = '%s' OR ",popularCurrencies.get(i));
//                }
//
//                sql.append(temp);
//            }
//
//            this.DBM.getPopularTable(sql.toString());
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
////            System.exit(0);
//            System.out.println("Records not created successfully");
//        }
//    }

    // public void getHistory(Currency c1, Currency c2, LocalDateTime startDate, LocalDateTime endDate) {
    //     String sql = String.format("SELECT * FROM History WHERE Timestamper BETWEEN %s and %s",startDate,endDate);
    //     ResultSet rs = DBM.executeQuery(sql);
    //     DBPrint.printResultSet(rs, 20);
    // }

    public void getHistory(){
        ArrayList<String> currencies = DBM.getCurrencies();
        System.out.print("Currency From: ");
        String c1 = input.nextLine();
        while (!(currencies.contains(c1.toUpperCase()))){
            System.out.println("Enter a valid currency\n");
            System.out.print("Currency From: ");
            c1 = input.nextLine();
        }

        System.out.print("Currency To: ");
        String c2 = input.nextLine();
        while (!(currencies.contains(c2.toUpperCase()))){
            System.out.println("Enter a valid currency\n");
            System.out.print("Currency To: ");
            c2 = input.nextLine();
        }
        System.out.println();
        DBM.printHistoryOfRates(c1,c2);

    }

     public void getHistorySummary () {

         ArrayList<String> currencies = DBM.getCurrencies();
         System.out.print("Currency From: ");
         String c1 = input.nextLine();
         while (!(currencies.contains(c1.toUpperCase()))){
             System.out.println("Enter a valid currency\n");
             System.out.print("Currency From: ");
             c1 = input.nextLine();
         }

         System.out.print("Currency To: ");
         String c2 = input.nextLine();
         while (!(currencies.contains(c2.toUpperCase()))){
             System.out.println("Enter a valid currency\n");
             System.out.print("Currency To: ");
             c2 = input.nextLine();
         }

         System.out.print("Start Date (YYYY-MM-DD): ");
         String c3 = input.nextLine();

         System.out.print("End Date (YYYY-MM-DD): ");
         String c4 = input.nextLine();

         System.out.println();

         DBM.printSummary(c1, c2, c3, c4);

         System.out.println();
     }


     public void getPopularTable() throws SQLException {
        ArrayList<String> popularCurrencies = DBM.getPopularCurrencies();

        String banner = String.format("""
                +------------------------------------------------------+
                |   To/From |   %s   |   %s   |   %s    |   %s    |
                +------------------------------------------------------+
                """, popularCurrencies.get(0), popularCurrencies.get(1),popularCurrencies.get(2),popularCurrencies.get(3));

        System.out.print(banner);

        ArrayList<String> rates = new ArrayList<>();
        ArrayList<String> change = new ArrayList<>();

        for (String currencySymbolFrom : popularCurrencies) {
            rates.clear();
            change.clear();

            for (String currencySymbolTo : popularCurrencies) {
                String sqlRateQuery = String.format("""
                        SELECT %s
                        FROM Currency
                        WHERE Code_id = '%s'
                        """, currencySymbolFrom, currencySymbolTo);
                ResultSet rateResultSet = DBM.executeQuery(sqlRateQuery);
                rates.add(rateResultSet.getString(1));

                String sqlHistoricalRateQuery = String.format("""
                        SELECT Rate
                        FROM History
                        WHERE CurrencyFrom = '%s' AND Currency = '%s'
                        ORDER BY TimeStamper DESC
                        """, currencySymbolFrom, currencySymbolTo);
                ResultSet historicalValuesResultSet = DBM.executeQuery(sqlHistoricalRateQuery);
                if (DBM.getResultSetCount(historicalValuesResultSet) > 1) {
                    historicalValuesResultSet = DBM.executeQuery(sqlHistoricalRateQuery);
                    historicalValuesResultSet.next();
                    float current = Float.parseFloat(historicalValuesResultSet.getString(1));
                    historicalValuesResultSet.next();
                    float mostRecent = Float.parseFloat(historicalValuesResultSet.getString(1));

                    if (current > mostRecent) {
                    change.add("D");
                    } else if (current < mostRecent) {
                        change.add("I");
                    } else {
                        change.add("-");
                    }
                } else {
                    change.add("-");
                }

            }

            String row = String.format("""
                    |   %s     |  %s %-3s  |  %s %-3s  |  %s %-3s  |  %s %-3s  |
                    +------------------------------------------------------+
                    """, currencySymbolFrom,
                    change.get(0),
                    rates.get(0),
                    change.get(1),
                    rates.get(1),
                    change.get(2),
                    rates.get(2),
                    change.get(3),
                    rates.get(3));

            System.out.print(row);

        }


     }
}