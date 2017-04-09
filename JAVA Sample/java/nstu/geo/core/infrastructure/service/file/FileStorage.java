package nstu.geo.core.infrastructure.service.file;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.Timestamp;
import java.util.Date;

public final class FileStorage {
    private static SecureRandom random = new SecureRandom();
    public static String tempDirectoryPath = "data/temp/";

    public static String generateSgyTempName() {
        return "temp_" + new BigInteger(130, random).toString(32) + ".sgy";
    }
    public static String generateSgyTimeName() {
        return (new Date()).getTime() + new BigInteger(130, random).toString(32) + ".sgy";
    }
    public static String getGeneratedSgyNameWithTempPath() {
        return tempDirectoryPath + generateSgyTempName();
    }
}
