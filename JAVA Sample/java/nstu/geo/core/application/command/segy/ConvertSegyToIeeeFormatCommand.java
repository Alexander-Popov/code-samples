package nstu.geo.core.application.command.segy;


import nstu.geo.core.application.command.Command;

import java.io.File;

public final class ConvertSegyToIeeeFormatCommand implements Command {
    private File file;
    private File fileToSave;
    private int increaseDiscretizationOn;

    public ConvertSegyToIeeeFormatCommand(File file, File fileToSave, int increaseDiscretizationOn) {
        this.file = file;
        this.fileToSave = fileToSave;
        this.increaseDiscretizationOn = increaseDiscretizationOn;

    }

    public File getFile() {
        return file;
    }

    public File getFileToSave() {
        return fileToSave;
    }

    public int getIncreaseDiscretizationOn() {
        return increaseDiscretizationOn;
    }
}
