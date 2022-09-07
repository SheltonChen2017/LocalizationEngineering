package UI_LabelApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class HelloWorld extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            BorderPane root = new BorderPane();
            Button button = new Button("Hello");
            button.setOnAction(event -> {
                button.setText("hello world");
            });

            root.setCenter(button);

            Scene scene = new Scene(root, 400, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("helloworld");
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
