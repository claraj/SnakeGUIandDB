package com.clara;

import java.sql.*;

public class Main {

    public static void main(String[] args) {

        //(If needed) create database and add sample data

        ResultSet resultSet = Database.setup();

        if (resultSet == null) {
            Database.shutdown();
            System.exit(-1);
        }

        try {

            // Create a model for the JTable
            SnakeModel snakeModel = new SnakeModel(resultSet);

            //Create and show the GUI
            SnakeInfoGUI tableGUI = new SnakeInfoGUI(snakeModel);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public static void shutdown() {
        Database.shutdown();
        //And quit the program
        System.exit(0);
    }
}
