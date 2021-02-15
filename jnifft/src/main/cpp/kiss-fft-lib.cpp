#include <jni.h>
#include <android/log.h>
#include "../../../../kiss_fft.h"
#include "../../../../kiss_fftr.h"

extern "C" {

#define TAG "KISSFFT"

JNIEXPORT jdoubleArray
Java_uk_me_berndporr_kiss_1fft_KISSFastFourierTransformer_dofft(JNIEnv *env, jobject,
                                                                jdoubleArray inArray,
                                                                jint is_inverse) {

    if (inArray == NULL) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "inArray has nullptr.");
        return NULL;
    }

    int n = env->GetArrayLength(inArray)/2;

    if (n < 1) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "FFT array length < 1!");
        return NULL;
    }

    kiss_fft_cfg cfg = kiss_fft_alloc(n, is_inverse);

    double *inValues = env->GetDoubleArrayElements(inArray, 0);

    jdoubleArray outArray = env->NewDoubleArray(n*2);
    double *outValues = env->GetDoubleArrayElements(outArray, 0);

    kiss_fft(cfg, (kiss_fft_cpx*)inValues, (kiss_fft_cpx*)outValues);

    env->ReleaseDoubleArrayElements(outArray, outValues, 0);
    env->ReleaseDoubleArrayElements(inArray, inValues, 0);

    free(cfg);

    return outArray;
}


JNIEXPORT jobjectArray
Java_uk_me_berndporr_kiss_1fft_KISSFastFourierTransformer_dofftdouble(JNIEnv *env, jobject,
                                                                      jdoubleArray data,
                                                                      jint is_inverse) {

    jclass complex = env->FindClass("org/apache/commons/math3/complex/Complex");

    if (data == NULL) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "data has nullptr.");
        return NULL;
    }

    int n = env->GetArrayLength(data);

    if (n < 1) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "FFT array length < 1!");
        return NULL;
    }

    kiss_fft_cfg cfg = kiss_fft_alloc(n, is_inverse);
    kiss_fft_cpx *inArray = new kiss_fft_cpx[n];
    kiss_fft_cpx *outArray = new kiss_fft_cpx[n];

    jdouble *values = env->GetDoubleArrayElements(data, 0);

    for (int j = 0; j < n; j++) {
        inArray[j].r = values[j];
        inArray[j].i = 0;
    }

    env->ReleaseDoubleArrayElements(data, values, 0);

    kiss_fft(cfg, inArray, outArray);

    jobjectArray ret = (jobjectArray) env->NewObjectArray(n, complex, NULL);
    jmethodID complexDoubleInit = env->GetMethodID(complex, "<init>", "(DD)V");

    for (int j = 0; j < n; j++) {
        double re = outArray[j].r;
        double im = outArray[j].i;
        jobject cObj = env->NewObject(complex, complexDoubleInit, re, im);
        env->SetObjectArrayElement(ret, j, cObj);
        env->DeleteLocalRef(cObj);
    }

    free(cfg);
    delete[] inArray;
    delete[] outArray;

    return ret;
}


JNIEXPORT jobjectArray
Java_uk_me_berndporr_kiss_1fft_KISSFastFourierTransformer_dofftr(JNIEnv *env, jobject,
                                                                 jdoubleArray data) {

    jclass complex = env->FindClass("org/apache/commons/math3/complex/Complex");

    if (data == NULL) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "data has nullptr.");
        return NULL;
    }

    int n = env->GetArrayLength(data);

    if (n < 1) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "FFT array length < 1!");
        return NULL;
    }

    int is_inverse = 0;
    kiss_fftr_cfg cfg = kiss_fftr_alloc(n, is_inverse);
    kiss_fft_cpx *outArray = new kiss_fft_cpx[n];

    double *values = env->GetDoubleArrayElements(data, 0);

    kiss_fftr(cfg, values, outArray);

    env->ReleaseDoubleArrayElements(data, values, 0);

    int complex_data_points = n / 2 + 1;

    jobjectArray ret = (jobjectArray) env->NewObjectArray(complex_data_points, complex, NULL);
    jmethodID complexDoubleInit = env->GetMethodID(complex, "<init>", "(DD)V");

    for (int j = 0; j < complex_data_points; j++) {
        double re = outArray[j].r;
        double im = outArray[j].i;
        jobject cObj = env->NewObject(complex, complexDoubleInit, re, im);
        env->SetObjectArrayElement(ret, j, cObj);
        env->DeleteLocalRef(cObj);
    }

    free(cfg);
    delete[] outArray;

    return ret;
}


JNIEXPORT jdoubleArray
Java_uk_me_berndporr_kiss_1fft_KISSFastFourierTransformer_dofftri(JNIEnv *env, jobject,
                                                                  jobjectArray data) {

    jclass complex = env->FindClass("org/apache/commons/math3/complex/Complex");
    jmethodID getImaginary = env->GetMethodID(complex, "getImaginary", "()D");
    jmethodID getReal = env->GetMethodID(complex, "getReal", "()D");

    if (data == NULL) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "data has nullptr.");
        return NULL;
    }

    int n = env->GetArrayLength(data);

    if (n < 1) {
        __android_log_write(ANDROID_LOG_ERROR, TAG, "FFT array length < 1!");
        return NULL;
    }

    int is_inverse = 1;
    // length of real sequence
    int real_data_points = 2 * n - 2;

    kiss_fftr_cfg cfg = kiss_fftr_alloc(real_data_points, is_inverse);
    kiss_fft_cpx *inArray = new kiss_fft_cpx[n];

    for (int j = 0; j < n; j++) {
        jobject one = env->GetObjectArrayElement(data, j);
        double re = 0;
        double im = 0;
        if (!(env->IsSameObject(one, NULL))) {
            re = env->CallDoubleMethod(one, getReal);
            im = env->CallDoubleMethod(one, getImaginary);
        }
        inArray[j].r = re;
        inArray[j].i = im;
        env->DeleteLocalRef(one);
    }

    jdoubleArray outArray = env->NewDoubleArray(real_data_points);
    double *outValues = env->GetDoubleArrayElements(outArray, 0);

    // inverse transform assuming that the complex numbers are complex conjugate
    kiss_fftri(cfg, inArray, outValues);

    env->ReleaseDoubleArrayElements(outArray, outValues, 0);

    free(cfg);
    delete[] inArray;

    return outArray;
}


}
