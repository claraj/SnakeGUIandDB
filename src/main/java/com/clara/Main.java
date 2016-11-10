package com.clara;

import java.sql.*;

public class Main {

    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static String DB_NAME = "snakes";           //todo create a database called snakes
    private static final String USER = "yourname";      //todo change to your own username
    private static final String PASSWORD = "yourpass";      //todo change to your own password

    //TODO Grant select, insert, create and drop to your user
    //Execute a command like this in your mysql shell for your own user
    // grant create, select, insert, drop on snakes to 'clara'@'localhost'

    static Connection connection = null;
    static Statement statement = null;
    static ResultSet rs = null;

    public static void main(String[] args) {

        //(If needed) create database and add sample data

        boolean setupOk = setup();

        if (!setupOk) {
            shutdown();
        }

        try {

            SnakeModel snakeModel = new SnakeModel(rs);
            //Create and show the GUI
            SnakeInfoGUI tableGUI = new SnakeInfoGUI(snakeModel);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    private static boolean setup() {

        try {
            String Driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(Driver);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No database drivers found. Quitting");
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD)) {
            connection = conn;

        } catch (SQLException sqle) {
            System.out.println("Can't connect to database. " +
                    "\nIs MySQL running? " +
                    "\nHave you created the database? " +
                    "\nVerify username and password. " +
                    "\nHave you granted the right permissions to your user?");
            sqle.printStackTrace();
            return false;
        }


        try {

            // Create a Statement.
            // The first argument allows us to move both forward and backwards through the ResultSet generated from this Statement.
            // The TableModel will need to do this.
            // by default, you can only move forward and you can only do one pass through the result set
            // This is you'll do most of the time, and it's less resource-intensive than being able to
            // go in both directions; and the database can identify when you are done
            // If you set one argument, you need the other. The second one means you will
            // not be modifying the data in the RowSet (we'll change this later though)

            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);


            // Create a table in the database. Using test data that we will delete and re-create every time the app runs
            // Drop (delete) the table if it already exists...

            statement.execute("DROP TABLE IF EXISTS Snakes");

            //Simple two-column table.
            String createTableSQL = "CREATE TABLE Snakes (species varchar(30), venom int)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Created Snakes table");

            //Add some example data
            statement.execute("INSERT INTO Snakes VALUES ('Cobra', 5)");
            statement.execute("INSERT INTO Snakes VALUES ('Boa Constrictor', 0)");
            statement.execute("INSERT INTO Snakes VALUES ('Python', 7)");

            System.out.println("Added three rows of data");


            // Run a query to fetch all of the data from the database.
            // The ResultSet will be used with the JTable in the GUI.

            String getAllData = "SELECT * FROM Snakes";
            rs = statement.executeQuery(getAllData);

        } catch (SQLException sqle) {

            System.out.println("Error during setup");
            sqle.printStackTrace();
            return false;

        }

        return true;   //At this point, seems everything worked.

    }

    public static void shutdown() {

        //Close resources - ResultSet, statement, connection - and tidy up whether this code worked or not.

        //Close ResultSet...
        try {
            if (rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        //And then the statement....
        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        //And then the connection
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        //And quit the program
        System.exit(0);
    }
}
