#include <cstdlib>
#include "jni.h"

//#define __VA_ARGS__

static jclass findClass(JNIEnv *env, const char *name) {
    jclass local = env->FindClass(name);
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);

    if (global == NULL) {
        abort();
    }

    return global;
}

static jclass findJavaFileDescriptorClass(JNIEnv *env) {
    static jclass clazz = nullptr;
    if (!clazz) {
        clazz = findClass(env, "java/io/FileDescriptor");
    }
    return clazz;
}

static _jmethodID *findJavaFileDescriptorInitMethod(JNIEnv *env) {
    return env->GetMethodID(findJavaFileDescriptorClass(env), "<init>", "()V");
}

static jfieldID findJavaFileDescriptorField(JNIEnv *env) {
    static jfieldID field = nullptr;
    if (!field) {
        field = env->GetFieldID(findJavaFileDescriptorClass(env), "descriptor", "I");
    }
    return field;
}

static int getIndexFromFileDescriptor(JNIEnv *env, jobject fileDescriptor) {
    jint fd = -1;
    jfieldID field = findJavaFileDescriptorField(env);
    if (field != NULL && fileDescriptor != NULL) {
        fd = env->GetIntField(fileDescriptor, field);
    }

    return fd;
}

static jclass findUnixExceptionClass(
        JNIEnv *env
) {
    static jclass exception = NULL;
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

static void throwUnixException(JNIEnv *env, int error, const char *name) {
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
    }


    auto exception = env->NewObject(
            findUnixExceptionClass(env),
            findUnixExceptionConstructorMethod(env),
            error,
            name
    );

//    env->Throw(exception);
}

static jclass findUnixStatusStructureStatClass(
        JNIEnv *env
) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/structure/UnixStatusStructure"
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
            "io/github/excu101/filesystem/unix/structure/UnixDirentStructure"
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
    static jclass clazz = NULL;
    if (clazz == NULL) {
        clazz = findClass(
                env,
                "io/github/excu101/filesystem/unix/structure/UnixFileSystemStatusStructure"
        );
    }
    return clazz;
}

static jmethodID findUnixFileSystemStatusStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixFileSystemStatusStructureClass(env),
            "<init>",
            "(JJJJJJJJJJJ)V"
    );
}

static jclass findUnixMountEntryStructureClass(JNIEnv *env) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/structure/UnixMountEntryStructure"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);
    return global;
}

static jmethodID findUnixMountEntryStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixMountEntryStructureClass(env),
            "<init>",
            "([B[B[B[BII)V"
    );
}