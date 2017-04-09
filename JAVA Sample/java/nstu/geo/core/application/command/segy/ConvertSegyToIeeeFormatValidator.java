package nstu.geo.core.application.command.segy;

import nstu.geo.core.application.command.Validator;
import nstu.geo.core.infrastructure.exeption.ValidationException;

import java.io.File;

public final class ConvertSegyToIeeeFormatValidator implements Validator<ConvertSegyToIeeeFormatCommand> {
    public void validate(final ConvertSegyToIeeeFormatCommand command) throws ValidationException {
        this.guardFileExists(command.getFile());
    }

    private void guardFileExists(final File file) throws ValidationException {
        if (!file.exists()) {
            throw new ValidationException("Файл не существует.");
        }
    }

}
