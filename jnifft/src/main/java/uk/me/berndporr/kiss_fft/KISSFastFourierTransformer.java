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

    public Complex[] transform(Complex[] input, TransformType transformType) {
        double[] ri = new double[input.length*2];
        int idx=0;
        for(Complex c:input) {
            ri[idx++] = c.getReal();
            ri[idx++] = c.getImaginary();
        }
        ri = dofft(ri,transformtype2Int(transformType));
        Complex[] result = new Complex[input.length];
        idx = 0;
        for(int i=0;i<input.length;i++) {
            result[i] = new Complex(ri[idx++],ri[idx++]);
        }
        return result;
    }

    public Complex[] transform(double[] v) {
        return dofftdouble(v,0);
    }

    public Complex[] transform(Double[] v) {
        int n = v.length;
        double[] cv = new double[n];
        for(int i=0;i<n;i++) {
            cv[i] = v[i];
        }
        return dofftdouble(cv,0);
    }

    public Complex[] transformRealOptimisedForward(double[] v) {
        return dofftr(v);
    }

    public double[] transformRealOptimisedInverse(Complex[] v) {
        return dofftri(v);
    }


    private native double[] dofft(double[] data, int is_inverse);

    private native Complex[] dofftdouble(double[] data, int is_inverse);

    private native Complex[] dofftr(double[] data);

    private native double[] dofftri(Complex[] data);

    private int transformtype2Int(TransformType transformType) {
        int i = 0;
        if (transformType == TransformType.INVERSE) i = 1;
        return i;
    }

}
