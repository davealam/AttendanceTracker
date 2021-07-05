package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UseSickDayDialogViewController {

    @FXML
    private TextField sickDaysField;
    @FXML
    private CheckBox paidCheckBox;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextArea notesField;

    public void autofillPaidSickDayEditDialog(PaidSickDay paidSickDay) {
        this.sickDaysField.setText(String.valueOf(paidSickDay.getAmountUsed()));
        this.dateField.setValue(paidSickDay.getDateUsedValue());
        this.notesField.setText(paidSickDay.getManagerComment());
    }

    public void autofillUnpaidSickDayEditDialog(UnpaidSickDay unpaidSickDay) {
        this.sickDaysField.setText(String.valueOf(unpaidSickDay.getAmountUsed()));
        this.dateField.setValue(unpaidSickDay.getDateUsedValue());
        this.notesField.setText(unpaidSickDay.getManagerComment());
    }


    public TextField getSickDaysField() {
        return sickDaysField;
    }

    public void setSickDaysField(TextField sickDaysField) {
        this.sickDaysField = sickDaysField;
    }

    public CheckBox getPaidCheckBox() {
        return paidCheckBox;
    }

    public void setPaidCheckBox(CheckBox paidCheckBox) {
        this.paidCheckBox = paidCheckBox;
    }

    public DatePicker getDateField() {
        return dateField;
    }

    public void setDateField(DatePicker dateField) {
        this.dateField = dateField;
    }

    public TextArea getNotesField() {
        return notesField;
    }

    public void setNotesField(TextArea notesField) {
        this.notesField = notesField;
    }
}
