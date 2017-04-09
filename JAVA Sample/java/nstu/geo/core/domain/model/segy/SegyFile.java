package nstu.geo.core.domain.model.segy;


import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class SegyFile {
    private File file;
    private boolean isLittleEndian;
    private short segyFormat;
    private byte[] headBytes;

    /**
     * @param file SEG-Y
     * @param isLittleEndian boolean
     * @param segyFormat формат SEG-Y
     */
    public SegyFile(
            File file,
            boolean isLittleEndian,
            short segyFormat
    ) {
        this.file = file;
        this.isLittleEndian = isLittleEndian;
        this.segyFormat = segyFormat;
    }

    /**
     * Полчаем исходный объект File
     * @return File
     */
    public File getFile() { return file; }

    /**
     * Получаем файл в виде массива байтов
     * @return byte[]
     */
    public byte[] getFileBytes() {
        byte[] bytes = new byte[(int) file.length()];
        try(FileInputStream stream = new FileInputStream(file)) {
            stream.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public boolean isIeeeAndLittleEndian()
    {
        return isLittleEndian && isIeeeFormat();
    }

    /**
     * Получаем данные конкретной сейсмотрассы (без заголовка) в виде массива байтов
     * @param index порядковый номер трассы
     * @return byte[]
     */
    public byte[] getSeismicTraceBytes(int index) {
        byte[] bytes = new byte[getNumOfDataBytesForOneTrace()  + 4]; // +4? почему выходит за пределы массива?
        int from = Parameters.HEADER_SIZE + Parameters.TRACE_HEADER_SIZE * index;
        from = from + getNumOfDataBytesForOneTrace()  * (index - 1);
        try (FileInputStream stream = new FileInputStream(file)) {
            stream.skip(from);
            stream.read(bytes, 0, getNumOfDataBytesForOneTrace());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     *
     * Получаем данные конкретной сейсмотрассы (без заголовка) в виде массива байтов
     * @param index порядковый номер трассы
     * @param pointFrom читаем с этой точки
     * @param pointTo читаем до этой точки (включительно)
     * @return byte[]
     */
    public byte[] getSeismicTraceBytes(final int index, final int pointFrom, final int pointTo) {
        int numOfBytesToRead = (pointTo - pointFrom) * 4;
        byte[] bytes = new byte[numOfBytesToRead + 4]; // +4? почему выходит за пределы массива?
        int from = Parameters.HEADER_SIZE + Parameters.TRACE_HEADER_SIZE * index;
        from = from + getNumOfDataBytesForOneTrace()  * (index - 1) + pointFrom * 4;
        try (FileInputStream stream = new FileInputStream(file)) {
            stream.skip(from);
            stream.read(bytes, 0, numOfBytesToRead);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * Получаем заголовок файла в виде массива из 3600 байтов
     * @return byte[]
     */
    public byte[] getSegyHeadBytes() {
        if(headBytes == null) {
            headBytes = new byte[Parameters.HEADER_SIZE];
            try (FileInputStream stream = new FileInputStream(file)) {
                stream.read(headBytes, 0, Parameters.HEADER_SIZE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return headBytes;
    }

    /**
     * Формат файла Ibm
     * @return boolean
     */
    public boolean isIbmFormat() {
        return segyFormat == Parameters.IBM_FORMAT;
    }

    /**
     * Формат файла Ieee. Порядок байтов не имеет значения.
     * @return boolean
     */
    public boolean isIeeeFormat() {
        return segyFormat == Parameters.IEEE_FORMAT;
    }

    /**
     * Проверка на порядка байтов little-endian
     * @return boolean
     */
    public boolean isLittleEndian() {
        return isLittleEndian;
    }

    /**
     * Получение кода формата файла
     * @return short
     */
    public short getSegyFormat() {
        return segyFormat;
    }

    /**
     * Общее количество записей в файле
     * @return long
     */
    public long getNumberOfDataRecords() {
        return (file.length() - Parameters.HEADER_SIZE) / (Parameters.TRACE_HEADER_SIZE + getNumOfReportsForOneTrace() * 4);
    }

    /**
     * Общее количество трасс в файле
     * @return short
     */
    public short getNumOfTraces() {
        return this.getShortFromHead(Parameters.COMMON_INFO_HEADER_SIZE + 13);
    }

    /**
     * Размер в байтах части сейсмотрассы с данными
     * @return int
     */
    private int getNumOfDataBytesForOneTrace() {
        return getNumOfReportsForOneTrace() * 4;
    }

    /**
     * Количество отчётов на одну трассу
     * @return short
     */
    public short getNumOfReportsForOneTrace() {
        return this.getShortFromHead(Parameters.COMMON_INFO_HEADER_SIZE + 21);
    }

    /**
     * Дискретизация в милисекундах
     * @return short
     */
    public short getDiscretizationTime() {
        return this.getShortFromHead(Parameters.COMMON_INFO_HEADER_SIZE + 17);
    }

    /**
     * Дополнительные заголовки
     * @return short
     */
    public short getExtendedHeaders() {
        return this.getShortFromHead(Parameters.COMMON_INFO_HEADER_SIZE + 105);
    }

    /**
     * Получает значение из заголовка файла
     * @param startByte первый (с начала файла) байт нужного значения
     * @return short
     */
    private short getShortFromHead(final int startByte) {
        byte[] bytes = new byte[]{getSegyHeadBytes()[startByte], getSegyHeadBytes()[startByte + 1]};
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        if (isLittleEndian) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return  buffer.getShort();
    }
}
