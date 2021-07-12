package Application;

import java.sql.*;

public class SQLiteDBHandler {

    Connection connection;
    public static final String EMPLOYEEINFORMATION_TABLE = "EmployeeInformation";

    public boolean connectionOpen() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:EmployeeData.db");
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Unable to connect to DB");
            return false;
        }
        return true;
    }

    public void writeEmployeeToDB(Employee employee) {

        Statement statement = null;

        try {

            statement = connection.createStatement();

            statement.execute("INSERT INTO " + EMPLOYEEINFORMATION_TABLE + " VALUES(" + "'" + employee.getEmployeeName() + "', '" + employee.getEmployeeManager() + "');");

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to close statement");
            }
        }

    }

    public boolean connectionClose() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Unable to close connection to DB");
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler();
        sqLiteDBHandler.connectionOpen();
    }

}
