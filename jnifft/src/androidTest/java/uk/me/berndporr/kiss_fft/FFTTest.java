package uk.me.berndporr.kiss_fft;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bernd Porr on 31/12/17.
 */

@RunWith(AndroidJUnit4.class)
public class FFTTest {

    KISSFastFourierTransformer kissFastFourierTransformer;

    FastFourierTransformer fastFourierTransformer;

    @Test
    public void compareWithMathCommons() throws Exception {
        kissFastFourierTransformer = new KISSFastFourierTransformer();
        fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        assertFalse(kissFastFourierTransformer == null);

        final int testsize = 64;
        Complex[] indata = new Complex[testsize];
        for(int i=0;i<testsize;i++) indata[i] = new Complex(Math.sin(0.2*Math.PI*(float)i));
        Complex[] outdata1 = kissFastFourierTransformer.transform(indata, TransformType.FORWARD);
        Complex[] outdata2 = fastFourierTransformer.transform(indata, TransformType.FORWARD);
        for(int i=0;i<testsize;i++) {
            double err = outdata1[i].subtract(outdata2[i]).abs();
            Log.d("FFTTest", ""+i+
                    " ("+outdata1[i].getReal()+","+outdata1[i].getImaginary()+") -- "+
                    " ("+outdata2[i].getReal()+","+outdata2[i].getImaginary()+") = "+err);
            assertTrue(err<1E-5);
        }
    }


    @Test
    public void doFFTandIFFT() throws Exception {
        kissFastFourierTransformer = new KISSFastFourierTransformer();
        assertFalse(kissFastFourierTransformer == null);

        final int testsize = 64;
        Complex[] indata = new Complex[testsize];
        for(int i=0;i<testsize;i++) indata[i] = new Complex(Math.random()-0.5);
        Complex[] outdata1 = kissFastFourierTransformer.transform(indata, TransformType.FORWARD);
        Complex[] outdata2 = kissFastFourierTransformer.transform(outdata1, TransformType.INVERSE);
        for(int i=0;i<testsize;i++) {
            double err = Math.abs(indata[i].getReal() - outdata2[i].getReal());
            Log.d("FFTTest", ""+i+
                    " ("+indata[i].getReal()+","+indata[i].getImaginary()+") -- "+
                    " ("+outdata2[i].getReal()+","+outdata2[i].getImaginary()+") = "+err);
            assertTrue(err<1E-5);
            assertTrue(Math.abs(outdata2[i].getImaginary())<1E-5);
        }
    }


}