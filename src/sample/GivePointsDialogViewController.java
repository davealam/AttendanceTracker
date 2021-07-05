package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class GivePointsDialogViewController {

    @FXML
    private TextField pointsField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextArea notesField;

    public void initialize() {
        dateField.setValue(LocalDate.now());
    }

    public void autoFill(Points pointsToEdit) {
        pointsField.setText(String.valueOf(pointsToEdit.getAmount()));
        dateField.setValue(pointsToEdit.getReceivedDate());
        notesField.setText(pointsToEdit.getManagerComment());
    }

    public TextField getPointsField() {
        return pointsField;
    }

    public DatePicker getDateField() {
        return dateField;
    }

    public TextArea getNotesField() {
        return notesField;
    }


}
