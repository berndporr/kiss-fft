package uk.me.berndporr.kiss_fft;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Bernd Porr, mail@berndporr.me.uk on 31/12/17.
 * <p>
 * Test class which compares the results with the apache FFT
 * library and does an FFT / IFFT test to see if the result
 * is the same again (within an error margin).
 */

@RunWith(AndroidJUnit4.class)
public class FFTTest {

    final String TAG = FFTTest.class.getSimpleName();

    private KISSFastFourierTransformer kissFastFourierTransformer;

    @Test
    public void compareWithMathCommons() {
        kissFastFourierTransformer = new KISSFastFourierTransformer();
        FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        assertNotNull(kissFastFourierTransformer);

        final int testsize = 64;
        Complex[] indata = new Complex[testsize];
        for (int i = 0; i < testsize; i++)
            indata[i] = new Complex(Math.random() - 0.5);
        Complex[] outdata1 = kissFastFourierTransformer.transform(indata, TransformType.FORWARD);
        Complex[] outdata2 = fastFourierTransformer.transform(indata, TransformType.FORWARD);
        for (int i = 0; i < testsize; i++) {
            double err = outdata1[i].subtract(outdata2[i]).abs();
            Log.d(TAG, "" + i +
                    " (" + outdata1[i].getReal() + "," + outdata1[i].getImaginary() + ") -- " +
                    " (" + outdata2[i].getReal() + "," + outdata2[i].getImaginary() + ") = " + err);
            assertTrue(err < 1E-10);
        }
    }


    @Test
    public void doFFTandIFFT() {
        kissFastFourierTransformer = new KISSFastFourierTransformer();

        final int testsize = 64;
        double[] indata = new double[testsize];
        for (int i = 0; i < testsize; i++) indata[i] = Math.random() - 0.5;
        Complex[] outdata1 = kissFastFourierTransformer.transform(indata);
        Complex[] outdata2 = kissFastFourierTransformer.transform(outdata1, TransformType.INVERSE);
        for (int i = 0; i < testsize; i++) {
            double err = Math.abs(indata[i] - outdata2[i].getReal());
            Log.d(TAG, "" + i +
                    " (" + indata[i] + ",0) -- " +
                    " (" + outdata2[i].getReal() + "," + outdata2[i].getImaginary() + ") = " + err);
            assertTrue(err < 1E-15);
            assertTrue(Math.abs(outdata2[i].getImaginary()) < 1E-15);
        }
    }


    @Test
    public void doDCTest() {
        kissFastFourierTransformer = new KISSFastFourierTransformer();

        final int testsize = 64;
        Double[] indata = new Double[testsize];
        for (int i = 0; i < testsize; i++) indata[i] = 1.0;
        Complex[] outdata1 = kissFastFourierTransformer.transform(indata);
        assertTrue(Math.abs(outdata1[0].getReal() - (double)testsize)<1E-10);
    }


}
