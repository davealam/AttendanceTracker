package Application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EmployeeChangeViewController {

    @FXML
    private TextField employeeNameField;
    @FXML
    private TextField managerNameField;
    @FXML
    private Label windowNameLabel;

    public TextField getEmployeeNameField() {
        return employeeNameField;
    }

    public TextField getManagerNameField() {
        return managerNameField;
    }

    public void setWindowNameLabel(Label windowNameLabel) {
        this.windowNameLabel = windowNameLabel;
    }

    public void autofillEmployeeChangeViewDialog(Employee employee) {
        this.employeeNameField.setText(employee.getEmployeeName());
        this.managerNameField.setText(employee.getEmployeeManager());
    }

}
