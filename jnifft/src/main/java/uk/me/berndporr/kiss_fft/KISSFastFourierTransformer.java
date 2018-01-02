package uk.me.berndporr.kiss_fft;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.TransformType;

/**
 * Created by Bernd Porr, mail@berndporr.me.uk on 31/12/17.
 *
 * Fast Fourier JAVA class which calls the C KISS FFT for
 * superfast native FFT.
 */

public class KISSFastFourierTransformer {

    static {
        System.loadLibrary("kiss-fft-lib");
    }

    public Complex[] transform(Complex[] complex, TransformType transformType) {
        return dofft(complex,transformtype2Int(transformType));
    }

    public Complex[] transform(double[] v, TransformType transformType) {
        return dofftdouble(v,transformtype2Int(transformType));
    }

    public Complex[] transform(Double[] v, TransformType transformType) {
        int n = v.length;
        double[] cv = new double[n];
        for(int i=0;i<n;i++) {
            cv[i] = v[i];
        }
        return dofftdouble(cv,transformtype2Int(transformType));
    }

    private native Complex[] dofft(Complex[] data, int is_inverse);

    private native Complex[] dofftdouble(double[] data, int is_inverse);

    private int transformtype2Int(TransformType transformType) {
        int i = 0;
        if (transformType == TransformType.INVERSE) i = 1;
        return i;
    }

}
