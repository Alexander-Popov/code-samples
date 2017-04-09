package nstu.geo.core.infrastructure.service.segy.converter;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;

import nstu.geo.core.domain.service.segy.convertor.SegyToIeeeFormatConverter;
import nstu.geo.core.infrastructure.service.spline.SegySplineInterpolator;
import nstu.geo.core.infrastructure.service.utils.convertor.IbmFloatToIeeeFloatConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import nstu.geo.core.domain.model.segy.SegyFile;
import nstu.geo.core.domain.model.segy.Parameters;

public final class SegyFileToIEEEFormatConverter implements SegyToIeeeFormatConverter{
    private byte[] file;
    private byte[] convertedFile;
    private SegyFile segyFile;
    private IbmFloatToIeeeFloatConverter ibmFloatToIeee = new IbmFloatToIeeeFloatConverter();
    private File fileToSave;
    private Integer increaseDiscretizationOn;

    public SegyFileToIEEEFormatConverter(
            final SegyFile segyFile,
            File fileToSave,
            final int increaseDiscretizationOn
    ) {
        this.increaseDiscretizationOn = increaseDiscretizationOn;
        file = segyFile.getFileBytes();
        this.fileToSave = fileToSave;
        int lengthOfNewData = (int) segyFile.getNumberOfDataRecords() * (segyFile.getNumOfReportsForOneTrace() * 4 * increaseDiscretizationOn - segyFile.getNumOfReportsForOneTrace() * 4);
        convertedFile = new byte[(int) segyFile.getFile().length() + lengthOfNewData]; //создаём массив для последующего заполнения
        this.segyFile = segyFile;
    }

    public void convert() {
        changeHeader();
        changeTraces();
        save();
    }

    //Меняем заголовок файла
    private void changeHeader()
    {
        System.arraycopy(file, 0, convertedFile, 0, Parameters.COMMON_INFO_HEADER_SIZE); //Добавляем общую информацию
        int cISize = Parameters.COMMON_INFO_HEADER_SIZE;
        for(int i = cISize + 1; i < cISize + 13;) { //добавляем в массив байты с 3201 по 3212
            byte[] bytes = new byte[]{file[i], file[i + 1], file[i + 2], file[i + 3]};
            System.arraycopy(formatBytes(bytes),0, convertedFile, i, 4);
            i = i + 4;
        }
        for(int i = cISize + 13; i < cISize + 61;) {  //добавляем в массив байты с 3213 по 3260
            byte[] bytes = new byte[]{file[i],file[i + 1]};
            System.arraycopy(formatBytes(bytes),0, convertedFile, i, 2);
            i = i + 2;
        }

        byte[] newCode = new byte[]{Parameters.IEEE_FORMAT, 0}; //изменяем код формата (переписанный с оригинала в предыдущем цикле) на 5й
        System.arraycopy(newCode,0, convertedFile, cISize + 25, 2);

        short newDiscretization = (short) (segyFile.getDiscretizationTime() / increaseDiscretizationOn); //дискретизация
        ByteBuffer buffer = ByteBuffer.allocate(2).putShort(newDiscretization);
        byte[] newDiscretizationArr = buffer.array();
        ArrayUtils.reverse(newDiscretizationArr);
        System.arraycopy(newDiscretizationArr,0, convertedFile, cISize + 17, 2);

        short newNumOfReportsForOneTrace = (short) (segyFile.getNumOfReportsForOneTrace() * increaseDiscretizationOn); //колличество отчётов на трассу
        buffer = ByteBuffer.allocate(2).putShort(newNumOfReportsForOneTrace);
        byte[] newNumOfReportsForOneTraceArr = buffer.array();
        ArrayUtils.reverse(newNumOfReportsForOneTraceArr);
        System.arraycopy(newNumOfReportsForOneTraceArr,0, convertedFile, cISize + 21, 2);
    }

    private void changeTraces()
    {
        long numOfRecords = segyFile.getNumberOfDataRecords();
        for(int i = 1; i <= numOfRecords; i++) {
            changeTraceByNumber(i);
        }
    }

    //Заполняем данными запись n
    private void changeTraceByNumber(int n) {
        int header = Parameters.TRACE_HEADER_SIZE;
        int numOfReportsBytesForOneTrace = segyFile.getNumOfReportsForOneTrace() * 4;
        int numOfReportsBytesForOneTraceFinal = segyFile.getNumOfReportsForOneTrace() * 4 * this.increaseDiscretizationOn;
        int step = header + numOfReportsBytesForOneTrace;
        int stepFinal = header + numOfReportsBytesForOneTraceFinal;
        int startByte = Parameters.HEADER_SIZE + (n-1) * step ; //первый байт трассы
        int startByteFinal = Parameters.HEADER_SIZE + (n-1) * stepFinal ; //первый байт трассы
        int dataStartByte = startByte + header; //первый байт трассы
        int dataStartByteFinal = startByteFinal + header; //первый байт трассы

        //Заполняем информацией заголовок трассы
        int iF = startByteFinal + 1;
        for(int i = startByte + 1; i < startByte + 29;) { //добавляем в заголовок трассы байты с 1 по 29
            byte[] bytes = new byte[]{file[i], file[i + 1], file[i + 2], file[i + 3]};
            System.arraycopy(formatBytes(bytes),0, convertedFile, iF, 4);
            i = i + 4;
            iF = i + 4;
        }
        iF = startByteFinal + 1;
        for(int i = startByte + 29; i < startByte + 37;) { //добавляем в заголовок трассы байты с 29 по 37
            byte[] bytes = new byte[]{file[i], file[i + 1]};
            System.arraycopy(formatBytes(bytes),0, convertedFile, iF, 2);
            i = i + 2;
            iF = i + 2;
        }
        iF = startByteFinal + 1;
        for(int i = startByte + 37; i < startByte + 69;) { //добавляем в заголовок трассы байты с 37 по 69
            byte[] bytes = new byte[]{file[i], file[i + 1], file[i + 2], file[i + 3]};
            System.arraycopy(formatBytes(bytes),0, convertedFile, iF, 4);
            i = i + 4;
            iF = i + 4;
        }
        iF = startByteFinal + 1;
        for(int i = startByte + 69; i < startByte + 73;) { //добавляем в заголовок трассы байты с 69 по 73
            byte[] bytes = new byte[]{file[i], file[i + 1]};
            System.arraycopy(formatBytes(bytes),0, convertedFile, iF, 2);
            i = i + 2;
            iF = i + 2;
        }
        iF = startByteFinal + 1;
        for(int i = startByte + 73; i < startByte + 89;) { //добавляем в заголовок трассы байты с 73 по 89
            byte[] bytes = new byte[]{file[i], file[i + 1], file[i + 2], file[i + 3]};
            System.arraycopy(formatBytes(bytes),0, convertedFile, iF, 4);
            i = i + 4;
            iF = i + 4;
        }
        iF = startByteFinal + 1;
        for(int i = startByte + 89; i < startByte + 181;) { //добавляем в заголовок трассы байты с 89 по 181
            byte[] bytes = new byte[]{file[i], file[i + 1]};
            System.arraycopy(formatBytes(bytes),0, convertedFile, iF, 2);
            i = i + 2;
            iF = i + 2;
        }
        iF = startByteFinal + 1;
        if (increaseDiscretizationOn != 1) {
            short newDiscretization = (short) (segyFile.getDiscretizationTime() / increaseDiscretizationOn); //дискретизация
            ByteBuffer buffer = ByteBuffer.allocate(2).putShort(newDiscretization);
            byte[] newDiscretizationArr = buffer.array();
            ArrayUtils.reverse(newDiscretizationArr);
            System.arraycopy(newDiscretizationArr,0, convertedFile, iF + 115, 2);

            short newNumOfReportsForOneTrace = (short) (segyFile.getNumOfReportsForOneTrace() * increaseDiscretizationOn); //колличество отчётов на трассу
            buffer = ByteBuffer.allocate(2).putShort(newNumOfReportsForOneTrace);
            byte[] newNumOfReportsForOneTraceArr = buffer.array();
            ArrayUtils.reverse(newNumOfReportsForOneTraceArr);
            System.arraycopy(newNumOfReportsForOneTraceArr,0, convertedFile, iF + 117, 2);
        }

        //Теперь добавляем сами данные
        if (this.increaseDiscretizationOn == 1) {
            for (int k = header + 1; k < step; ) {
                byte[] bytes = new byte[]{
                        file[startByte + k],
                        file[startByte + k + 1],
                        file[startByte + k + 2],
                        file[startByte + k + 3]
                };
                if (segyFile.isIbmFormat()) {
                    bytes = ibmFloatToIeee.convert(bytes);
                }
                System.arraycopy(formatBytes(bytes), 0, convertedFile, startByte + k, 4);
                k = k + 4;
            }
        } else {
            byte[] slice = Arrays.copyOfRange(file, dataStartByte + 1, dataStartByte + segyFile.getNumOfReportsForOneTrace() * 4 + 1);
            byte[] discretiziedSlice = getIncreasedDiscretization(slice);
            System.arraycopy(discretiziedSlice, 0, convertedFile, dataStartByteFinal + 1, segyFile.getNumOfReportsForOneTrace() * 4 * increaseDiscretizationOn);

            /*
            int partSize = 100;
            int numOfParts = segyFile.getNumOfReportsForOneTrace() / partSize;
            int rest = segyFile.getNumOfReportsForOneTrace() - partSize * numOfParts;
            for (int p = 1; p < numOfParts; p++) {
                byte[] slice = Arrays.copyOfRange(file, dataStartByte, dataStartByte + 4 * partSize * p);
                byte[] discretiziedSlice = getIncreasedDiscretization(slice);
                System.arraycopy(discretiziedSlice, 0, convertedFile, dataStartByte + 4 * partSize * p, 4 * partSize);
            }
            if (rest != 0) {
                byte[] slice = Arrays.copyOfRange(file, dataStartByte, dataStartByte + 4 * partSize * numOfParts);
                byte[] discretiziedSlice =  getIncreasedDiscretization(slice);
                System.arraycopy(discretiziedSlice, 0, convertedFile, dataStartByte + 4 * partSize * numOfParts, rest * 4);
            }
            */
        }
    }

    private byte[] getIncreasedDiscretization(byte[] bytes) {
        byte[] result = new byte[bytes.length * this.increaseDiscretizationOn];
        int numOfFloats = bytes.length / 4;
        int[] xs = new int[numOfFloats];
        float[] ys = new float[numOfFloats];
        int index = 0;
        SegyFloatConverter floatConverter = new SegyFloatConverter(segyFile);

        for (int k = 0; k < bytes.length;) {
            byte[] value = new byte[]{
                    bytes[k],
                    bytes[k + 1],
                    bytes[k + 2],
                    bytes[k + 3]
            };
            xs[index] = index;
            ys[index] = floatConverter.convert(value);
            k = k + 4;
            index++;
        }
        SegySplineInterpolator interpolator = new SegySplineInterpolator();
        interpolator.interpolate(xs, ys);
        int newX = 0;
        for(double i = 0; i < numOfFloats - 1;) {
            byte[] floatArr = ByteBuffer.allocate(4).putFloat((float) interpolator.value(i)).array();
            ArrayUtils.reverse(floatArr);
            result[newX * 4] = floatArr[0];
            result[newX * 4 + 1] = floatArr[1];
            result[newX * 4 + 2] = floatArr[2];
            result[newX * 4 + 3] = floatArr[3];
            i = i + (double) 1 / (double) this.increaseDiscretizationOn;
            newX++;

        }
        return result;
    }

    private void save()
    {
        try {
            FileUtils.writeByteArrayToFile(fileToSave, convertedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] formatBytes(byte[] bytes) {
        if (!segyFile.isLittleEndian()) {
            ArrayUtils.reverse(bytes);
        }
        return bytes;
    }
}
