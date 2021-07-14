package Application;

import java.sql.*;

public class SQLiteDBHandler {

    Connection connection;
    Statement statement;

    public static final String EMPLOYEEINFORMATION_TABLE = "EmployeeInformation";
    public static final String POINTS_TABLE = "Points";

    public Statement connectionOpen() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:EmployeeData.db");
            statement = connection.createStatement();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Unable to connect to DB");
            return null;
        }
        return statement;
    }

    public void writeEmployeesToDB(Statement statement, Employee employee) {

        try {
            statement.execute("INSERT INTO " + EMPLOYEEINFORMATION_TABLE +
                    " VALUES(" + "'" + employee.getEmployeeName() + "', '" + employee.getEmployeeManager() + "');");

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

    public void writeEmployeePointsToDB(Statement statement, Employee employee, Points points) {

        try {
            statement.execute("INSERT INTO " + POINTS_TABLE + " VALUES(" + "'" +
                    employee.getEmployeeName() + "', " + "'" + points.getReceivedDateAsString() + "', " +
                    points.getAmount() + ", " + "'" + points.getManagerComment() + "');");

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch ( SQLException exception) {
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
