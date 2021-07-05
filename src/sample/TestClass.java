package sample;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestClass {
    public static void main(String[] args) {

        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        "MM/dd/yyyy"

        String date = "2021-07-04";

        LocalDate convertedDate = LocalDate.parse(date, inputDateFormatter);

        System.out.println(convertedDate);
        System.out.println(convertedDate.getClass());

    }
}
