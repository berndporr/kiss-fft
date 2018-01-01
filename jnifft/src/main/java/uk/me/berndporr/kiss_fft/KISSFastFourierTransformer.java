package uk.me.berndporr.kiss_fft;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.TransformType;

/**
 * Created by Bernd Porr on 31/12/17.
 */

public class KISSFastFourierTransformer {

    static {
        System.loadLibrary("kiss-fft-lib");
    }

    public Complex[] transform(Complex[] complex, TransformType transformType) {
        int is_inverse = 0;
        if (transformType == TransformType.INVERSE) is_inverse = 1;
        return dofft(complex,is_inverse);
    }

    private native Complex[] dofft(Complex[] data, int is_inverse);

}
