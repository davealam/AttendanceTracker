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
    public static final String POINTS_RECEIVEDDATE_COLUMN = "receivedDate";
    public static final String POINTS_RECEIVEDAMOUNT_COLUMN = "receivedAmount";
    public static final String POINTS_MANAGERCOMMENTS_COLUMN = "managerComments";

    public static final String PAIDSICKDAYS_TABLE = "PaidSickDays";
    public static final String PAIDSICKDAYS_USAGEDATE_COLUMN = "usageDate";
    public static final String PAIDSICKDAYS_USAGEAMOUNT_COLUMN = "usageAmount";
    public static final String PAIDSICKDAYS_MANAGERCOMMENTS_COLUMN = "managerComments";

    public static final String UNPAIDSICKDAYS_TABLE = "UnpaidSickDays";
    public static final String UNPAIDSICKDAYS_USAGEDATE_COLUMN = "usageDate";
    public static final String UNPAIDSICKDAYS_USAGEAMOUNT_COLUMN = "usageAmount";
    public static final String UNPAIDSICKDAYS_MANAGERCOMMENTS_COLUMN = "managerComments";


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
            statement.execute("DROP TABLE " + PAIDSICKDAYS_TABLE);
            statement.execute("DROP TABLE " + UNPAIDSICKDAYS_TABLE);

            statement.execute("CREATE TABLE EmployeeInformation (" +
                    "employeeName TEXT NOT NULL, " +
                    "employeeManager TEXT NOT NULL);");

            statement.execute("CREATE TABLE Points (" +
                    "employeeName TEXT NOT NULL, " +
                    "receivedDate TEXT NOT NULL, " +
                    "receivedAmount INTEGER NOT NULL, " +
                    "managerComments TEXT);");

            statement.execute("CREATE TABLE PaidSickDays (" +
                    "employeeName TEXT NOT NULL, " +
                    "usageDate TEXT NOT NULL, " +
                    "usageAmount INTEGER NOT NULL, " +
                    "managerComments TEXT);");

            statement.execute("CREATE TABLE UnpaidSickDays (" +
                    "employeeName TEXT NOT NULL, " +
                    "usageDate TEXT NOT NULL, " +
                    "usageAmount INTEGER NOT NULL, " +
                    "managerComments TEXT);");
            
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
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

    public void writePointsToDB(Connection connection, Employee employee, Points points) {
        try {
            statement = connection.createStatement();

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

    public void writePaidSickDaysToDB(Connection connection, Employee employee, PaidSickDay paidSickDay) {
        try {
            statement = connection.createStatement();

            statement.execute("INSERT INTO " + PAIDSICKDAYS_TABLE + " VALUES(" + "'" +
                    employee.getEmployeeName() + "', " + "'" + paidSickDay.getDateUsed() + "', " +
                    paidSickDay.getAmountUsed() + ", " + "'" + paidSickDay.getManagerComment() + "');");

        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void writeUnpaidSickDaysToDB(Connection connection, Employee employee, UnpaidSickDay unpaidSickDay) {
        try {
            statement = connection.createStatement();

            statement.execute("INSERT INTO " + UNPAIDSICKDAYS_TABLE + " VALUES(" + "'" +
                    employee.getEmployeeName() + "', " + "'" + unpaidSickDay.getDateUsed() + "', " +
                    unpaidSickDay.getAmountUsed() + ", " + "'" + unpaidSickDay.getManagerComment() + "');");

        }catch (SQLException exception) {
            exception.printStackTrace();
        }

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
        } finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to close statement");
            }
        }
    }

    public void readPointsFromDB(Connection connection, Employee employee) {
        try {
            statement = connection.createStatement();
            ResultSet retrievedPointsTable = statement.executeQuery(
                    "SELECT * FROM " + POINTS_TABLE + " WHERE employeeName = '" + employee.getEmployeeName()+ "';");

            while(retrievedPointsTable.next()) {

                String receivedDateString = retrievedPointsTable.getString(POINTS_RECEIVEDDATE_COLUMN);
                String receivedAmountString = retrievedPointsTable.getString(POINTS_RECEIVEDAMOUNT_COLUMN);
                String managerComments = retrievedPointsTable.getString(POINTS_MANAGERCOMMENTS_COLUMN);

                LocalDate receivedDate = LocalDate.parse(receivedDateString, inputDateFormatter);
                int receivedAmountInt = Integer.valueOf(receivedAmountString);

                employee.addPoints(receivedAmountInt, receivedDate, managerComments);
            }

        } catch(SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to read Points from DB");
        } finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to close statement");
            }
        }
    }

    public void readPaidSickDaysFromDB(Connection connection, Employee employee) {
        try {
            statement = connection.createStatement();
            ResultSet retrievedPaidSickDaysTable = statement.executeQuery(
                    "SELECT * FROM " + PAIDSICKDAYS_TABLE + " WHERE employeeName = '" + employee.getEmployeeName()+ "';");

            while(retrievedPaidSickDaysTable.next()) {

                String usageDateString = retrievedPaidSickDaysTable.getString(PAIDSICKDAYS_USAGEDATE_COLUMN);
                String usageAmountString = retrievedPaidSickDaysTable.getString(PAIDSICKDAYS_USAGEAMOUNT_COLUMN);
                String managerComments = retrievedPaidSickDaysTable.getString(PAIDSICKDAYS_MANAGERCOMMENTS_COLUMN);

                LocalDate receivedDate = LocalDate.parse(usageDateString, inputDateFormatter);
                double usageAmountDouble = Double.valueOf(usageAmountString);

                employee.addPaidSickDay(usageAmountDouble, receivedDate, managerComments);
            }

        } catch(SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to read PaidSickDays from DB");
        } finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to close statement");
            }
        }
    }

    public void readUnpaidSickDaysFromDB(Connection connection, Employee employee) {
        try {
            statement = connection.createStatement();
            ResultSet retrievedUnpaidSickDaysTable = statement.executeQuery(
                    "SELECT * FROM " + UNPAIDSICKDAYS_TABLE + " WHERE employeeName = '" + employee.getEmployeeName()+ "';");

            while(retrievedUnpaidSickDaysTable.next()) {

                String usageDateString = retrievedUnpaidSickDaysTable.getString(UNPAIDSICKDAYS_USAGEDATE_COLUMN);
                String usageAmountString = retrievedUnpaidSickDaysTable.getString(UNPAIDSICKDAYS_USAGEAMOUNT_COLUMN);
                String managerComments = retrievedUnpaidSickDaysTable.getString(UNPAIDSICKDAYS_MANAGERCOMMENTS_COLUMN);

                LocalDate receivedDate = LocalDate.parse(usageDateString, inputDateFormatter);
                double usageAmountDouble = Double.valueOf(usageAmountString);

                employee.addUnpaidSickDay(usageAmountDouble, receivedDate, managerComments);
            }

        } catch(SQLException exception) {
            exception.printStackTrace();
            System.out.println("Failed to read PaidSickDays from DB");
        } finally {
            try {
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.out.println("Unable to close statement");
            }
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
