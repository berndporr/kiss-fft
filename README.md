# KISS FFT

KISS FFT - A mixed-radix Fast Fourier Transform in C with an Android
JNI wrapper

## In C

### Complex to Complex FFT and IFFT

The basic usage for 1-d complex FFT is:

```
        #include "kiss_fft.h"

        kiss_fft_cfg cfg = kiss_fft_alloc( nfft, is_inverse_fft, 0, 0 );

            // put kth sample in cx_in[k].r and cx_in[k].i
            
            kiss_fft( cfg , cx_in , cx_out );
            
            // transformed. DC is in cx_out[0].r and cx_out[0].i 
            
        free(cfg);
```

The frequency-domain data is stored from DC up to 2PI.
So cx_out[0] is the DC bin of the FFT and cx_out[nfft/2]
is the Nyquist bin (if exists).

Declarations are in "kiss_fft.h", along with a brief description of the 
functions you'll need to use.

Code definitions for 1d complex FFTs are in kiss_fft.c.

### Real valued FFT

A real valued optimized FFT which takes real valued signals as its input is implemtned in `kiss_fftr.h` and `kiss_fftr.c`. It returns the positive half-spectrum: (nfft/2+1) complex frequency bins.

## Android
Do super-fast native FFTs under Android

### Compilation
Open this project in Android studio and run "Build". Depending
on the config you'll generate a debug version of the kiss-fft
library or a release version.

### Installation
The Android library is in `jnifft/build/outputs/`.
Just import it into your Android project with "New-Module-Android Library" and add
the dependency with
```
compile project(":jnifft-release")
```

### Complex to Complex transform
```
kissFastFourierTransformer = new KISSFastFourierTransformer();
Complex[] outdata = kissFastFourierTransformer.transform(indata, TransformType.FORWARD);
```
which transforms from Complex to Complex as defined in the apache Commons.
The constant `TransformType` is also defined in apache Commons which determines
if it's a forward or inverse transform. It can be used as a direct
replacement of the apache commons FFT function.

There are also convenience functions as in the commons library for double and
Double which perform the conversion to Complex in C++ and are also very fast.

### Real to Complex and Complex to Real transform
For real valued sequences there are two optimised functions which
directly perform the FFT on the raw double bufferwithout any
conversion to complex. For real valued sequences this runs at least
twice as fast as the functions above. The complex sequence of the real sequence of length N has the length
N/2+1 and then expands back to length N by the inverse transform:
```
public Complex[] transformRealOptimisedForward(double[] v)
public double[] transformRealOptimisedInverse(Complex[] v)
```

### Unit tests
Run `FFTTest` which compares the results with that from the
apache commons FFT, does a DC test and does an ifft(fft) test to
check the rounding errors and remaining imaginary parts.


## Attribution
This is a fork from the original kiss-fft library by Mark Borgerding
<Mark@Borgerding.net>.
