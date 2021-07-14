package Application;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SQLiteDBHandler {

    private static Connection connection = null;
    private DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, u");
    private Statement statement = null;



    public static final String EMPLOYEEINFORMATION_TABLE = "EmployeeInformation";
    public static final String EMPLOYEEINFORMMATION_EMPLOYEENAME_COLUMN = "employeeName";
    public static final String EMPLOYEEINFORMATION_EMPLOYEEMANAGER_COLUMN = "employeeManager";
    public static final String POINTS_TABLE = "Points";
    public static final String POINTS_EMPLOYEENAME_COLUMN = "employeeName";
    public static final String POINTS_RECEIVEDDATE_COLUMN = "receivedDate";
    public static final String POINTS_RECEIVEDAMOUNT_COLUMN = "receivedAmount";
    public static final String POINTS_MANAGERCOMMENTS_COLUMN = "managerComments";

    public static Connection openConnection(){

        if(connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:EmployeeData.db");

            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to connect to DB");
            }
        }

        return connection;
    }

    //Need to remove hardcoding for method
    public void wipeDBTables(Connection connection) {
        try {
            statement = connection.createStatement();

            statement.execute("DROP TABLE " + EMPLOYEEINFORMATION_TABLE);
            statement.execute("DROP TABLE  " + POINTS_TABLE);

            statement.execute("CREATE TABLE EmployeeInformation (" +
                    "employeeName TEXT NOT NULL, " +
                    "employeeManager TEXT NOT NULL);");

            statement.execute("CREATE TABLE Points (" +
                    "employeeName TEXT NOT NULL, " +
                    "receivedDate TEXT NOT NULL, " +
                    "receivedAmount INTEGER NOT NULL, " +
                    "managerComments TEXT);");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
/*
        finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
*/
    }

    public void writeEmployeesToDB(Connection connection, Employee employee) {
        try {
            statement = connection.createStatement();

            statement.execute("INSERT INTO " + EMPLOYEEINFORMATION_TABLE +
                    " VALUES(" + "'" + employee.getEmployeeName() + "', '" + employee.getEmployeeManager() + "');");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
/*
        finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to close statement");
            }
        }
*/
    }

    public void writeEmployeePointsToDB(Connection connection, Employee employee, Points points) {
        try {
            statement = connection.createStatement();

            statement.execute("INSERT INTO " + POINTS_TABLE + " VALUES(" + "'" +
                    employee.getEmployeeName() + "', " + "'" + points.getReceivedDateAsString() + "', " +
                    points.getAmount() + ", " + "'" + points.getManagerComment() + "');");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
/*
        finally {
            try {
                statement.close();
            } catch ( SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to close statement");
            }
        }
*/
    }

    public ObservableList<Employee> readEmployeesFromDB(Connection connection) {
        ObservableList<Employee> retrievedEmployeeObservableList;

        try {
            statement = connection.createStatement();
            retrievedEmployeeObservableList = FXCollections.observableArrayList();
            ResultSet retrievedEmployeeInformationTable = statement.executeQuery("SELECT * FROM EmployeeInformation;");

            while(retrievedEmployeeInformationTable.next()) {
                String employeeName = retrievedEmployeeInformationTable.getString(EMPLOYEEINFORMMATION_EMPLOYEENAME_COLUMN);
                String employeeManager = retrievedEmployeeInformationTable.getString(EMPLOYEEINFORMATION_EMPLOYEEMANAGER_COLUMN);
                Employee employeeToAdd = new Employee(employeeName, employeeManager);

                retrievedEmployeeObservableList.add(employeeToAdd);
            }

            return retrievedEmployeeObservableList;

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Unable to get result set");
            return null;
        }
/*
        finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to close statement");
            }
        }
*/
    }

    public void readEmployeePointsFromDB(Connection connection, Employee employee) {
        try {
            statement = connection.createStatement();
            ResultSet retrievedPointsTable = statement.executeQuery(
                    "SELECT * FROM Points WHERE '" + employee.getEmployeeName()+ "';");

            System.out.println(employee.getEmployeeName());
            System.out.println(retrievedPointsTable.toString());

            while(retrievedPointsTable.next()) {
                String receivedDateString = retrievedPointsTable.getString(POINTS_RECEIVEDDATE_COLUMN);
                String receivedAmountString = retrievedPointsTable.getString(POINTS_RECEIVEDAMOUNT_COLUMN);
                String managerComments = retrievedPointsTable.getString(POINTS_MANAGERCOMMENTS_COLUMN);

                LocalDate receivedDate = LocalDate.parse(receivedDateString, inputDateFormatter);
                int receivedAmountInt = Integer.valueOf(receivedAmountString);

                System.out.println(receivedAmountString + receivedAmountString + managerComments);

                employee.addPoints(receivedAmountInt, receivedDate, managerComments);
            }

        } catch(SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to read Points from DB");
        }
    }

    public boolean closeConnection() {
        try {
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Unable to close connection to DB");
            return false;
        }
        return true;
    }

}
