package nstu.geo.core.infrastructure.service.utils.convertor;

import java.nio.ByteBuffer;


public final class IbmFloatToIeeeFloatConverter {
    private final static int MSK_MANT_IBM = 0xffffff;
    private final static int MSK_EXP_IBM = 0x7f000000;
    private final static int MSK_SIGN_IBM = 0x80000000;
    private final static int MSK_NORM_IEEE = 0x800000;

    public byte[] convert(byte[] ibmBytes)
    {
        int fpn = ByteBuffer.wrap(ibmBytes).getInt();
        int exponent, mantissa, sign;
        if ((fpn & MSK_MANT_IBM) == 0) {
            float zero = 0;
            return ByteBuffer.allocate(4).putFloat(zero).array();
        } else {
            mantissa = (fpn & MSK_MANT_IBM);
            exponent = ((((fpn & MSK_EXP_IBM) >> 24) - 64) << 2 );
            sign = (fpn & MSK_SIGN_IBM);

            while ((mantissa & MSK_NORM_IEEE) == 0)
            {
                mantissa <<= 1;  /* normalize */
                exponent-- ;
            }
            mantissa = mantissa & 0x7fffff;    /* shift understood one out */
            exponent += 126;
            if (( exponent < 0 ) || (exponent > 255)) { /* IBM floating point exponent out of range */
                exponent = 0;
            }
            exponent <<= 23;
            float result = Float.intBitsToFloat(sign | exponent | mantissa);
            return ByteBuffer.allocate(4).putFloat(result).array();
        }
    }
}
