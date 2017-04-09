package nstu.geo.presentation.helper.view;

public class SegyFileViewHelper {
    public static String getFileFormatCodeDescription(short fileFormatCode) {
        if(fileFormatCode == 1) {
            return "IBM плавающая запятая (4 байта)";
        }
        if(fileFormatCode == 2) {
            return "фиксированная запятая (4 байта)";
        }
        if(fileFormatCode == 3) {
            return "фиксированная запятая (2 байта)";
        }
        if(fileFormatCode == 4) {
            return "фиксированная запятая (без кода)";
        }
        if(fileFormatCode == 5) {
            return  "IEEE плавающая запятая (4 байта)";
        }
        return "";
    }
}
