package Application;

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

public class DetailViewController {
    @FXML
    private Label employeeNameDisplay;
    @FXML
    private TableView<Points> pointsTableView;
    @FXML
    private TableColumn<Points, Integer> pointsCol;
    @FXML
    private TableColumn<Points, String> dateReceivedCol;
    @FXML
    private TableColumn<Points, String> twelveMonthRollingFallOffDateCol;
    @FXML
    private TableColumn<Points, String> managerNotesCol;
    @FXML
    private Button back;
    @FXML
    private Button deletePoints;
    @FXML
    private Button editPoints;

    @FXML
    private Employee selectedEmployee;

    public void initDetailView(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;

        pointsCol.setCellValueFactory(new PropertyValueFactory<Points, Integer>("amount"));
        dateReceivedCol.setCellValueFactory(new PropertyValueFactory<Points, String>("receivedDateAsString"));
        twelveMonthRollingFallOffDateCol.setCellValueFactory(new PropertyValueFactory<Points, String>("twelveMonthRollingFallOffDateAsString"));
        managerNotesCol.setCellValueFactory(new PropertyValueFactory<Points, String>("managerComment"));

        pointsTableView.setItems(selectedEmployee.getTwelveMonthRollingPointsObservableList());

        employeeNameDisplay.setText(selectedEmployee.getEmployeeName() + " Detail View: Points Accrued");
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
    public void handleDeletePointsClick() {
        Points selectedPoints = pointsTableView.getSelectionModel().getSelectedItem();
        selectedEmployee.removePointsFromTwelveMonthRollingList(selectedPoints);
        selectedEmployee.removePointsFromTwentyFourMonthRollingList(selectedPoints);
    }

    @FXML
    public void handleEditPointsClick() throws IOException {
        Points selectedPoints = pointsTableView.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> editPointsDialogPane = new Dialog<>();
        editPointsDialogPane.initOwner(back.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("GivePointsDialogView.fxml"));

        editPointsDialogPane.getDialogPane().setContent(fxmlLoader.load());

        GivePointsDialogViewController givePointsDialogViewController = fxmlLoader.getController();

        editPointsDialogPane.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editPointsDialogPane.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        givePointsDialogViewController.autoFill(selectedPoints);

        Optional<ButtonType> result = editPointsDialogPane.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            int editedPoints = Integer.valueOf(givePointsDialogViewController.getPointsField().getText().trim());
            LocalDate editedReceivedDate = givePointsDialogViewController.getDateField().getValue();
            String editedManagerNotes = givePointsDialogViewController.getNotesField().getText().trim();

            selectedPoints.setAmount(editedPoints);
            selectedPoints.setReceivedDate(editedReceivedDate);
            selectedPoints.setManagerComment(editedManagerNotes);

            FXMLLoader fxmlLoader2 = new FXMLLoader();
            fxmlLoader2.setLocation(getClass().getResource("View.fxml"));
            ViewController viewController = fxmlLoader2.getController();

            selectedEmployee.setFallOffs();
            selectedEmployee.setPointTotals();

        } else {
            System.out.println("Cancelled");
        }

    }

}
