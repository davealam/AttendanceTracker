package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class SickDaysDetailsViewController {

    @FXML
    private Button back;
    @FXML
    private Button editSickDay;
    @FXML
    private Button deleteSickDay;
    @FXML
    private TableView<PaidSickDay> paidSickDayTableView;
    @FXML
    private TableColumn<PaidSickDay, Double> paidSickDaysUsedCol;
    @FXML
    private TableColumn<PaidSickDay, String> paidDateUsedCol;
    @FXML
    private TableColumn<PaidSickDay, String> paidManagerCommentCol;

    @FXML
    private TableView<UnpaidSickDay> unpaidSickDayTableView;
    @FXML
    private TableColumn<UnpaidSickDay, Double> unpaidSickDaysUsedCol;
    @FXML
    private TableColumn<UnpaidSickDay, String> unpaidDateUsedCol;
    @FXML
    private TableColumn<UnpaidSickDay, String> unpaidManagerCommentCol;
    @FXML
    private Employee selectedEmployee;
    @FXML
    private Label employeeNameDisplay;


    public void initPaidSickDaysUsedDetailView(Employee employee) {
        Employee selectedEmployee = employee;
        this.selectedEmployee = selectedEmployee;

        paidSickDaysUsedCol.setCellValueFactory(new PropertyValueFactory<PaidSickDay, Double>("amountUsed"));
        paidDateUsedCol.setCellValueFactory(new PropertyValueFactory<PaidSickDay, String>("dateUsed"));
        paidManagerCommentCol.setCellValueFactory(new PropertyValueFactory<PaidSickDay, String>("managerComment"));

        employeeNameDisplay.setText(selectedEmployee.getEmployeeName() + " Detail View: Sick Days Used");

        paidSickDayTableView.setItems(employee.getPaidSickDayObservableList());
    }

    public void initUnpaidSickDaysUsedDetailView(Employee employee) {
        Employee selectedEmployee = employee;

        unpaidSickDaysUsedCol.setCellValueFactory(new PropertyValueFactory<UnpaidSickDay, Double>("amountUsed"));
        unpaidDateUsedCol.setCellValueFactory(new PropertyValueFactory<UnpaidSickDay, String>("dateUsed"));
        unpaidManagerCommentCol.setCellValueFactory(new PropertyValueFactory<UnpaidSickDay, String>("managerComment"));

        unpaidSickDayTableView.setItems(selectedEmployee.getUnpaidSickDayObservableList());
    }

    @FXML
    public void handleBackClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("View.fxml"));
        Parent mainView = fxmlLoader.load();
        Scene newScene = new Scene(mainView);
        Stage stage = (Stage) back.getScene().getWindow();
        stage.setScene(newScene);
    }

    @FXML
    public void handleEditSickDayClick() throws IOException {
        if(paidSickDayTableView.getSelectionModel().getSelectedItem() != null) {
            PaidSickDay selectedPaidSickDay = paidSickDayTableView.getSelectionModel().getSelectedItem();
            initEditPaidSickDaysView(selectedPaidSickDay);
            paidSickDayTableView.getSelectionModel().clearSelection();
            selectedEmployee.setSickDaysTotal();
        }

        if(unpaidSickDayTableView.getSelectionModel().getSelectedItem() != null) {
            UnpaidSickDay selectedUnpaidSickDay = unpaidSickDayTableView.getSelectionModel().getSelectedItem();
            initEditUnpaidSickDaysView(selectedUnpaidSickDay);
            unpaidSickDayTableView.getSelectionModel().clearSelection();
            selectedEmployee.setSickDaysTotal();
        }

    }

    public void initEditPaidSickDaysView(PaidSickDay paidSickDay) throws IOException {
        Dialog<ButtonType> editSickDaysDialogPane = new Dialog<>();
        editSickDaysDialogPane.initOwner(editSickDay.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UseSickDayDialogView.fxml"));
        editSickDaysDialogPane.getDialogPane().setContent(fxmlLoader.load());

        editSickDaysDialogPane.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editSickDaysDialogPane.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        UseSickDayDialogViewController useSickDayDialogViewController = fxmlLoader.getController();
        useSickDayDialogViewController.autofillPaidSickDayEditDialog(paidSickDay);

        Optional<ButtonType> result = editSickDaysDialogPane.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            paidSickDay.setAmountUsed(Double.valueOf(useSickDayDialogViewController.getSickDaysField().getText()));
            paidSickDay.setDateUsed(useSickDayDialogViewController.getDateField().getValue());
            paidSickDay.setManagerComment(useSickDayDialogViewController.getNotesField().getText());

        } else {
            System.out.println("Cancelled");
        }
    }

    public void initEditUnpaidSickDaysView(UnpaidSickDay unpaidSickDay) throws IOException {
        Dialog<ButtonType> editSickDaysDialogPane = new Dialog<>();
        editSickDaysDialogPane.initOwner(editSickDay.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UseSickDayDialogView.fxml"));
        editSickDaysDialogPane.getDialogPane().setContent(fxmlLoader.load());

        editSickDaysDialogPane.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editSickDaysDialogPane.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        UseSickDayDialogViewController useSickDayDialogViewController = fxmlLoader.getController();
        useSickDayDialogViewController.autofillUnpaidSickDayEditDialog(unpaidSickDay);


        Optional<ButtonType> result = editSickDaysDialogPane.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            unpaidSickDay.setAmountUsed(Double.valueOf(useSickDayDialogViewController.getSickDaysField().getText()));
            unpaidSickDay.setDateUsed(useSickDayDialogViewController.getDateField().getValue());
            unpaidSickDay.setManagerComment(useSickDayDialogViewController.getNotesField().getText());

        } else {
            System.out.println("Cancelled");
        }
    }

    @FXML
    public void handleDeleteSickDayClick() {
        if(paidSickDayTableView.getSelectionModel().getSelectedItem() != null) {
            PaidSickDay selectedPaidSickDay = paidSickDayTableView.getSelectionModel().getSelectedItem();
            selectedEmployee.getPaidSickDayObservableList().remove(selectedPaidSickDay);

        } else {
            UnpaidSickDay selectedUnpaidSickDay = unpaidSickDayTableView.getSelectionModel().getSelectedItem();
            selectedEmployee.getUnpaidSickDayObservableList().remove(selectedUnpaidSickDay);
        }

        paidSickDayTableView.getSelectionModel().clearSelection();
        selectedEmployee.setSickDaysTotal();
    }
}
