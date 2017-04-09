package nstu.geo.core.domain.model.segy;


import java.util.ArrayList;
import java.util.List;

public final class Parameters {
    /**
     * Текст свободного формата
     * Стандартный заголовок 3200 байт
     * Однако, в Java, при чтение файла, нужно сдвинуть всё на один байт и
     * что бы везде не отнимать единицу, сделаем это здесь.
     */
    public final static int COMMON_INFO_HEADER_SIZE = 3199;
    public final static int HEADER_SIZE = COMMON_INFO_HEADER_SIZE + 400;
    public final static int TRACE_HEADER_SIZE = 240;
    public static final int IEEE_FORMAT = 5;
    public static final int IBM_FORMAT = 1;

    /**
     * 1 = IBM плавающая запятая (4 байта)
     * 2 = фиксированная запятая (4 байта)
     * 3 = фиксированная запятая (2 байта)
     * 4 = фиксированная запятая (без кода) (4 байта)
     * 5 = IEEE плавающая запятая (4 байта). Приводим в такой вид при конвертации.
     * 6 = фиксированная запятая (1 байт). Не поддерживаем.
     */
    public static  List<Short> getFormats() {
        List<Short> formats = new ArrayList<>();
        formats.add((short) 1);
        formats.add((short) 2);
        formats.add((short) 3);
        formats.add((short) 4);
        formats.add((short) 5);
        return formats;
    }

}
