#ifndef _VORTEX_FILE_MANAGER_JNI_CLASSES_
#define _VORTEX_FILE_MANAGER_JNI_CLASSES_

#include "jni.h"
#include <cstring>
#include <cstdlib>

static jclass findClass(JNIEnv *env, const char *name);

static jclass findJavaFileDescriptorClass(JNIEnv *env);

static jmethodID findJavaFileDescriptorInitMethod(JNIEnv *env);

static jfieldID findJavaFileDescriptorField(JNIEnv *env);

static int getIndexFromFileDescriptor(JNIEnv *env, jobject fileDescriptor);

static jclass findInt64RefClass(JNIEnv *env);

static jfieldID findInt64RefValueField(JNIEnv *env);

static jclass findUnixExceptionClass(JNIEnv *env);

static jmethodID findUnixExceptionConstructorMethod(JNIEnv *env);

static jclass findUnixStatusStructureStatClass(JNIEnv *env);

static jmethodID findUnixStatusStructureInitMethod(JNIEnv *env);

static void throwUnixException(JNIEnv *env, int error, const char *name);

static jclass findUnixDirectoryEntryStructureClass(JNIEnv *env);

static jmethodID findUnixDirectoryEntryStructureInitMethod(JNIEnv *env);

static jclass findUnixFileSystemStatusStructureClass(JNIEnv *env);

static jmethodID findUnixFileSystemStatusStructureInitMethod(JNIEnv *env);

static jclass findUnixMountEntryStructureClass(JNIEnv *env);

static jmethodID findUnixMountEntryStructureInitMethod(JNIEnv *env);

static jclass findUnixPathObservableEventClass(JNIEnv *env);

static jmethodID findUnixPathObservableEventInitMethod(JNIEnv *env);

static char *fromByteArrayToPath(JNIEnv *env, jbyteArray path);

static jbyteArray createByteArray(JNIEnv *env, char *name);

#endif //_VORTEX_FILE_MANAGER_JNI_CLASSES_
