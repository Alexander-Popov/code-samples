package nstu.geo.presentation.helper.view;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Layouts {
    public Parent getMain()
    {
        Parent main = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Main.fxml"));
        try {
            main =  loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return main;
    }
}
