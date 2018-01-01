#include <jni.h>
#include <android/log.h>
#include "../../../../kiss_fft.h"

extern "C" {

#define TAG "KISSFFT"

JNIEXPORT jobjectArray JNICALL
Java_uk_me_berndporr_kiss_1fft_KISSFastFourierTransformer_dofft(JNIEnv *env, jobject,
                                                                jobjectArray data,
                                                                jint is_inverse) {

    jclass complex = env->FindClass("org/apache/commons/math3/complex/Complex");
    jmethodID getImaginary = env->GetMethodID(complex, "getImaginary", "()D");
    jmethodID getReal = env->GetMethodID(complex, "getReal", "()D");

    int n = env->GetArrayLength(data);

    if (n < 1) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "FFT array length < 1!");
        return NULL;
    }

    kiss_fft_cfg cfg = kiss_fft_alloc(n, is_inverse, 0, 0);
    kiss_fft_cpx *inArray = new kiss_fft_cpx[n];
    kiss_fft_cpx *outArray = new kiss_fft_cpx[n];


    for (int j = 0; j < n; j++) {
        jobject one = env->GetObjectArrayElement(data, j);
        double re = env->CallDoubleMethod(one, getReal);
        double im = env->CallDoubleMethod(one, getImaginary);
        inArray[j].r = (float) re;
        inArray[j].i = (float) im;
    }

    kiss_fft(cfg, inArray, outArray);

    jobjectArray ret = (jobjectArray) env->NewObjectArray(n, complex, NULL);
    jmethodID complexDoubleInit = env->GetMethodID(complex, "<init>", "(DD)V");

    for (int j = 0; j < n; j++) {
        double re = outArray[j].r;
        double im = outArray[j].i;
        jobject cObj = env->NewObject(complex, complexDoubleInit, re, im);
        env->SetObjectArrayElement(ret, j, cObj);
    }

    free(cfg);
    delete inArray;
    delete outArray;

    return ret;
}

}
