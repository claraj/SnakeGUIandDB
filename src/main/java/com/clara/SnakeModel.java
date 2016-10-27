package com.clara;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SnakeModel extends AbstractTableModel{

    ResultSet resultSet;
    int numberOfRows;
    int numberOfColumns;

    //Constructor - use a ResultSet to work out how many rows and columns we have.
    SnakeModel(ResultSet rs) {
        this.resultSet = rs;
        //Figure out number of rows in the ResultSet
        try {

            //Calculate number of rows. Have to loop over result set and count the number of iterations

            numberOfRows = 0;
            while (resultSet.next()) {
                numberOfRows++;
            }

            //But can query the ResultSet to get the number of columns
            numberOfColumns = resultSet.getMetaData().getColumnCount();

            System.out.println("number of rows " + numberOfRows + ", number of columns " + numberOfColumns);

        } catch (SQLException sqle) {
            System.out.println("Error setting up data model");
            sqle.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return numberOfRows;
    }

    @Override
    public int getColumnCount() {
        return numberOfColumns;
    }

    @Override
    //Fetch value for the cell at (row, col).
    //The table will call toString on the object, so it's a good idea
    //to return a String or something that implements toString in a useful way
    //In this code, we have varchar (String) and int, so they will display as expected.
    public String getValueAt(int row, int col) {
        try {
            //Move to this row in the result set. Rows are numbered 1, 2, 3...
            resultSet.absolute(row + 1);
            //And get the column at this row. Columns numbered 1, 2, 3...
            Object o = resultSet.getObject(col + 1);
            return o.toString();
        } catch (SQLException sqle) {
            //Display the text of the error message in the cell
            return sqle.toString();
        }


    }
}


