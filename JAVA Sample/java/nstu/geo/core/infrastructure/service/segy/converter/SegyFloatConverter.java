package nstu.geo.core.infrastructure.service.segy.converter;


import nstu.geo.core.domain.model.segy.SegyFile;
import nstu.geo.core.infrastructure.service.utils.convertor.IbmFloatToIeeeFloatConverter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;

public class SegyFloatConverter {
    private final SegyFile segyFile;
    private final IbmFloatToIeeeFloatConverter ibmToIeee = new IbmFloatToIeeeFloatConverter();
    public SegyFloatConverter(SegyFile segyFile) {
        this.segyFile = segyFile;
    }

    public Float convert(byte[] bytes) {
        if (segyFile.isIeeeFormat()) {
            return convertBytesToFloat(bytes);
        }
        if (segyFile.isIbmFormat()) {
            return convertBytesToFloat(ibmToIeee.convert(bytes));
        }
        throw new InvalidParameterException();
    }

    private Float convertBytesToFloat(byte[] bytes) {
        if(segyFile.isLittleEndian()){
            return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        } else {
            return ByteBuffer.wrap(bytes).getFloat();
        }
    }
}
