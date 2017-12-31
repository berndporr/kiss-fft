#include <jni.h>
#include <android/log.h>
#include "../../../../kiss_fft.h"

#define TAG "KISSFFT"

JNIEXPORT jobjectArray JNICALL Java_uk_me_berndporr_kiss_1fft_KISSFastFourierTransformer_dofft(JNIEnv *env, jobject, jobjectArray data) {

    int n = env->GetArrayLength(data);

    if (n<1) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "FFT array length < 1!");
        return NULL;
    }

    int is_inverse = 0;

    kiss_fft_cfg cfg = kiss_fft_alloc( n, is_inverse, 0, 0 );
    kiss_fft_cpx* inArray = new kiss_fft_cpx[n];
    kiss_fft_cpx* outArray = new kiss_fft_cpx[n];

    jclass complex = env->FindClass("Lorg/apache/commons/math3/complex/Complex");
    jmethodID getImaginary = env->GetMethodID(complex,"getImaginary","()D");
    jmethodID getReal = env->GetMethodID(complex,"getReal","()D");

    for(int j=0;j<n;j++) {
        jobject one = env->GetObjectArrayElement(data, j);
        double re = env->CallDoubleMethod(one, getReal);
        double im = env->CallDoubleMethod(one, getImaginary);
        inArray[j].r = (float)re;
        inArray[j].i = (float)im;
    }

    kiss_fft( cfg , inArray , outArray );

    jobjectArray ret = (jobjectArray)env->NewObjectArray(n,complex,NULL);
    jmethodID complexDoubleInit = env->GetMethodID(complex, "<init>", "(DD)V");

    for(int j=0;j<n;j++) {
        jobject cObj = env->NewObject(complex,complexDoubleInit,outArray[j].r,outArray[j].i);
        env->SetObjectArrayElement(ret,j,cObj);
    }

    free(cfg);
    delete inArray;
    delete outArray;

    return ret;
}
