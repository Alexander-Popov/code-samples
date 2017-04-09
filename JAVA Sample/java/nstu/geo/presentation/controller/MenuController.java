package nstu.geo.presentation.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nstu.geo.core.domain.model.segy.SegyFile;
import nstu.geo.core.domain.model.segy.SegyFileFabric;
import nstu.geo.core.domain.repository.segy.SegyFileInMemoryRepository;
import nstu.geo.core.infrastructure.repository.SegyFileInRamRepository;
import nstu.geo.presentation.Main;
import nstu.geo.presentation.helper.view.Layouts;

import java.io.File;
import java.io.IOException;


public class MenuController {
    private static Stage window;
    private SegyFileInMemoryRepository segyRepository = SegyFileInRamRepository.getInstance();
    private Layouts layouts = new Layouts();
    static {
        window = Main.window;
    }

    @FXML
    private void exitAction(final ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void chooseFileAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            SegyFile segyFile = SegyFileFabric.createNew(file);
            this.segyRepository.add("main", segyFile);
            Parent layout = layouts.getMain();
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.show();
        }
    }

}
