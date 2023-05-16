
#include "jni_classes.h"
#include "jni_class_names.h"

static jclass findClass(JNIEnv *env, const char *name) {
    jclass local = env->FindClass(name);
    jobject global = env->NewGlobalRef(local);
    env->DeleteLocalRef(local);

    if (global == NULL) {
        abort();
    }

    return (jclass) global;
}

static jclass findJavaFileDescriptorClass(JNIEnv *env) {
    static jclass clazz = NULL;
    if (!clazz) {
        clazz = findClass(env, "java/io/FileDescriptor");
    }
    return clazz;
}

static jmethodID findJavaFileDescriptorInitMethod(JNIEnv *env) {
    return env->GetMethodID(findJavaFileDescriptorClass(env), "<init>", "()V");
}

static jfieldID findJavaFileDescriptorField(JNIEnv *env) {
    static jfieldID field = NULL;
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
    static jclass clazz = NULL;
    if (!clazz) {
        clazz = findClass(env, "android/system/Int64Ref");
    }

    return clazz;
}

static jfieldID findInt64RefValueField(JNIEnv *env) {
    static jfieldID field = NULL;
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

    jobject exception = env->NewObject(
            findUnixExceptionClass(env),
            findUnixExceptionConstructorMethod(env),
            error,
            env->NewStringUTF(name)
    );

    env->Throw((jthrowable) exception);
}

static jclass findUnixStatusStructureStatClass(JNIEnv *env) {
    static jclass structure;
    if (structure == NULL) {
        structure = findClass(
                env,
                STATUS_STRUCTURE
        );
    }
    return structure;
}

static jmethodID findUnixStatusStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixStatusStructureStatClass(env),
            "<init>",
            "(IIIJJJJJJJJJJJJJ)V"
    );
}

static jclass findUnixDirectoryEntryStructureClass(JNIEnv *env) {
    static jclass structure;
    if (structure == NULL) {
        structure = findClass(
                env,
                DIRECTORY_ENTRY
        );
    }
    return structure;
}

static jmethodID findUnixDirectoryEntryStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixDirectoryEntryStructureClass(env),
            "<init>",
            "(JJII[B)V"
    );
}

static jclass findUnixFileSystemStatusStructureClass(JNIEnv *env) {
    static jclass clazz = NULL;
    if (clazz == NULL) {
        clazz = findClass(
                env,
                STATUS_STRUCTURE
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

static jclass findUnixGroupStructureClass(JNIEnv *env) {
    static jclass clazz = NULL;
    if (clazz == NULL) {
        clazz = findClass(
                env,
                GROUP
        );
    }
    return clazz;
}

static jmethodID findUnixGroupStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixGroupStructureClass(env),
            "<init>",
            "([B[BI[[B)V"
    );
}

static jclass findUnixMountEntryStructureClass(JNIEnv *env) {
    static jclass entry;

    if (entry == NULL) {
        entry = findClass(
                env,
                MOUNT_ENTRY
        );
    }

    return entry;
}

static jmethodID findUnixMountEntryStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(

            findUnixMountEntryStructureClass(env),
            "<init>",
            "([B[B[B[BII)V"
    );
}

static jclass findUnixPathObservableEventClass(JNIEnv *env) {
    static jclass entry;

    if (entry == NULL) {
        entry = findClass(
                env,
                OBSERVABLE_EVENT
        );
    }

    return entry;
}

static jmethodID findUnixPathObservableEventInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixPathObservableEventClass(env),
            "<init>",
            "(III[B)V"
    );
}


static char *fromByteArrayToPath(JNIEnv *env, jbyteArray path) {
    jbyte *segments = env->GetByteArrayElements(path, nullptr);
    jsize jLength = env->GetArrayLength(path);
    size_t length = jLength;
    char *cPath = (char *) malloc(length + 1);
    memcpy(cPath, segments, length);
    env->ReleaseByteArrayElements(path, segments, JNI_ABORT);
    cPath[length] = '\0';
    return cPath;
}

static jbyteArray createByteArray(JNIEnv *env, char *name) {
    size_t length = strlen(name);
    jsize javaLength = length;
    jbyteArray bytes = env->NewByteArray(javaLength);

    if (bytes == NULL) {
        return NULL;
    }

    env->SetByteArrayRegion(bytes, 0, javaLength, (jbyte *) name);
    return bytes;
}
