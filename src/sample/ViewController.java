package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class ViewController {

    @FXML
    TableView<Employee> employeeTableView;
    @FXML
    TableColumn<Employee, String> nameCol;
    @FXML
    TableColumn<Employee, Integer> pointsAccruedCol;
    @FXML
    TableColumn<Employee, String> nextFallOffDateCol;
    @FXML
    TableColumn<Employee, String> nextFallOffAmountCol;
    @FXML
    TableColumn<Employee, Double> paidSickDaysUsedCol;
    @FXML
    TableColumn<Employee, Double> unpaidSickDaysUsedCol;
    @FXML
    Button viewDetails;
    @FXML
    Button givePoints;
    @FXML
    Button useSickDay;
    @FXML
    Button sickTimeDetails;
    @FXML
    private MenuItem addEmployee;
    @FXML
    private MenuItem editEmployee;
    @FXML
    private MenuItem deleteEmployee;

    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeName"));
        pointsAccruedCol.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("totalPointsInteger"));
        nextFallOffDateCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("nextFallOffDate"));
        nextFallOffAmountCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("nextFallOffAmountString"));
        paidSickDaysUsedCol.setCellValueFactory(new PropertyValueFactory<Employee, Double>("totalPaidSickDaysUsed"));
        unpaidSickDaysUsedCol.setCellValueFactory(new PropertyValueFactory<Employee, Double>("totalUnpaidSickDaysUsed"));

        employeeTableView.setItems(EmployeeRepository.getInstance().getEmployeeObservableList());

    }

    @FXML
    public void handleUseSickDayClick() throws IOException {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> useSickDayDialogPane = new Dialog<>();
        useSickDayDialogPane.initOwner(useSickDay.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UseSickDayDialogView.fxml"));
        useSickDayDialogPane.getDialogPane().setContent(fxmlLoader.load());

        useSickDayDialogPane.getDialogPane().getButtonTypes().add(ButtonType.OK);
        useSickDayDialogPane.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = useSickDayDialogPane.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            UseSickDayDialogViewController useSickDayDialogViewController = fxmlLoader.getController();
            double sickDaysAmount = Double.valueOf(useSickDayDialogViewController.getSickDaysField().getText());
            boolean isPaid = useSickDayDialogViewController.getPaidCheckBox().isSelected();
            LocalDate dateUsed = useSickDayDialogViewController.getDateField().getValue();
            String managerNotes = useSickDayDialogViewController.getNotesField().getText();

            if(isPaid) {
                    selectedEmployee.addPaidSickDay(sickDaysAmount, dateUsed, managerNotes);
            } else {
                    selectedEmployee.addUnpaidSickDay(sickDaysAmount, dateUsed, managerNotes);
            }
        } else {
            System.out.println("Cancelled");
        }
    }

    @FXML
    public void handleViewDetailsClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("DetailView.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) viewDetails.getScene().getWindow();
        Scene newScene = new Scene(root);

        DetailViewController detailViewController = fxmlLoader.getController();
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        detailViewController.initDetailView(selectedEmployee);

        stage.setScene(newScene);
    }

    @FXML
    public void handleSickTimeDetailsClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("SickDaysDetailView.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) sickTimeDetails.getScene().getWindow();
        Scene newScene = new Scene(root);

        SickDaysDetailsViewController sickDaysDetailsViewController = fxmlLoader.getController();
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        sickDaysDetailsViewController.initPaidSickDaysUsedDetailView(selectedEmployee);
        sickDaysDetailsViewController.initUnpaidSickDaysUsedDetailView(selectedEmployee);

        stage.setScene(newScene);
    }

    @FXML
    public void handleGivePointsClick() throws IOException {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> givePointsDialogPane = new Dialog<>();
        givePointsDialogPane.initOwner(givePoints.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("GivePointsDialogView.fxml"));
        givePointsDialogPane.getDialogPane().setContent(fxmlLoader.load());

        givePointsDialogPane.getDialogPane().getButtonTypes().add(ButtonType.OK);
        givePointsDialogPane.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = givePointsDialogPane.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            GivePointsDialogViewController givePointsDialogViewController = fxmlLoader.getController();

            int inputPoints = Integer.valueOf(givePointsDialogViewController.getPointsField().getText().trim());
            LocalDate inputReceivedDate = givePointsDialogViewController.getDateField().getValue();

            System.out.println(inputReceivedDate);

            String inputManagerNotes = givePointsDialogViewController.getNotesField().getText().trim();

            selectedEmployee.addPoints(inputPoints, inputReceivedDate, inputManagerNotes);

        } else {
            System.out.println("Cancelled");
        }
    }

    @FXML
    public void handleAddEmployeeClick() throws IOException {

        Dialog<ButtonType> employeeChangeDialogView = new Dialog<>();
        employeeChangeDialogView.initOwner(givePoints.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("EmployeeChangeView.fxml"));
        employeeChangeDialogView.getDialogPane().setContent(fxmlLoader.load());

        employeeChangeDialogView.getDialogPane().getButtonTypes().add(ButtonType.OK);
        employeeChangeDialogView.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = employeeChangeDialogView.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            System.out.println("clicked okay : )");
                        
        } else {
            
            System.out.println("Cancelled");
            
        }

    }

    @FXML
    public void handleEditEmployeeClick() {

    }

    @FXML
    public void handleDeleteEmployeeCLick() {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        EmployeeRepository.getInstance().getEmployeeObservableList().remove(selectedEmployee);
    }

}

