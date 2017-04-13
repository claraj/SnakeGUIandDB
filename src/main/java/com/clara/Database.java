package com.clara;

import javax.xml.transform.Result;
import java.sql.*;

/**
 * Created by admin on 4/12/17.
 */
public class Database {

    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static String DB_NAME = "snakes";       // todo create the snakes database
    private static final String USER = "clara";   //todo change to your user
    private static final String PASS = System.getenv("MYSQL_PW");  // TODO make sure this environment variable is set

    //TODO Grant select, insert, create and drop to your user
    //Execute a command like this in your mysql shell for your own user
    // grant create, select, insert, drop on snakes.* to 'clara'@'localhost'

    private static Connection conn = null;
    private static Statement statement = null;
    private static ResultSet rs = null;


    // Connect to the database and return a ResultSet containing all of the data.
    static ResultSet setup() {

        try {
            String Driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(Driver);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No database drivers found. Quitting");
            return null;
        }

        try {
            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASS);

        } catch (SQLException sqle) {
            System.out.println("Can't connect to database. " +
                    "\nIs MySQL running? " +
                    "\nHave you created the database? " +
                    "\nVerify username and password. " +
                    "\nHave you granted the right permissions to your user?");
            sqle.printStackTrace();
            return null;
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

            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);


            // Create a table in the database. Using test data that we will delete and re-create every time the app runs
            // You probably won't do this for a real app, since you'll lose all of your data - only if you
            // want to start with a fresh set of data or are using temporary data.
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
            return null;

        }

        return rs;   //At this point, seems everything worked. Return the ResultSet.

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
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }


    }
}
