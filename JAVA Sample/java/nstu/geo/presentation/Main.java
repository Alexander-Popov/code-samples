package nstu.geo.presentation;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nstu.geo.presentation.helper.view.Layouts;


public class Main extends Application {

    public static Stage window;
    private Layouts layouts = new Layouts();

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        Parent root = layouts.getMain();
        window.setTitle("NSTU SGE-Y master 2017");
        window.setScene(new Scene(root));
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
