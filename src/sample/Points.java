package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Points {

    private SimpleIntegerProperty amount;
    private SimpleObjectProperty<LocalDate> receivedDate;
    private SimpleObjectProperty<LocalDate> fallOffDate;
    private SimpleStringProperty managerComment;

    private DateTimeFormatter dateTimeFormatter;

    private SimpleStringProperty receivedDateAsString;
    private SimpleStringProperty fallOffDateAsString;

    public Points(int pointAmount, LocalDate pointReceivedDate, String comment) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, u");

        this.amount = new SimpleIntegerProperty();
        this.amount.set(pointAmount);

        this.receivedDate = new SimpleObjectProperty<>();
        this.receivedDate.set(pointReceivedDate);

        this.fallOffDate = new SimpleObjectProperty<>();
        this.fallOffDate.set(receivedDate.get().plusYears(1));

        //String format for points detailed tableView
        this.fallOffDateAsString = new SimpleStringProperty();
        this.fallOffDateAsString.set(dateTimeFormatter.format(fallOffDate.get()));
        this.receivedDateAsString = new SimpleStringProperty();
        this.receivedDateAsString.set(dateTimeFormatter.format(pointReceivedDate));

        managerComment = new SimpleStringProperty();
        managerComment.set(comment);
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public SimpleIntegerProperty amountProperty() {
        return amount;
    }

    public LocalDate getReceivedDate() {
        return receivedDate.get();
    }

    public SimpleObjectProperty<LocalDate> receivedDateProperty() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate.set(receivedDate);
        this.receivedDateAsString.set(dateTimeFormatter.format(receivedDate));
        updateFallOffDate();
    }

    public void updateFallOffDate() {
        this.fallOffDate.set(receivedDate.get().plusYears(1));
        this.fallOffDateAsString.set(dateTimeFormatter.format(receivedDate.get().plusYears(1)));
    }

    public LocalDate getFallOffDate() {
        return fallOffDate.get();
    }

    public SimpleObjectProperty<LocalDate> fallOffDateProperty() {
        return fallOffDate;
    }

    public String getManagerComment() {
        return managerComment.get();
    }

    public SimpleStringProperty managerCommentProperty() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment.set(managerComment);
    }

    public String getReceivedDateAsString() {
        return receivedDateAsString.get();
    }

    public SimpleStringProperty receivedDateAsStringProperty() {
        return receivedDateAsString;
    }

    public String getFallOffDateAsString() {
        return fallOffDateAsString.get();
    }

    public SimpleStringProperty fallOffDateAsStringProperty() {
        return fallOffDateAsString;
    }

    @Override
    public String toString() {
        return receivedDate.toString();
    }
}
