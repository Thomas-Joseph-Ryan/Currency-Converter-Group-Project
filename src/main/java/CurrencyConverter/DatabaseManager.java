package CurrencyConverter;

import java.sql.*;
import java.util.ArrayList;
import java.lang.StringBuilder;

public class DatabaseManager {
//    DBTablePrinter DBTable;
    private final String url;
    public Statement stmt = null;
    // private ResultSet rs;

    public  DatabaseManager(String url){
        this.url = url;
    }

//    public Connection getConnection(){
//        return this.c;
//    }

    public void connectToDatabase() {
        try {
            Connection c = DriverManager.getConnection(url);
            if (c != null) {
//                DatabaseMetaData meta = c.getMetaData();
                // System.out.println("The driver name is " + meta.getDriverName());
                // System.out.println("A new database has been created.");
                stmt = c.createStatement();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createCurrencyTable() {
        try {
            Class.forName("org.sqlite.JDBC");
            // System.out.println("Opened database successfully");

            String sql = "CREATE TABLE IF NOT EXISTS Currency " +
                    " (Code_id  VARCHAR(20) PRIMARY KEY, " +
                    " AUD VARCHAR(20), " +
                    " USD VARCHAR(20)," +
                    " EUR VARCHAR(20)," +
                    " CAD VARCHAR(20)," +
                    " HKD VARCHAR(20)," +
                    " SGD VARCHAR(20));";
            stmt.executeUpdate(sql);
            System.out.println("Currency Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
            System.out.println("Table not created successfully");
        }
    }

    public void insertDefaultCurrencyValues() {
        try {
            Class.forName("org.sqlite.JDBC");
            // System.out.println("Opened database successfully");

            String sql = "INSERT INTO currency VALUES ('AUD', '1', '0.68','0.67', '0.89', '5.38', '0.98');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO currency VALUES ('USD', '1.46','1','0.98', '1.3', '7.85', '1.4');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO currency VALUES  ('EUR', '1.48','1.02','1', '1.31', '7.97', '1.42')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO currency VALUES  ('CAD', '1.12','0.77','0.76', '1', '6.01', '1.07')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO currency VALUES  ('HKD', '0.19','0.13','0.13', '0.16', '1', '0.18')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO currency VALUES  ('SGD', '1.04','0.71','0.7', '0.93', '5.60', '1')";
            stmt.executeUpdate(sql);
            System.out.println("Currency Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
            // System.out.println("Table not created successfully");
        }
    }



    public int count(String column ,String database) {
        try {
            Class.forName("org.sqlite.JDBC");
            // System.out.println("Opened database successfully");

            String sql = String.format("SELECT COUNT(%s) FROM %s", column, database);
            return stmt.executeQuery(sql).getInt(1);


        } catch ( Exception e ) {
            return -1;
            // System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            // System.exit(0);
            // System.out.println("Count unsuccessful");
        }

        // return -1;
    }

    public void createHistoryTable() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Opened database successfully");

            String sql = "CREATE TABLE IF NOT EXISTS History " +
                    " (CurrencyFrom VARCHAR(20), " +
                    " Currency VARCHAR(20), " +
                    " Rate VARCHAR(20)," +
                    " TimeStamper datetime);";
            stmt.executeUpdate(sql);
            System.out.println("History Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
            System.out.println("Table not created successfully");
        }
    }

    public void createPopularCurrencyTable() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Opened database successfully");

            String sql = "CREATE TABLE IF NOT EXISTS Popular " +
                    "( Code_id VARCHAR(20));";
            stmt.executeUpdate(sql);
            System.out.println("Popular Table created successfully");

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
            System.out.println("Table not created successfully");
        }
    }

    public void insertDefaultPopularCurrencies() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Database opened successfully");

            String sql = "INSERT INTO Popular VALUES ('AUD')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Popular VALUES ('USD')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Popular VALUES ('EUR')";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Popular VALUES ('HKD')";
            stmt.executeUpdate(sql);

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
            System.out.println("Table not created successfully");
        }
    }


    public ArrayList<String> getCurrencies() {
        ArrayList<String> currencies = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Currency";

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            do {
                currencies.add(rs.getString(1));
            }while (rs.next());

//            System.out.println(currencies.toString());

            return currencies;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.out.println("Records not created successfully");
            return null;
        }
    }

    public ArrayList<String> getPopularCurrencies() {
        ArrayList<String> currencies = new ArrayList<>();
        try {
            String sql = "SELECT Code_id FROM Popular";
            ResultSet rs = stmt.executeQuery(sql);
            // rs.next();
            while (rs.next()) {
                currencies.add(rs.getString(1));
            }
            return currencies;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.out.println("Records not created successfully");
            return null;
        }

    }

//    public void getPopularTable(String sql){
//        try {
//
//        ResultSet rs = stmt.executeQuery(sql);
//        ResultSetMetaData rsmd = rs.getMetaData();
//        int columnsNumber = rsmd.getColumnCount();
//        for (int i = 1; i <= columnsNumber; i++) {
//            System.out.print(rsmd.getColumnName(i));
//            if (i != columnsNumber) {
//                System.out.print("  ");
//            }
//        }
//        System.out.print("\n");
//        while (rs.next()) {
//            for (int i = 1; i <= columnsNumber; i++) {
//                if (i > 1) System.out.print(",  ");
//                String columnValue = rs.getString(i);
//
//                if (i > 1){
//
//                    // if (checkIfChanged(rsmd.getColumnName(i),rs.getString(i)) == 1){
//                    //     columnValue += " (I)";
//                    // }
//                    // else if (checkIfChanged(rsmd.getColumnName(i),rs.getString(i)) == 1){
//                    //     columnValue += " (D)";
//                    // }
//
//                }
//
//                System.out.print(columnValue);
//                if (i != columnsNumber) {
//                    System.out.print(" ");
//                }
//            }
//            System.out.print("\n");
//        }
//
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.out.println("Records not created successfully");
//
//        }
//    }

    public void initializeNewCurrency(String name) {
        try {
            String sql = String.format("ALTER TABLE currency " +
                    "ADD %s VARCHAR(20);",name);
            stmt.executeUpdate(sql);

            System.out.println("Records created successfully");

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
            System.out.println("Records not created successfully");
        }
    }

    public void initializeRatesNewCurrency(String new_currency,String name, String rate) {
        try {
            String sql = String.format("UPDATE currency " +
                    "SET %s = '%s' WHERE code_id = '%s' ;",new_currency,rate,name);
            stmt.executeUpdate(sql);

            System.out.println("Records created successfully");

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
            System.out.println("Records not created successfully");
        }
    }

    // adding last row after adding new currency
    public void lastInsert(ArrayList<Float> rates,String new_currency) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO Currency VALUES (");
            String temp = String.format("'%s',",new_currency);
            sql.append(temp);
            for (int i=0;i<rates.size();i++){

                temp = String.format("'%s',",String.valueOf(Math.round(1/rates.get(i) * 100.0) / 100.0));


                sql.append(temp);
            }

            sql.append("'1');");
            System.out.println(sql);
            stmt.executeUpdate(sql.toString());

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
            System.out.println("Records not created successfully");
        }
    }

//    public void initialiseCurriencies() {
//        try {
//            String sql = String.format("INSERT INTO Currency " +
//                    "VALUES ('AUD', '1', '0.68','0.68','0.89','5.33','0.95');" +
//                    "INSERT INTO Currency " +
//                    "VALUES ('USD', '1.47','1','1','1.31','7.85','1.4');" +
//                    "INSERT INTO Currency " +
//                    "VALUES ('EUR', '1.47','1','1','1.31','7.85','1.4');" +
//                    "INSERT INTO Currency " +
//                    "VALUES ('CAD', '1.47','1','1','1.31','7.85','1.4');" +
//                    "INSERT INTO Currency " +
//                    "VALUES ('HKD', '1.47','1','1','1.31','7.85','1.4');" +
//                    "INSERT INTO Currency " +
//                    "VALUES ('SGD', '1.47','1','1','1.31','7.85','1.4');"
//            );
//            stmt.executeUpdate(sql);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public ResultSet executeQuery(String query) {
        try {
            return stmt.executeQuery(query);

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return null;
    }

    public void removeCurrency(String currency) {
        try {
            String query = String.format("DELETE FROM Currency WHERE code_id = '%s';", currency.toUpperCase());
            stmt.executeUpdate(query);
            query = String.format("ALTER TABLE Currency DROP Column %s;", currency.toUpperCase());
            stmt.executeUpdate(query);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void executeUpdate(String query) {
        try {
            stmt.executeUpdate(query);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public float retrieveRate(String from, String to){

        try{
            String sql = String.format("SELECT %s FROM currency WHERE Code_id = '%s'",to,from);
            ResultSet rs = stmt.executeQuery(sql);
            float rate = Float.parseFloat(rs.getString(1));

            // float roundedRate = Math.round(rate*100)/100;
            String temp = String.valueOf(Math.round(rate*100)/100.0);
            return Float.parseFloat(temp);



        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.out.println("Records not created successfully");
            return -1;
        }

    }

    public void dropTables() {
        try {
            String sql = "DROP TABLE Currency";
            stmt.executeUpdate(sql);
            sql = "DROP TABLE History";
            stmt.executeUpdate(sql);
            sql = "DROP TABLE Popular";
            stmt.executeUpdate(sql);

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

    }

    public void initialiseHistoryTable() {

        try {
            Class.forName("org.sqlite.JDBC");
            String sql =
                    """
            INSERT INTO HISTORY
            SELECT 'AUD', Code_id, AUD, datetime('now','localtime')
            FROM Currency;
            INSERT INTO HISTORY
            SELECT 'USD', Code_id, USD, datetime('now','localtime')
            FROM Currency;
            INSERT INTO HISTORY
            SELECT 'CAD', Code_id, CAD, datetime('now','localtime')
            FROM Currency;
            INSERT INTO HISTORY
            SELECT 'HKD', Code_id, HKD, datetime('now','localtime')
            FROM Currency;
            INSERT INTO HISTORY
            SELECT 'SGD', Code_id, SGD, datetime('now','localtime')
            FROM Currency;
            INSERT INTO HISTORY
            SELECT 'EUR', Code_id, EUR, datetime('now','localtime')
            FROM Currency;
            select * from History;
            delete from History Where Rate = 1;
            """;
            stmt.executeUpdate(sql);

            System.out.println("Records created successfully");



        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
            System.out.println("Records not created successfully");
        }
    }


//DEPRECATED

//    public boolean checkexists (String code){
//        String sql = String.format("SELECT COUNT(Code_id) FROM Currency WHERE Code_id = %s", code);
//        try {
//            ResultSet count = stmt.executeQuery(sql);
//            if (!count.equals("0")) {
//                return false;
//
//            } else {
//                return true;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//
//        }
//    }

//    public void displaytable (String TableName){
//        String sql = String.format("SELECT * FROM %s", TableName);
//
//        try {
//            ResultSet rs = stmt.executeQuery(sql);
//            DBTable.printResultSet(rs);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public void insertHistory(String currencyFrom, String currencyTo, String rate) {
        try {
            double r = Double.parseDouble(rate);
            String inverse = String.valueOf(1/r);

            String sql = String.format("INSERT INTO History VALUES ('%s', '%s', '%s', datetime('now','localtime'));", currencyFrom, currencyTo, rate);
            stmt.executeUpdate(sql);
            String sql2 = String.format("INSERT INTO History VALUES ('%s', '%s', '%s', datetime('now','localtime'));", currencyTo, currencyFrom, inverse);
            stmt.executeUpdate(sql2);
        } catch (Exception e) {
            System.out.println("Could not insert into History.");
        }


    }

        //returns a list of rates between 2 currencies
    public ArrayList<Float> printHistoryOfRates(String c1, String c2){

        ArrayList<Float> history = new ArrayList<>();
        // history.add((float)1.23);


        try{
            c1 = c1.toUpperCase();
            c2 = c2.toUpperCase();
            String sql = String.format("SELECT CurrencyFrom, Rate, Currency, TimeStamper FROM History WHERE CurrencyFrom = '%s' AND Currency = '%s'", c1,c2);
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++) {
                System.out.print(rsmd.getColumnName(i));
                if (i != columnsNumber) {
                    System.out.print("  ");
                }
            }

            System.out.print("\n\n");

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {

                
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);

                    // adds rates to a list
                    if (i == 2){
                       history.add(Float.parseFloat(rs.getString(i)));
                   }

                    if (i != columnsNumber) {
                        System.out.print(" ");
                    }
                }
                System.out.print("\n");
            }

            return history;

        }catch (Exception e){
            
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return null;
        }
    }


//    public ArrayList<Float>  getHistoryOfRates(String c1, String c2){

//        ArrayList<Float> history = new ArrayList<>();
//        c1 = c1.toUpperCase();
//        c2 = c2.toUpperCase();
//        try{


//            String sql = String.format("SELECT CurrencyFrom, Rate, Currency FROM History WHERE CurrencyFrom = '%s' AND Currency = '%s'", c1,c2);
//            ResultSet rs = stmt.executeQuery(sql);
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int columnsNumber = rsmd.getColumnCount();

//            while (rs.next()) {
//                for (int i = 1; i <= columnsNumber; i++) {

//                    if (i == 2){
//                        history.add(Float.parseFloat(rs.getString(i)));
//                    }
//                }
//            }
//            return history;
//        }catch (SQLException ignored){
//            return null;
//        }
//    }

    public void printHistorySummary(String currencyFrom, String currencyTo, String startDate, String endDate) {

        currencyFrom = currencyFrom.toUpperCase();
        currencyTo = currencyTo.toUpperCase();

        try {

            String ratesHistoryQuery = String.format(
                    "SELECT CurrencyFrom, Rate, Currency, TimeStamper " +
                            "FROM History " +
                            "WHERE CurrencyFrom = '%s' AND Currency = '%s' AND TimeStamper >= '%s' AND TimeStamper <= '%s';", currencyFrom, currencyTo, startDate, endDate);

            ResultSet ratesHistory = stmt.executeQuery(ratesHistoryQuery);
            ResultSetMetaData ratesHistoryMetaData = ratesHistory.getMetaData();

            // Print all historical rates between provided dates

            int numColumnsRatesHistory = ratesHistoryMetaData.getColumnCount();

            for (int i = 1; i <= numColumnsRatesHistory; i++) {
                System.out.print(ratesHistoryMetaData.getColumnName(i));
                if (i != numColumnsRatesHistory) {
                    System.out.print("  ");
                }
            }

            System.out.print("\n\n");

            while (ratesHistory.next()) {
                for (int i = 1; i <= numColumnsRatesHistory; i++) {

                    if (i > 1) System.out.print(",  ");
                    String columnValue = ratesHistory.getString(i);
                    System.out.print(columnValue);

                    if (i != numColumnsRatesHistory) {
                        System.out.print(" ");
                    }
                }
                System.out.print("\n");
            }

            System.out.println();

            printSummary(currencyFrom, currencyTo, startDate, endDate);

        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void printSummary(String currencyFrom, String currencyTo, String startDate, String endDate) {

        currencyFrom = currencyFrom.toUpperCase();
        currencyTo = currencyTo.toUpperCase();

        try {
            String ratesSummaryQuery = String.format(
                    "SELECT AVG(Rate), MAX(Rate), MIN(Rate)" +
                            "FROM History " +
                            "WHERE CurrencyFrom = '%s' AND Currency = '%s' AND TimeStamper >= '%s' AND TimeStamper <= '%s';", currencyFrom, currencyTo, startDate, endDate);

            ResultSet ratesSummary = stmt.executeQuery(ratesSummaryQuery);
            ResultSetMetaData ratesSummaryMetaData = ratesSummary.getMetaData();

            int numColumnsRatesSummary = ratesSummaryMetaData.getColumnCount();

            for (int i = 1; i <= numColumnsRatesSummary; i++) {
                System.out.print(ratesSummaryMetaData.getColumnName(i));
                if (i != numColumnsRatesSummary) {
                    System.out.print("  ");
                }
            }

            System.out.print("\n\n");

            while (ratesSummary.next()) {
                for (int i = 1; i <= numColumnsRatesSummary; i++) {

                    if (i > 1) System.out.print(",  ");
                    String columnValue = ratesSummary.getString(i);
                    System.out.print(columnValue);

                    if (i != numColumnsRatesSummary) {
                        System.out.print(" ");
                    }
                }
                System.out.print("\n");
            }

        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

    }


//DEPRECATED

//    public ArrayList<Float>  getHistoryOfRates(String c1, String c2){
//
//        ArrayList<Float> history = new ArrayList<>();
//        c1 = c1.toUpperCase();
//        c2 = c2.toUpperCase();
//        try{
//
//
//            String sql = String.format("SELECT CurrencyFrom, Rate, Currency FROM History WHERE CurrencyFrom = '%s' AND Currency = '%s'", c1,c2);
//            ResultSet rs = stmt.executeQuery(sql);
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int columnsNumber = rsmd.getColumnCount();
//
//            while (rs.next()) {
//                for (int i = 1; i <= columnsNumber; i++) {
//
//                    if (i == 2){
//                        history.add(Float.parseFloat(rs.getString(i)));
//                    }
//                }
//            }
//            return history;
//        }catch (SQLException ignored){
//            return null;
//        }
        // ResultSetMetaData rsmd = rs.getMetaData();
        // int RowNumber = rsmd.getRowCount();
        // System.out.println(RowNumber);
//    }


//DEPRECATED

//    public int checkIfChanged(String c1, String c2) {
//
//
//        ArrayList<Float> rates = new ArrayList();
//        rates = getHistoryOfRates(c1, c2);
//        if (rates.size() > 0) {
//            float currentRate = rates.get(rates.size() - 1);
//            float previousRate = -1;
//            if (rates.size() >= 2) {
//                previousRate = rates.get(rates.size() - 2);
//            } else {
//                return 0;
//            }
//
//            if (currentRate > previousRate) {
//                return 1;
//            } else if (currentRate < previousRate) {
//                return -1;
//            } else {
//                return 0;
//            }
//        } else {
//            return 0;
//        }
//        // 0 if no change, 1 if its higher, -1 if its lower
//    }

    public int getResultSetCount(ResultSet rs) throws SQLException {
        int size = 0;
        while (rs.next()) {
            size++;
        }
        return size;
    }
}
