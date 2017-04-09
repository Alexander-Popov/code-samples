package nstu.geo.presentation.controller.tab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import nstu.geo.core.domain.model.segy.SegyFile;
import nstu.geo.core.domain.repository.segy.SegyFileInMemoryRepository;
import nstu.geo.core.infrastructure.repository.SegyFileInRamRepository;
import nstu.geo.core.infrastructure.service.segy.converter.SegyFloatConverter;
import nstu.geo.core.infrastructure.service.spline.SegySplineInterpolator;
import nstu.geo.core.infrastructure.service.string.RangeFromStringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class TraceLineChartController {
	private SegyFileInMemoryRepository segyRepository = SegyFileInRamRepository.getInstance();
	@FXML private TextField rangeInput;
	@FXML private TextField pointFromInput;
	@FXML private TextField pointToInput;
	@FXML private LineChart<Number, Number> lineChart;
	@FXML private Button submit;
	private static String range = "1";
	private static Integer pointFrom = 0;
	private static Integer pointTo;
	private static boolean flagWorking = false;
	@FXML private NumberAxis xAxis;

	@FXML
	public void initialize() {
		if(this.segyRepository.exists("main")) {
			rangeInput.setDisable(false);
			pointFromInput.setDisable(false);
			pointToInput.setDisable(false);
			submit.setDisable(false);
			rangeInput.setText(range);
			pointFromInput.setText(pointFrom.toString());
			if (pointTo != null) {
				pointToInput.setText(pointTo.toString());
			}
			if(!flagWorking) {

				SegyFile segyFile = this.segyRepository.find("main");
				int numOfRep = segyFile.getNumOfReportsForOneTrace();
				setLineChart(segyFile, 0, numOfRep);
				pointTo = numOfRep;
				pointToInput.setText(pointTo.toString());
				xAxis.setLowerBound(0);
				xAxis.setUpperBound(numOfRep);
				flagWorking = true;
				setTickUnit(numOfRep);
			}
		} else {
			rangeInput.setDisable(true);
			pointFromInput.setDisable(true);
			pointToInput.setDisable(true);
			submit.setDisable(true);
		}
	}

	@FXML
	public void showCustomAction(ActionEvent event) throws IOException {
		lineChart.getData().clear();
		flagWorking = true;
		SegyFile segyFile = this.segyRepository.find("main");
		range = rangeInput.getText();
		pointFrom = Integer.valueOf(pointFromInput.getText());
		if(pointTo == null) {
			pointTo = (int) segyFile.getNumOfReportsForOneTrace();
		}
		if(!pointToInput.getText().isEmpty()) {
			pointTo = Integer.valueOf(pointToInput.getText());
		}
		if (pointFrom < 0) {
			pointFrom = 0;
		}
		if(pointTo > segyFile.getNumOfReportsForOneTrace()) {
			pointTo = (int) segyFile.getNumOfReportsForOneTrace();
		}
		xAxis.setLowerBound(pointFrom);
		xAxis.setUpperBound(pointTo);
		setTickUnit(pointTo - pointFrom);
		this.setLineChart(segyFile, pointFrom, pointTo);
	}

	/* test with spline*/
	private void setLineChartWithSpline(SegyFile segyFile, int pointFrom, int pointTo) {
		pointFrom = 0;
		pointTo = 4000;

		Set<Integer> tracesNums = RangeFromStringConverter.getIntRanges(range);
		Integer max = Collections.max(tracesNums);
		Integer min = Collections.min(tracesNums);
		for (int i = min; i <= max; i++) {
			if(tracesNums.contains(i)) {
				XYChart.Series series = new XYChart.Series();
				series.setName("Сейсмотрасса " + i);
				//populating the series with data
				byte[] bytes = segyFile.getSeismicTraceBytes(30, pointFrom, pointTo);
				SegyFloatConverter floatConverter = new SegyFloatConverter(segyFile);
				int fNByte = 1;



				float[] yFloats = new float[pointTo - pointFrom];
				int[] xInts = new int[pointTo - pointFrom];
				int seq = 0;


				for (int k = 0; k < (pointTo - pointFrom); k++) {
					if (k != 0) {
						fNByte += 4;
					}
					byte[] floatBytes = new byte[]{bytes[fNByte], bytes[fNByte + 1], bytes[fNByte + 2], bytes[fNByte + 3]};
					Float y = floatConverter.convert(floatBytes);

					yFloats[seq] = y;
					xInts[seq] = pointFrom + k;
					//series.getData().add(new XYChart.Data(pointFrom + k, y));


					seq += 1;
				}
				lineChart.getData().add(series);



				XYChart.Series seriesSpline = new XYChart.Series();
				seriesSpline.setName("Сейсмотрасса сплайн" + i);
				SegySplineInterpolator interpolator = new SegySplineInterpolator();
				interpolator.interpolate(xInts, yFloats);
				for(float m = pointFrom; m < pointTo - 1;) {
					System.out.println(interpolator.value(m));
					seriesSpline.getData().add(new XYChart.Data(m, interpolator.value(m)));
					m += 0.1;

				}
				lineChart.getData().add(seriesSpline);
			}
		}
	}


	/* Original. It works.*/
	private void setLineChart(SegyFile segyFile, int pointFrom, int pointTo) {
		Set<Integer> tracesNums = RangeFromStringConverter.getIntRanges(range);
		Integer max = Collections.max(tracesNums);
		Integer min = Collections.min(tracesNums);
		for (int i = min; i <= max; i++) {
			if(tracesNums.contains(i)) {
				XYChart.Series series = new XYChart.Series();
				series.setName("Сейсмотрасса " + i);
				//populating the series with data
				byte[] bytes = segyFile.getSeismicTraceBytes(i, pointFrom, pointTo);
				SegyFloatConverter floatConverter = new SegyFloatConverter(segyFile);
				int fNByte = 1;
				for (int k = 0; k < (pointTo - pointFrom); k++) {
					if (k != 0) {
						fNByte += 4;
					}
					byte[] floatBytes = new byte[]{bytes[fNByte], bytes[fNByte + 1], bytes[fNByte + 2], bytes[fNByte + 3]};
					Float y = floatConverter.convert(floatBytes);

					series.getData().add(new XYChart.Data(pointFrom + k, y));
					//System.out.println(pointFrom + k + " " + y);
				}
				lineChart.getData().add(series);
			}
		}
	}

	private void setTickUnit(int length) {
		int lengthOfPart = length / 10;
		int value = 1;
		if(lengthOfPart > 5  && lengthOfPart < 10) {
			value = 5;
		} else if (lengthOfPart >= 10  && lengthOfPart <= 50) {
			value = 10;
		} else if (lengthOfPart >= 50  && lengthOfPart <= 100) {
			value = 50;
		} else if (lengthOfPart >= 100  && lengthOfPart <= 500) {
			value = 100;
		} else if (lengthOfPart >= 500  && lengthOfPart <= 1000) {
			value = 500;
		} else if (lengthOfPart >= 1000  && lengthOfPart <= 10000) {
			value = 1000;
		} else if (lengthOfPart >= 10000 ) {
			value = 10000;
		}
		xAxis.setTickUnit(value);
	}
}
