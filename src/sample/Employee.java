package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class Employee implements Serializable {

    private SimpleStringProperty employeeName;
    private SimpleStringProperty employeeManager;
    private SimpleStringProperty nextFallOffDate;
    private SimpleStringProperty nextFallOffAmountString;
    private SimpleIntegerProperty totalPointsInteger;

    private SimpleDoubleProperty totalPaidSickDaysUsed;
    private SimpleDoubleProperty totalUnpaidSickDaysUsed;

    private ObservableList<Points> pointsObservableList;
    private ObservableList<PaidSickDay> paidSickDayObservableList;
    private ObservableList<UnpaidSickDay> unpaidSickDayObservableList;

    private FallOffDateComparator fallOffDateComparator;
    private DateTimeFormatter dateTimeFormatter;


    public Employee(String name, String manager) {
        this.employeeName = new SimpleStringProperty();
        this.employeeName.set(name);
        this.employeeManager = new SimpleStringProperty();
        this.employeeManager.set(manager);
        this.nextFallOffDate = new SimpleStringProperty();
        this.nextFallOffAmountString = new SimpleStringProperty();

        this.fallOffDateComparator = new FallOffDateComparator();

        this.totalPointsInteger = new SimpleIntegerProperty();

        this.totalPaidSickDaysUsed = new SimpleDoubleProperty();

        this.totalUnpaidSickDaysUsed = new SimpleDoubleProperty();

        this.dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, u");

        //observable lists
        this.pointsObservableList = FXCollections.observableArrayList();
        this.paidSickDayObservableList = FXCollections.observableArrayList();
        this.unpaidSickDayObservableList = FXCollections.observableArrayList();

    }

    public void addPoints(int amount, LocalDate receivedDate, String comment) {
        Points pointsToBeAdded = new Points(amount, receivedDate, comment);
        pointsObservableList.add(pointsToBeAdded);
        setFallOffs();
        setPointTotals();
    }

    public void removePoints(Points pointsToBeRemoved) {
        pointsObservableList.remove(pointsToBeRemoved);
        setFallOffs();
        setPointTotals();
    }

    public void addPaidSickDay(double amount, LocalDate date, String comment) {
        PaidSickDay newPaidSickDay = new PaidSickDay(amount, date, comment);
        paidSickDayObservableList.add(newPaidSickDay);
        setSickDaysTotal();
    }

    public void addUnpaidSickDay(double amount, LocalDate date, String comment) {
        UnpaidSickDay newUnpaidSickDay = new UnpaidSickDay(amount, date, comment);
        unpaidSickDayObservableList.add(newUnpaidSickDay);
        setSickDaysTotal();
    }

    public void setFallOffs() {
        Collections.sort(pointsObservableList, fallOffDateComparator);

        if(pointsObservableList.isEmpty()) {
            this.nextFallOffDate.set(null);
            this.nextFallOffAmountString.set(null);
        } else {
            int earliestDate = 0;
            this.nextFallOffDate.set(pointsObservableList.get(earliestDate).getFallOffDate().format(dateTimeFormatter).toString());
            this.nextFallOffAmountString.set(String.valueOf(pointsObservableList.get(earliestDate).getAmount()));
        }
    }

    public void setPointTotals() {
        int sumOfPoints = 0;
        for(int i = 0; i<pointsObservableList.size(); i++) {
            sumOfPoints = pointsObservableList.get(i).getAmount() + sumOfPoints;
        }

        this.totalPointsInteger.set(sumOfPoints);
    }

    public void setSickDaysTotal() {
        double sumOfPaidSickDays = 0;
        for(int i = 0; i<paidSickDayObservableList.size(); i++) {
            sumOfPaidSickDays = paidSickDayObservableList.get(i).getAmountUsed() + sumOfPaidSickDays;
        }
        this.totalPaidSickDaysUsed.set(sumOfPaidSickDays);

        double sumOfUnpaidSickDays = 0;
        for(int i = 0; i<unpaidSickDayObservableList.size(); i++) {
            sumOfUnpaidSickDays = unpaidSickDayObservableList.get(i).getAmountUsed() + sumOfUnpaidSickDays;
        }
        this.totalUnpaidSickDaysUsed.set(sumOfUnpaidSickDays);
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public SimpleStringProperty employeeNameProperty() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }

    public String getEmployeeManager() {
        return employeeManager.get();
    }

    public SimpleStringProperty employeeManagerProperty() {
        return employeeManager;
    }

    public void setEmployeeManager(String employeeManager) {
        this.employeeManager.set(employeeManager);
    }

    public String getNextFallOffDate() {
        return nextFallOffDate.get();
    }

    public SimpleStringProperty nextFallOffDateProperty() {
        return nextFallOffDate;
    }

    public void setNextFallOffDate(String nextFallOffDate) {
        this.nextFallOffDate.set(nextFallOffDate);
    }

    public String getNextFallOffAmountString() {
        return nextFallOffAmountString.get();
    }

    public SimpleStringProperty nextFallOffAmountStringProperty() {
        return nextFallOffAmountString;
    }

    public void setNextFallOffAmountString(String nextFallOffAmountString) {
        this.nextFallOffAmountString.set(nextFallOffAmountString);
    }

    public int getTotalPointsInteger() {
        return totalPointsInteger.get();
    }

    public SimpleIntegerProperty totalPointsIntegerProperty() {
        return totalPointsInteger;
    }

    public ObservableList<Points> getPointsObservableList() {
        return pointsObservableList;
    }

    public ObservableList<PaidSickDay> getPaidSickDayObservableList() {
        return paidSickDayObservableList;
    }

    public ObservableList<UnpaidSickDay> getUnpaidSickDayObservableList() {
        return unpaidSickDayObservableList;
    }

    public double getTotalPaidSickDaysUsed() {
        return totalPaidSickDaysUsed.get();
    }

    public SimpleDoubleProperty totalPaidSickDaysUsedProperty() {
        return totalPaidSickDaysUsed;
    }

    public double getTotalUnpaidSickDaysUsed() {
        return totalUnpaidSickDaysUsed.get();
    }

    public SimpleDoubleProperty totalUnpaidSickDaysUsedProperty() {
        return totalUnpaidSickDaysUsed;
    }


    @Override
    public String toString() {
        return employeeName.get();
    }

}
