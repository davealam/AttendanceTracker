package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UnpaidSickDay {
    private SimpleStringProperty dateUsed;
    private SimpleObjectProperty<LocalDate> dateUsedValue;
    private SimpleDoubleProperty amountUsed;
    private SimpleStringProperty managerComment;
    private DateTimeFormatter dateTimeFormatter;

    public UnpaidSickDay(double amount, LocalDate date, String comment) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, u");

        this.amountUsed = new SimpleDoubleProperty();
        this.amountUsed.set(amount);

        this.dateUsedValue = new SimpleObjectProperty<>();
        this.dateUsedValue.set(date);

        this.dateUsed = new SimpleStringProperty();
        dateUsed.set(dateTimeFormatter.format(date));

        this.managerComment = new SimpleStringProperty();
        this.managerComment.set(comment);


    }

    public double getAmountUsed() {
        return amountUsed.get();
    }

    public SimpleDoubleProperty amountUsedProperty() {
        return amountUsed;
    }

    public String getDateUsed() {
        return dateUsed.get();
    }

    public LocalDate getDateUsedValue() {
        return dateUsedValue.get();
    }

    public SimpleObjectProperty<LocalDate> dateUsedValueProperty() {
        return dateUsedValue;
    }

    public SimpleStringProperty dateUsedProperty() {
        return dateUsed;
    }

    public String getManagerComment() {
        return managerComment.get();
    }

    public SimpleStringProperty managerCommentProperty() {
        return managerComment;
    }

    public void setDateUsed(LocalDate dateUsed) {
        this.dateUsedValue.set(dateUsed);
        this.dateUsed.set(dateTimeFormatter.format(dateUsed));
    }

    public void setAmountUsed(double amountUsed) {
        this.amountUsed.set(amountUsed);
    }

    public void setManagerComment(String managerComment) {
        this.managerComment.set(managerComment);
    }
}
