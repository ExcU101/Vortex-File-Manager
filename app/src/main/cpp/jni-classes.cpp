#include <cstdlib>
#include <cstring>
#include "jni-classes.h"

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

static jmethodID findJavaFileDescriptorInitMethod(JNIEnv *env) {
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

static jclass findInt64RefClass(JNIEnv *env) {
    static jclass clazz = nullptr;
    if (!clazz) {
        clazz = findClass(env, "android/system/Int64Ref");
    }

    return clazz;
}

static jfieldID findInt64RefValueField(JNIEnv *env) {
    static jfieldID field = nullptr;
    if (!field) {
        field = env->GetFieldID(findInt64RefClass(env), "value", "J");
    }
    return field;
}

static jclass findUnixExceptionClass(JNIEnv *env) {
    static jclass exception = NULL;
    if (!exception) {
        exception = findClass(env, "io/github/excu101/filesystem/unix/error/UnixException");
    }

    return exception;
}

static jmethodID findUnixExceptionConstructorMethod(JNIEnv *env) {
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
            env->NewStringUTF(name)
    );

    env->Throw((jthrowable) exception);
}


static jclass findUnixStatusStructureStatClass(JNIEnv *env) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/structure/UnixStatusStructure"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);
    return global;
}

static jmethodID findUnixStatusStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixStatusStructureStatClass(env),
            "<init>",
            "(IIIJJJJJJJJJJJJJ)V"
    );
}

static jclass findUnixDirectoryEntryStructureClass(JNIEnv *env) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/structure/UnixDirectoryEntryStructure"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);
    return global;
}

static jmethodID findUnixDirectoryEntryStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixDirectoryEntryStructureClass(env),
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

static jclass findUnixPathObservableEventClass(JNIEnv *env) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/structure/UnixPathObservableStructureEvent"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    env->DeleteLocalRef(local);
    return global;
}

static jmethodID findUnixPathObservableEventInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixPathObservableEventClass(env),
            "<init>",
            "(III[B)V"
    );
}


static char *fromByteArrayToPath(JNIEnv *env, jbyteArray path) {
    jbyte *segments = env->GetByteArrayElements(path, NULL);
    jsize jLength = env->GetArrayLength(path);
    auto length = (size_t) jLength;
    char *cPath = (char *) malloc(length + 1);
    memcpy(cPath, segments, length);
    env->ReleaseByteArrayElements(path, segments, JNI_ABORT);
    cPath[length] = '\0';
    return cPath;
}

static jbyteArray createByteArray(JNIEnv *env, char *name) {
    size_t length = strlen(name);
    auto javaLength = (jsize) length;
    jbyteArray bytes = env->NewByteArray(javaLength);

    if (bytes == NULL) {
        return NULL;
    }

    env->SetByteArrayRegion(bytes, 0, javaLength, (jbyte *) name);
    return bytes;
}
