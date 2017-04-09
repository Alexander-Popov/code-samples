package nstu.geo.presentation.controller.tab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nstu.geo.core.application.command.Command;
import nstu.geo.core.application.command.SimpleCommandBus;
import nstu.geo.core.application.command.ValidationCommandBus;
import nstu.geo.core.application.command.segy.ConvertSegyToIeeeFormatCommand;
import nstu.geo.core.domain.model.segy.SegyFile;
import nstu.geo.core.domain.repository.segy.SegyFileInMemoryRepository;
import nstu.geo.core.infrastructure.repository.SegyFileInRamRepository;
import nstu.geo.core.infrastructure.service.file.FileStorage;
import nstu.geo.presentation.helper.view.SegyFileViewHelper;
import nstu.geo.presentation.model.SegyParameter;

import java.io.File;

public class InfoController {

	@FXML private TableView infoTable;
	@FXML private Button convertButton;
	@FXML private TextField increaseOn;
	private SegyFileInMemoryRepository segyRepository = SegyFileInRamRepository.getInstance();

	@FXML
	public void initialize() {
		if(this.segyRepository.exists("main")) {
			SegyFile segyFile = this.segyRepository.find("main");
			infoTable.setItems(getSegyHeaderData(segyFile));
			if(!segyFile.isIeeeAndLittleEndian()) {
				convertButton.setDisable(false);
			}
			increaseOn.setDisable(false);
			increaseOn.setText("1");
		} else {
			increaseOn.setDisable(true);
		}

	}

	private static ObservableList getSegyHeaderData(SegyFile segyFile) {
		String fileFormat = Short.toString(segyFile.getSegyFormat()) + " (" + SegyFileViewHelper.getFileFormatCodeDescription(segyFile.getSegyFormat()) + ")";
		return FXCollections.observableArrayList(
				new SegyParameter("Файл", segyFile.getFile().getAbsolutePath()),
				new SegyParameter("Колличество байтов", Long.toString(segyFile.getFile().length())),
				new SegyParameter("Порядок байтов", segyFile.isLittleEndian() ? "little-endian" : "big-endian"),
				new SegyParameter("Код формата файла", fileFormat),
				new SegyParameter("Колличество сейсмотрасс", Short.toString(segyFile.getNumOfTraces())),
				new SegyParameter("Время дискретизации", Short.toString(segyFile.getDiscretizationTime())),
				new SegyParameter("Колличество записей на одну трассу", Short.toString(segyFile.getNumOfReportsForOneTrace())),
				new SegyParameter("Дополнительные заголовки", Short.toString(segyFile.getExtendedHeaders())),
				new SegyParameter("Колличество отчётов", Long.toString(segyFile.getNumberOfDataRecords()))
		);
	}


	@FXML
	public void convertFileAction() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Выберите директорию для сохранения");
		File selectedDirectory = directoryChooser.showDialog(new Stage());
		if (selectedDirectory != null) {
			Command command = new ConvertSegyToIeeeFormatCommand(
					segyRepository.find("main").getFile(),
					(new File(selectedDirectory.getAbsolutePath() + "/" + FileStorage.generateSgyTimeName())),
					Integer.valueOf(increaseOn.getText())
			);
			new ValidationCommandBus((new SimpleCommandBus())).execute(command);
		}
	}
}
