package Application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Points {

    private SimpleIntegerProperty amount;
    private SimpleObjectProperty<LocalDate> receivedDate;
    private SimpleObjectProperty<LocalDate> twelveMonthFallOffDate;
    private SimpleObjectProperty<LocalDate> twentyFourMonthFallOffDate;
    private SimpleStringProperty managerComment;

    private DateTimeFormatter dateTimeFormatter;

    private SimpleStringProperty receivedDateAsString;
    private SimpleStringProperty twelveMonthRollingFallOffDateAsString;
    private SimpleStringProperty twentyFourMonthRollingFallOffDateAsString;

    public Points(int pointAmount, LocalDate pointReceivedDate, String comment) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, u");

        this.amount = new SimpleIntegerProperty();
        this.amount.set(pointAmount);

        this.receivedDate = new SimpleObjectProperty<>();
        this.receivedDate.set(pointReceivedDate);

        this.twelveMonthFallOffDate = new SimpleObjectProperty<>();
        this.twelveMonthFallOffDate.set(receivedDate.get().plusYears(1));

        this.twentyFourMonthFallOffDate = new SimpleObjectProperty<>();
        this.twentyFourMonthFallOffDate.set(receivedDate.get().plusYears(2));

        //String format for points detailed tableView
        this.twelveMonthRollingFallOffDateAsString = new SimpleStringProperty();
        this.twelveMonthRollingFallOffDateAsString.set(dateTimeFormatter.format(twelveMonthFallOffDate.get()));

        this.twentyFourMonthRollingFallOffDateAsString = new SimpleStringProperty();
        this.twentyFourMonthRollingFallOffDateAsString.set(dateTimeFormatter.format(twentyFourMonthFallOffDate.get()));

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
        this.twelveMonthFallOffDate.set(receivedDate.get().plusYears(1));
        this.twelveMonthRollingFallOffDateAsString.set(dateTimeFormatter.format(receivedDate.get().plusYears(1)));

        this.twentyFourMonthFallOffDate.set(receivedDate.get().plusYears(2));
        this.twentyFourMonthRollingFallOffDateAsString.set(dateTimeFormatter.format(receivedDate.get().plusYears(2)));
    }

    public void setTwelveMonthRollingFallOffDateAsString(String twelveMonthRollingFallOffDateAsString) {
        this.twelveMonthRollingFallOffDateAsString.set(twelveMonthRollingFallOffDateAsString);
    }

    public LocalDate getTwelveMonthFallOffDate() {
        return twelveMonthFallOffDate.get();
    }

    public LocalDate getTwentyFourMonthFallOffDate() {
        return twentyFourMonthFallOffDate.get();
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

    public String getTwelveMonthRollingFallOffDateAsString() {
        return twelveMonthRollingFallOffDateAsString.get();
    }

    public SimpleStringProperty twelveMonthRollingFallOffDateAsStringProperty() {
        return twelveMonthRollingFallOffDateAsString;
    }

    public SimpleObjectProperty<LocalDate> twentyFourMonthFallOffDateProperty() {
        return twentyFourMonthFallOffDate;
    }

    public String getTwentyFourMonthRollingFallOffDateAsString() {
        return twentyFourMonthRollingFallOffDateAsString.get();
    }

    public SimpleObjectProperty<LocalDate> twelveMonthFallOffDateProperty() {
        return twelveMonthFallOffDate;
    }

    @Override
    public String toString() {
        return receivedDate.toString();
    }
}
