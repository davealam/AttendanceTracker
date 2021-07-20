package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeRepository {

    private static EmployeeRepository instance = new EmployeeRepository();
    private DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ObservableList<Employee> employeeObservableList;

    public static EmployeeRepository getInstance() {
        return instance;
    }

    private EmployeeRepository() {

        employeeObservableList = FXCollections.observableArrayList();

    }

    public ObservableList<Employee> getEmployeeObservableList() {
        return employeeObservableList;
    }

    public void addEmployee(String employeeName, String managerName) {
        Employee employeeToAdd = new Employee(employeeName, managerName);
        employeeObservableList.add(employeeToAdd);
    }

    public void saveToSQLiteDB() {
        Connection connection = SQLiteDBHandler.openConnection();

        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler();

        sqLiteDBHandler.wipeDBTables(connection);

        for(Employee employee : employeeObservableList) {
            ObservableList<Points> twentyFourMonthRollingPointsObservableList =
                    employee.getTwentyFourMonthRollingPointsObservableList();
            ObservableList<PaidSickDay> paidSickDayObservableList = employee.getPaidSickDayObservableList();
            ObservableList<UnpaidSickDay> unpaidSickDayObservableList = employee.getUnpaidSickDayObservableList();

            sqLiteDBHandler.writeEmployeesToDB(connection, employee);

            for(Points points : twentyFourMonthRollingPointsObservableList) {
                sqLiteDBHandler.writePointsToDB(connection, employee, points);
            }

            for(PaidSickDay paidSickDay : paidSickDayObservableList) {
                sqLiteDBHandler.writePaidSickDaysToDB(connection, employee, paidSickDay);
            }

            for(UnpaidSickDay unpaidSickDay : unpaidSickDayObservableList) {
                sqLiteDBHandler.writeUnpaidSickDaysToDB(connection, employee, unpaidSickDay);
            }
        }
        sqLiteDBHandler.closeConnection();
    }

    public void readFromSQLiteDB() {
        Connection connection = SQLiteDBHandler.openConnection();
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler();

        this.employeeObservableList = sqLiteDBHandler.readEmployeesFromDB(connection);

        for(Employee employee : employeeObservableList) {
            sqLiteDBHandler.readPointsFromDB(connection, employee);
            sqLiteDBHandler.readPaidSickDaysFromDB(connection, employee);
            sqLiteDBHandler.readUnpaidSickDaysFromDB(connection, employee);
        }
    }
}
