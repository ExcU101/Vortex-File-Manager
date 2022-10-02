#include "jni.h"

template<typename T>
static T cast(jlong address) {
    return reinterpret_cast<T>(static_cast<uintptr_t>(address));
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_channel_UnixBufferCalls_putImpl(
        JNIEnv *env,
        jobject thiz,
        jlong address,
        jbyte byte
) {
    *cast<jbyte *>(address) = byte;
}

extern "C"
JNIEXPORT jbyte JNICALL
Java_io_github_excu101_filesystem_unix_channel_UnixBufferCalls_getImpl(
        JNIEnv *env,
        jobject thiz,
        jlong address
) {
    return *cast<const jbyte *>(address);
}