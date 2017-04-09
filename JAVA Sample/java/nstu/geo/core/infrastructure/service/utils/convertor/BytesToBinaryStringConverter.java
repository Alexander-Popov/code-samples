package nstu.geo.core.infrastructure.service.utils.convertor;

/* Конвертируем байты в битовую строку для дебага */
public final class BytesToBinaryStringConverter {
    public String convert(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }}
