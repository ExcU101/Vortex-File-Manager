#include <unistd.h>
#include "sys/uio.h"
#include <jni.h>
#include <cerrno>

static void clearErrno() {
    errno = 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixFileChannelCalls_readImpl__IJI(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jlong address,
        jint count
) {
    clearErrno();

    int cDescriptor = descriptor;
    long long cAddress = address;
    size_t cCount = count;
    int result = read(cDescriptor, &cAddress, cCount);

    if (errno != 0) {

    }

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixFileChannelCalls_writeImpl__IJI(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jlong address,
        jint count
) {
    clearErrno();

    int cDescriptor = descriptor;
    long long cAddress = address;
    size_t cCount = count;

    int result = write(cDescriptor, &cAddress, cCount);

    if (errno != 0) {

    }

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixFileChannelCalls_readImpl__I_3JI(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jlongArray addresses,
        jint count
) {
    // TODO: Future releases
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixFileChannelCalls_writeImpl__I_3JI(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jlongArray addresses,
        jint count
) {
    // TODO: implement writeImpl()
}