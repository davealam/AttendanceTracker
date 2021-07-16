package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        EmployeeRepository.getInstance().loadState();
        EmployeeRepository.getInstance().readFromSQLiteDB();

        for(int i = 0; i < EmployeeRepository.getInstance().getEmployeeObservableList().size(); i++) {
            EmployeeRepository.getInstance().getEmployeeObservableList().get(i).removeExpiredPoints();
        }

        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setTitle("AttendanceTracker");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
//        EmployeeRepository.getInstance().saveState();
        EmployeeRepository.getInstance().saveToSQLiteDB();
        super.stop();
    }
}