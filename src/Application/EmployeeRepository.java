package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.Statement;
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

    public void saveState() {

        JSONArray jsonEmployeeArray = new JSONArray();

        if(employeeObservableList.isEmpty()) {
            try {
                FileWriter fileWriter = new FileWriter("employeeRecords.json");
                fileWriter.write("");

                fileWriter.close();

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }

        for (Employee employee : employeeObservableList) {

            JSONObject employeeContainer = new JSONObject();
            JSONArray jsonPointsArray = new JSONArray();
            JSONArray jsonPaidSickDayArray = new JSONArray();
            JSONArray jsonUnpaidSickDayArray = new JSONArray();

            employeeContainer.put("employeeName", employee.getEmployeeName());
            employeeContainer.put("employeeManager", employee.getEmployeeManager());
            employeeContainer.put("pointsObservableList", jsonPointsArray);
            employeeContainer.put("paidSickDayObservableList", jsonPaidSickDayArray);
            employeeContainer.put("unpaidSickDayObservableList", jsonUnpaidSickDayArray);


            for (int i = 0; i < employee.getPointsObservableList().size(); i++) {
                JSONObject pointJSONObject = new JSONObject();
                pointJSONObject.put("pointAmount", employee.getPointsObservableList().get(i).getAmount());
                pointJSONObject.put("receivedDate", employee.getPointsObservableList().get(i).getReceivedDate().toString());
                pointJSONObject.put("managerComment", employee.getPointsObservableList().get(i).getManagerComment());

                jsonPointsArray.add(pointJSONObject);
            }

            for (int i = 0; i < employee.getPaidSickDayObservableList().size(); i++) {
                JSONObject paidSickDayJSONObject = new JSONObject();
                paidSickDayJSONObject.put("amountUsed", employee.getPaidSickDayObservableList().get(i).getAmountUsed());
                paidSickDayJSONObject.put("dateUsedValue", employee.getPaidSickDayObservableList().get(i).getDateUsedValue().toString());
                paidSickDayJSONObject.put("managerComment", employee.getPaidSickDayObservableList().get(i).getManagerComment());

                jsonPaidSickDayArray.add(paidSickDayJSONObject);
            }

            for (int i = 0; i < employee.getUnpaidSickDayObservableList().size(); i++) {
                JSONObject unpaidSickDayJSONObject = new JSONObject();
                unpaidSickDayJSONObject.put("amountUsed", employee.getUnpaidSickDayObservableList().get(i).getAmountUsed());
                unpaidSickDayJSONObject.put("dateUsedValue", employee.getUnpaidSickDayObservableList().get(i).getDateUsedValue().toString());
                unpaidSickDayJSONObject.put("managerComment", employee.getUnpaidSickDayObservableList().get(i).getManagerComment());

                jsonUnpaidSickDayArray.add(unpaidSickDayJSONObject);
            }

            jsonEmployeeArray.add(employeeContainer);

            try {
                FileWriter fileWriter = new FileWriter("employeeRecords.json");
                fileWriter.write(jsonEmployeeArray.toString());

                fileWriter.close();

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void loadState() {

        //Declare containers to be used for retrieved objects and arrays
        JSONArray retrievedEmployeeArray = new JSONArray();
        JSONObject retrievedEmployee;
        JSONArray retrievedPointsObservableList;
        JSONObject retrievedPoint;
        JSONArray retrievedPaidSickDayObservableList;
        JSONObject retrievedPaidSickDay;
        JSONArray retrievedUnpaidSickDayObservableList;
        JSONObject retrievedUnpaidSickDay;


        try {
            FileReader fileReader = new FileReader("employeeRecords.json");
            JSONParser jsonParser = new JSONParser();

            retrievedEmployeeArray = (JSONArray) jsonParser.parse(fileReader);

        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        //Retrieve employee list
        for (int i = 0; i < retrievedEmployeeArray.size(); i++) {

            retrievedEmployee = (JSONObject) retrievedEmployeeArray.get(i);

            String employeeName = (String) retrievedEmployee.get("employeeName");
            String employeeManager = (String) retrievedEmployee.get("employeeManager");

            Employee employee = new Employee(employeeName, employeeManager);
            employeeObservableList.add(employee);

            //Apply points to each employee
            retrievedPointsObservableList = (JSONArray) retrievedEmployee.get("pointsObservableList");

            for (int j = 0; j < retrievedPointsObservableList.size(); j++) {
                retrievedPoint = (JSONObject) retrievedPointsObservableList.get(j);

                long longPointAmount = (long) retrievedPoint.get("pointAmount");
                int pointAmount = (int) longPointAmount;

                String receivedDateString = (String) retrievedPoint.get("receivedDate");
                LocalDate receivedDate = LocalDate.parse(receivedDateString, inputDateFormatter);

                String managerComment = (String) retrievedPoint.get("managerComment");

                employee.addPoints(pointAmount, receivedDate, managerComment);

            }

            //Apply paid sick days used by each employee
            retrievedPaidSickDayObservableList = (JSONArray) retrievedEmployee.get("paidSickDayObservableList");

            for (int k = 0; k < retrievedPaidSickDayObservableList.size(); k++) {
                retrievedPaidSickDay = (JSONObject) retrievedPaidSickDayObservableList.get(k);

                double amountUsed = (double) retrievedPaidSickDay.get("amountUsed");

                String dateUsedValue = (String) retrievedPaidSickDay.get("dateUsedValue");
                LocalDate dateUsed = LocalDate.parse(dateUsedValue, inputDateFormatter);

                String managerComment = (String) retrievedPaidSickDay.get("managerComment");

                employee.addPaidSickDay(amountUsed, dateUsed, managerComment);

            }

            //Apply unpaid sick days used by each employee
            retrievedUnpaidSickDayObservableList = (JSONArray) retrievedEmployee.get("unpaidSickDayObservableList");

            for (int m = 0; m < retrievedUnpaidSickDayObservableList.size(); m++) {
                retrievedUnpaidSickDay = (JSONObject) retrievedUnpaidSickDayObservableList.get(m);

                double amountUsed = (double) retrievedUnpaidSickDay.get("amountUsed");

                String dateUsedValue = (String) retrievedUnpaidSickDay.get("dateUsedValue");
                LocalDate dateUsed = LocalDate.parse(dateUsedValue, inputDateFormatter);

                String managerComment = (String) retrievedUnpaidSickDay.get("managerComment");

                employee.addUnpaidSickDay(amountUsed, dateUsed, managerComment);

            }
        }
    }

    public void addEmployee(String employeeName, String managerName) {
        Employee employeeToAdd = new Employee(employeeName, managerName);
        employeeObservableList.add(employeeToAdd);
    }

    public void saveToSQLiteDB() {
        for(Employee employee : employeeObservableList) {
            SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler();
            sqLiteDBHandler.connectionOpen();
            sqLiteDBHandler.writeEmployeeToDB(employee);
        }
    }
}
