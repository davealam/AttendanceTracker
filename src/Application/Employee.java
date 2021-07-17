package Application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class Employee {

    private SimpleStringProperty employeeName;
    private SimpleStringProperty employeeManager;

    private SimpleStringProperty nextTwelveMonthRollingFallOffDate;
    private SimpleStringProperty nextTwelveMonthFallOffAmountString;
    private SimpleIntegerProperty twelveMonthRollingTotalPointsInteger;
    private SimpleStringProperty nextTwentyFourMonthRollingFallOffDate;
    private SimpleIntegerProperty twentyFourMonthRollingTotalPointsInteger;

    private SimpleDoubleProperty totalPaidSickDaysUsed;
    private SimpleDoubleProperty totalUnpaidSickDaysUsed;

    private ObservableList<Points> twelveMonthRollingPointsObservableList;
    private ObservableList<Points> twentyFourMonthRollingPointsObservableList;
    private ObservableList<PaidSickDay> paidSickDayObservableList;
    private ObservableList<UnpaidSickDay> unpaidSickDayObservableList;

    private FallOffDateComparator fallOffDateComparator;
    private DateTimeFormatter dateTimeFormatter;


    public Employee(String name, String manager) {
        this.employeeName = new SimpleStringProperty();
        this.employeeName.set(name);

        this.employeeManager = new SimpleStringProperty();
        this.employeeManager.set(manager);

        this.nextTwelveMonthRollingFallOffDate = new SimpleStringProperty();
        this.nextTwelveMonthFallOffAmountString = new SimpleStringProperty();
        this.nextTwentyFourMonthRollingFallOffDate = new SimpleStringProperty();

        this.fallOffDateComparator = new FallOffDateComparator();

        this.twelveMonthRollingTotalPointsInteger = new SimpleIntegerProperty();
        this.twentyFourMonthRollingTotalPointsInteger = new SimpleIntegerProperty();

        this.totalPaidSickDaysUsed = new SimpleDoubleProperty();

        this.totalUnpaidSickDaysUsed = new SimpleDoubleProperty();

        this.dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, u");

        //observable lists
        this.twelveMonthRollingPointsObservableList = FXCollections.observableArrayList();
        this.twentyFourMonthRollingPointsObservableList = FXCollections.observableArrayList();
        this.paidSickDayObservableList = FXCollections.observableArrayList();
        this.unpaidSickDayObservableList = FXCollections.observableArrayList();

    }

    //Removes expired points by checking each point received date vs current date
    public void removeExpiredPoints() {
        for(int i = 0; i< twelveMonthRollingPointsObservableList.size(); i++) {
            if(twelveMonthRollingPointsObservableList.get(i).getTwelveMonthFallOffDate().isBefore(LocalDate.now())) {

                Points twelveMonthRollingPointsToBeRemoved = twelveMonthRollingPointsObservableList.get(i);
                twelveMonthRollingPointsToBeRemoved.setTwelveMonthRollingFallOffDateAsString(null);
                removePointsFromTwelveMonthRollingList(twelveMonthRollingPointsToBeRemoved);
            }
        }

        for(int i = 0; i< twentyFourMonthRollingPointsObservableList.size(); i++) {
            if(twentyFourMonthRollingPointsObservableList.get(i).getTwentyFourMonthFallOffDate().isBefore(LocalDate.now())) {

                Points twentyFourMonthRollingPointsToBeRemoved = twentyFourMonthRollingPointsObservableList.get(i);
                removePointsFromTwentyFourMonthRollingList(twentyFourMonthRollingPointsToBeRemoved);
            }
        }

    }

    public void addPoints(int amount, LocalDate receivedDate, String comment) {
        Points pointsToBeAdded = new Points(amount, receivedDate, comment);
        twelveMonthRollingPointsObservableList.add(pointsToBeAdded);
        twentyFourMonthRollingPointsObservableList.add(pointsToBeAdded);
        setFallOffs();
        setPointTotals();
    }

    public void removePointsFromTwelveMonthRollingList(Points pointsToBeRemoved) {
        twelveMonthRollingPointsObservableList.remove(pointsToBeRemoved);
        setFallOffs();
        setPointTotals();
    }

    public void removePointsFromTwentyFourMonthRollingList(Points pointsToBeRemoved) {
        twentyFourMonthRollingPointsObservableList.remove(pointsToBeRemoved);
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
        Collections.sort(twelveMonthRollingPointsObservableList, fallOffDateComparator);

        if(twelveMonthRollingPointsObservableList.isEmpty()) {
            this.nextTwelveMonthRollingFallOffDate.set(null);
            this.nextTwelveMonthFallOffAmountString.set(null);
        } else {
            int earliestDate = 0;
            this.nextTwelveMonthRollingFallOffDate.set(twelveMonthRollingPointsObservableList.
                    get(earliestDate).getTwelveMonthFallOffDate().format(dateTimeFormatter).toString());
            this.nextTwelveMonthFallOffAmountString.set(String.valueOf(twelveMonthRollingPointsObservableList.
                    get(earliestDate).getAmount()));
        }

        if(twentyFourMonthRollingPointsObservableList.isEmpty()) {
            System.out.println("24 MONTH LIST IS EMPTY");
            this.nextTwentyFourMonthRollingFallOffDate.set(null);
        } else {
            int earliestDate = 0;
            this.nextTwentyFourMonthRollingFallOffDate.set(twentyFourMonthRollingPointsObservableList.
                    get(earliestDate).getTwentyFourMonthFallOffDate().format(dateTimeFormatter).toString());
        }

    }

    public void setPointTotals() {
        int sumOfTwelveMonthRollingPoints = 0;
        for(int i = 0; i< twelveMonthRollingPointsObservableList.size(); i++) {
            sumOfTwelveMonthRollingPoints = twelveMonthRollingPointsObservableList.get(i).getAmount() + sumOfTwelveMonthRollingPoints;
        }

        this.twelveMonthRollingTotalPointsInteger.set(sumOfTwelveMonthRollingPoints);

        int sumOfTwentyFourMonthRollingPoints = 0;
        for(int i = 0; i< twentyFourMonthRollingPointsObservableList.size(); i++) {
            sumOfTwentyFourMonthRollingPoints = twentyFourMonthRollingPointsObservableList.get(i).getAmount() + sumOfTwentyFourMonthRollingPoints;
        }

        this.twentyFourMonthRollingTotalPointsInteger.set(sumOfTwentyFourMonthRollingPoints);

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

    public String getNextTwelveMonthRollingFallOffDate() {
        return nextTwelveMonthRollingFallOffDate.get();
    }

    public SimpleStringProperty nextTwelveMonthRollingFallOffDateProperty() {
        return nextTwelveMonthRollingFallOffDate;
    }

    public void setNextTwelveMonthRollingFallOffDate(String nextTwelveMonthRollingFallOffDate) {
        this.nextTwelveMonthRollingFallOffDate.set(nextTwelveMonthRollingFallOffDate);
    }

    public String getNextTwelveMonthFallOffAmountString() {
        return nextTwelveMonthFallOffAmountString.get();
    }

    public SimpleStringProperty nextTwelveMonthFallOffAmountStringProperty() {
        return nextTwelveMonthFallOffAmountString;
    }

    public void setNextTwelveMonthFallOffAmountString(String nextTwelveMonthFallOffAmountString) {
        this.nextTwelveMonthFallOffAmountString.set(nextTwelveMonthFallOffAmountString);
    }

    public int getTwelveMonthRollingTotalPointsInteger() {
        return twelveMonthRollingTotalPointsInteger.get();
    }

    public SimpleIntegerProperty twelveMonthRollingTotalPointsIntegerProperty() {
        return twelveMonthRollingTotalPointsInteger;
    }

    public ObservableList<Points> getTwelveMonthRollingPointsObservableList() {
        return twelveMonthRollingPointsObservableList;
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

    public String getNextTwentyFourMonthRollingFallOffDate() {
        return nextTwentyFourMonthRollingFallOffDate.get();
    }

    public SimpleStringProperty nextTwentyFourMonthRollingFallOffDateProperty() {
        return nextTwentyFourMonthRollingFallOffDate;
    }

    public int getTwentyFourMonthRollingTotalPointsInteger() {
        return twentyFourMonthRollingTotalPointsInteger.get();
    }

    public SimpleIntegerProperty twentyFourMonthRollingTotalPointsIntegerProperty() {
        return twentyFourMonthRollingTotalPointsInteger;
    }

    public ObservableList<Points> getTwentyFourMonthRollingPointsObservableList() {
        return twentyFourMonthRollingPointsObservableList;
    }

    @Override
    public String toString() {
        return employeeName.get();
    }

}
