# KISS FFT

KISS FFT - A mixed-radix Fast Fourier Transform based up on the principle, 
"Keep It Simple, Stupid."

There are many great fft libraries already around. Kiss FFT is not trying
to be better than any of them. It only attempts to be a reasonably efficient, 
moderately useful FFT that can use fixed or floating data types and can be 
incorporated into someone's C program in a few minutes with trivial licensing.

## Basic usage

    The basic usage for 1-d complex FFT is:

```
        #include "kiss_fft.h"

        kiss_fft_cfg cfg = kiss_fft_alloc( nfft ,is_inverse_fft ,0,0 );

            // put kth sample in cx_in[k].r and cx_in[k].i
            
            kiss_fft( cfg , cx_in , cx_out );
            
            // transformed. DC is in cx_out[0].r and cx_out[0].i 
            
        free(cfg);
```

Note: frequency-domain data is stored from dc up to 2pi.
So cx_out[0] is the dc bin of the FFT and cx_out[nfft/2]
is the Nyquist bin (if exists).

Declarations are in "kiss_fft.h", along with a brief description of the 
functions you'll need to use.

Code definitions for 1d complex FFTs are in kiss_fft.c.

## Advanced topics

You can do other cool stuff with the extras you'll find in `tools/`

* multi-dimensional FFTs 
* real-optimized FFTs  (returns the positive half-spectrum: (nfft/2+1) complex frequency bins)
* fast convolution FIR filtering (not available for fixed point)
* spectrum image creation

The core fft and most `tools/` code can be compiled using float, double,
 Q15 short or Q31 samples. The default is float.


## Attribution
This is a fork from the original kiss-fft library by Mark Borgerding
<Mark@Borgerding.net>.