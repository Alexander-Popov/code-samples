package nstu.geo.core.infrastructure.service.file;

import java.io.File;
import java.io.FileInputStream;

public final class FileToBytesConverter {
    public byte[] convert(File file) {
        byte[] bytes = new byte[(int) file.length()];
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
