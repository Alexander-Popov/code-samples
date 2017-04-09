package nstu.geo.core.application.command.segy;


import nstu.geo.core.application.command.Handler;
import nstu.geo.core.domain.model.segy.SegyFile;
import nstu.geo.core.domain.model.segy.SegyFileFabric;
import nstu.geo.core.domain.service.segy.convertor.SegyToIeeeFormatConverter;
import nstu.geo.core.infrastructure.service.segy.converter.SegyFileToIEEEFormatConverter;

public final class ConvertSegyToIeeeFormatHandler implements Handler<ConvertSegyToIeeeFormatCommand> {

    public void handle(final ConvertSegyToIeeeFormatCommand command) {
        SegyFile segyFile = SegyFileFabric.createNew(command.getFile());
        SegyToIeeeFormatConverter converter = new SegyFileToIEEEFormatConverter(segyFile, command.getFileToSave(), command.getIncreaseDiscretizationOn());
        converter.convert();
    }
}
