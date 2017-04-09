package nstu.geo.core.domain.model.segy;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;

public final class SegyFileFabric {
    private short getSegyFormat;
    private boolean littleEndian;
    private byte[] headBytes = new byte[Parameters.HEADER_SIZE];

    /**
     * @param file SEG-Y
     */
    private SegyFileFabric(final File file) {
        try(FileInputStream stream = new FileInputStream(file)) {
            stream.read(headBytes, 0, Parameters.HEADER_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        recognizeEndianAndSegyFormats();
    }

    /**
     * @param file SEG-Y
     */
    public static SegyFile createNew(final File file) {
        SegyFileFabric fabric = new SegyFileFabric(file);
        return new SegyFile(file, fabric.littleEndian, fabric.getSegyFormat);
    }

    /**
     * Распознование порядка байтов и формата SEG-Y файла
     * Необходимо помнить, что в Java всё в big endian.
     * Так, что если вам удалось успешно распознать формат сразу (первый if), то формат - big endian.
     * Валидные форматы для нас только с 1 по 5.
     */
    private void recognizeEndianAndSegyFormats() {
        littleEndian = false;
        short result = getShortFromFileHead(Parameters.COMMON_INFO_HEADER_SIZE + 25);
        short discretization = getShortFromFileHead(Parameters.COMMON_INFO_HEADER_SIZE + 17);
        if (formatIsValid(result) && discretizationIsValid(discretization)) {
            getSegyFormat = result;
            return;
        }
        littleEndian = true;
        discretization = getShortFromFileHead(Parameters.COMMON_INFO_HEADER_SIZE + 17);
        result = getShortFromFileHead(Parameters.COMMON_INFO_HEADER_SIZE + 25);
        if (formatIsValid(result) && discretizationIsValid(discretization)) {
            getSegyFormat = result;
            return;
        }
        throw new InvalidParameterException("Неизвестный тип SEG-Y файл. Валидация не пройдена");
    }

    /**
     * Проверка на существование формата
     * @param getSegyFormat формат
     * @return boolean
     */
    private boolean formatIsValid(final short getSegyFormat) {
        return Parameters.getFormats().contains(getSegyFormat);
    }

    /**
     * Проверяем корректность времени дискретизации
     * @param discretization дискретизация в милисекундах
     * @return boolean
     */
    private boolean discretizationIsValid(final short discretization) {
        return discretization > 0 && discretization < 10000;
    }

    /**
     * Получает значение из заголовка файла
     * @param startByte первый (с начала файла) байт нужного значения
     * @return short
     */
    private short getShortFromFileHead(final int startByte) {
        byte[] bytes = new byte[]{ headBytes[startByte],headBytes[startByte + 1]};
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        if (littleEndian) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return  buffer.getShort();
    }
}
