#include <cstdlib>
#include "jni.h"

static jclass findClass(JNIEnv *env, const char *name) {
    jclass local = env->FindClass(name);
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);

    return global;
}

static jclass findJavaFileDescriptorClass(JNIEnv *env) {
    static jclass clazz = nullptr;
    if (!clazz) {
        clazz = env->FindClass("java/io/FileDescriptor");
    }
    return clazz;
}

static _jmethodID *findJavaFileDescriptorInitMethod(JNIEnv *env) {
    return env->GetMethodID(findJavaFileDescriptorClass(env), "<init>", "(I)V");
}

static int getIndexFromFileDescriptor(JNIEnv *env, jobject fileDescriptor) {
    jint fd = -1;
    jclass fdClass = env->FindClass("java/io/FileDescriptor");

    if (fdClass != NULL) {
        jfieldID fdClassDescriptorFieldID = env->GetFieldID(fdClass, "fd", "I");
        if (fdClassDescriptorFieldID != NULL && fileDescriptor != NULL) {
            fd = env->GetIntField(fileDescriptor, fdClassDescriptorFieldID);
        }
    }

    return fd;
}

static jclass findUnixExceptionClass(
        JNIEnv *env
) {
    static jclass exception = nullptr;
    if (!exception) {
        exception = findClass(env, "io/github/excu101/filesystem/unix/error/UnixException");
    }

    return exception;
}

static jmethodID findUnixExceptionConstructorMethod(
        JNIEnv *env
) {
    return env->GetMethodID(
            findUnixExceptionClass(env),
            "<init>",
            "(ILjava/lang/String;)V"
    );
}

static void __throwUnixException(JNIEnv *env, int error, const char *name) {
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
    }
    env->ThrowNew(findUnixExceptionClass(env), "exception");
}

#define UNIX_ERROR(...) __throwUnixException(__VA_ARGS__);

static jclass findUnixStatusStructureStatClass(
        JNIEnv *env
) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/attr/UnixStatusStructure"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);
    return global;
}

static jmethodID findUnixStatusStructureInitMethod(
        JNIEnv *env
) {
    return env->GetMethodID(
            findUnixStatusStructureStatClass(env),
            "<init>",
            "(IIIJJJJJJJJJJJJJ)V"
    );
}

static jclass findUnixDirentStructureClass(JNIEnv *env) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/attr/UnixDirentStructure"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);
    return global;
}

static jmethodID findUnixDirentStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixDirentStructureClass(env),
            "<init>", "(JJII[B)V"
    );
}

static jclass findUnixFileSystemStatusStructureClass(JNIEnv *env) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/attr/UnixStructureFileSystemStatus"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);
    return global;
}

static jmethodID findUnixFileSystemStatusStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixFileSystemStatusStructureClass(env),
            "<init>",
            "(JJJJJJJJJJJ)V"
    );
}