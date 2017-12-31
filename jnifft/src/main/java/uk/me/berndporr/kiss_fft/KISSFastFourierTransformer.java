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

    private Complex inputData;
    private Complex[] outputData;

    public Complex[] transform(Complex[] complex, TransformType transformType) {
        return dofft(complex);
    }

    private native Complex[] dofft(Complex[] data);

}
