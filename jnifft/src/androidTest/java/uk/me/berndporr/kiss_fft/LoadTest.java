package uk.me.berndporr.kiss_fft;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoadTest {

    KISSFastFourierTransformer kissFastFourierTransformer;

    @Test
    public void useAppContext() throws Exception {
        kissFastFourierTransformer = new KISSFastFourierTransformer();
        assertFalse(kissFastFourierTransformer == null);
    }
}
