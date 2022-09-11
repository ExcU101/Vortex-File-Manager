#include "stdlib.h"
#include "dirent.h"
#include "string"
#include "errno.h"
#include "../log.h"
#include "classes.cpp"
#include "dirent.h"
#include "jni.h"
#include "attrs.cpp"
#include "operations.cpp"

namespace Unix {}

static void clearErrno() {
    errno = 0;
}

static char *fromByteArrayToPath(
        JNIEnv *env,
        jbyteArray path
) {
    jbyte *segments = env->GetByteArrayElements(path, NULL);
    jsize jLength = env->GetArrayLength(path);
    auto length = (size_t) jLength;
    char *cPath = (char *) malloc(length + 1);
    memcpy(cPath, segments, length);
    env->ReleaseByteArrayElements(path, segments, JNI_ABORT);
    cPath[length] = '\0';
    return cPath;
}

static jobject doStat(
        JNIEnv *env,
        char *path,
        bool isLinkStatus
) {
    static jmethodID constructor(nullptr);
    if (!constructor) { constructor = findUnixStatusStructureInitMethod(env); }
    struct stat64 status = {};
    isLinkStatus ? isLinkExists(path, &status) : isExists(path, &status);
    free(path);

    auto userId = (jint) status.st_uid;
    auto groupId = (jint) status.st_gid;
    auto mode = (jint) status.st_mode;
    auto deviceId = (jlong) status.st_dev;
    auto inode = (jlong) status.st_ino;
    jlong countLinks = status.st_nlink;
    auto otherDeviceId = (jlong) status.st_rdev;
    jlong blocksSize = status.st_blksize;
    auto blocks = (jlong) status.st_blocks;
    jlong size = status.st_size;
    jlong lastModifiedTimeSeconds = status.st_mtim.tv_sec;
    jlong lastAccessTimeSeconds = status.st_atim.tv_sec;
    jlong creationTimeSeconds = status.st_ctim.tv_sec;
    jlong lastModifiedTimeNanos = status.st_mtim.tv_nsec;
    jlong lastAccessTimeNanos = status.st_atim.tv_nsec;
    jlong creationTimeNanos = status.st_ctim.tv_nsec;

    return env->NewObject(
            findUnixStatusStructureStatClass(env),
            constructor,
            userId,
            groupId,
            mode,
            deviceId,
            inode,
            countLinks,
            otherDeviceId,
            blocksSize,
            blocks,
            size,
            lastModifiedTimeSeconds,
            lastAccessTimeSeconds,
            creationTimeSeconds,
            lastModifiedTimeNanos,
            lastAccessTimeNanos,
            creationTimeNanos
    );
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_lstat(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    return doStat(env, cPath, true);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_stat(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    return doStat(env, cPath, false);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_removeDirectory(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    clearErrno();

    char *cPath = fromByteArrayToPath(env, path);
    rmdir(cPath);
    free(cPath);

    if (errno != 0) {

    }
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_unlink(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    clearErrno();
    char *cPath = fromByteArrayToPath(env, path);
    unlink(cPath);
    free(cPath);

    if (errno != 0) {
        UNIX_ERROR(env, errno, "unlink")
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_closeDir(
        JNIEnv *env,
        jobject thiz,
        jlong pointer
) {
    clearErrno();
    DIR *dir = (DIR *) pointer;
    closedir(dir);
}

jlong openDirectory(
        JNIEnv *env,
        char *path
) {
    clearErrno();
    DIR *pointer = opendir(path);
    free(path);

    return (jlong) pointer;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_openDir(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    return openDirectory(env, fromByteArrayToPath(env, path));
}

static jobject newLinuxDirentStruct(
        JNIEnv *env,
        const struct dirent64 *directory
) {
    static jmethodID constructor(nullptr);
    if (!constructor) {
        constructor = findUnixDirentStructureInitMethod(env);
    }

    auto inode = (jlong) directory->d_ino;
    jlong offset = directory->d_off;
    jint recordLength = directory->d_reclen;
    jint type = directory->d_type;
    char *name = (char *) (directory->d_name);

    size_t length = strlen(name);
    auto javaLength = (jsize) length;
    jbyteArray bytes = env->NewByteArray(javaLength);
    env->SetByteArrayRegion(bytes, 0, javaLength, (jbyte *) name);

    return env->NewObject(
            findUnixDirentStructureClass(env),
            constructor,
            inode,
            offset,
            recordLength,
            type,
            bytes
    );
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_linux_LinuxCalls_rename(
        JNIEnv *env,
        jobject thiz,
        jbyteArray source,
        jbyteArray dest
) {
    char *sourcePath = fromByteArrayToPath(env, source);
    char *destPath = fromByteArrayToPath(env, dest);
    rename(sourcePath, destPath);
    free(sourcePath);
    free(destPath);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_readDir(
        JNIEnv *env,
        jobject thiz,
        jlong pointer
) {
    clearErrno();

    if (!pointer) {
        return nullptr;
    }

    DIR *dir = (DIR *) pointer;

    struct dirent64 *directory = readdir64(dir);

    if (!directory) {
        return nullptr;
    }

    return newLinuxDirentStruct(env, directory);
}

static jobject openFileDescriptor(JNIEnv *env, int id) {
    static jmethodID constructor(nullptr);
    if (!constructor) {
        constructor = findJavaFileDescriptorInitMethod(env);
    }

    return env->NewObject(
            findJavaFileDescriptorClass(env),
            constructor,
            id
    );
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_open(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path,
        jint flags,
        jint mode
) {
    char *cPath = fromByteArrayToPath(env, path);
    int cFlags = flags;
    auto cMode = (mode_t) mode;
    int id = openFileDescriptor(cPath, cFlags, cMode, false);
    free(cPath);

    return openFileDescriptor(env, id);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_mkdir(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path,
        jint mode
) {
    clearErrno();
    char *cPath = fromByteArrayToPath(env, path);
    auto cMode = (mode_t) mode;
    makeDirectory(cPath, cMode);
    free(cPath);
}

static jobject doStatVfs(JNIEnv *env, const struct statvfs64 *statvfs) {
    static jmethodID constructor(nullptr);

    if (!constructor) {
        constructor = findUnixFileSystemStatusStructureInitMethod(env);
    }

    jlong blockSize = statvfs->f_bsize;
    jlong fundamentBlockSize = statvfs->f_frsize;
    jlong totalBlocks = statvfs->f_blocks;
    jlong freeBlocks = statvfs->f_bfree;
    jlong availableBlocks = statvfs->f_bavail;
    jlong totalFiles = statvfs->f_files;
    jlong freeFiles = statvfs->f_ffree;
    jlong freeNonRootFiles = statvfs->f_favail;
    jlong fileSystemId = statvfs->f_fsid;
    jlong bitMask = statvfs->f_flag;
    jlong maxFileNameLength = statvfs->f_namemax;

    return env->NewObject(
            findUnixFileSystemStatusStructureClass(env),
            constructor,
            blockSize,
            fundamentBlockSize,
            totalBlocks,
            freeBlocks,
            availableBlocks,
            totalFiles,
            freeFiles,
            freeNonRootFiles,
            fileSystemId,
            bitMask,
            maxFileNameLength
    );
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_statVfs(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    clearErrno();
    struct statvfs64 statvfs = {};
    char *cPath = fromByteArrayToPath(env, path);
    statusFileSystem(cPath, &statvfs);

    free(cPath);

    return doStatVfs(env, &statvfs);
}
extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_rename(
        JNIEnv *env,
        jobject thiz,
        jbyteArray source,
        jbyteArray dest
) {
    clearErrno();
    char *cSource = fromByteArrayToPath(env, source);
    char *cDest = fromByteArrayToPath(env, dest);
    renameFile(cSource, cDest);

    free(cSource);
    free(cDest);
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_fs_operation_NativeCalls_pointerRead(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jlong address,
        jlong length,
        jlong pointer
) {
    clearErrno();
    jint result = pread64(descriptor, &address, length, pointer);

    if (errno != 0) {

    }

    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_close(
        JNIEnv *env,
        jobject thiz,
        jint descriptor
) {
    closeFileDescriptor(descriptor);
}

struct count {
    int files;
    int folders;
};

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_getDirectoryCountImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    clearErrno();
    dirent64 *ent;
    char *cPath = fromByteArrayToPath(env, path);
    char subpath[PATH_MAX];
    DIR *dir = opendir(cPath);
    struct stat64 buffer = {};

    if (dir == nullptr) return -1;
    int count = 0;

    while ((ent = readdir64(dir))) {
        if (strlen(cPath) + 1 + strlen(ent->d_name) > PATH_MAX) {
            return -1;
        }

        sprintf(subpath, "%s%c%s", cPath, '/', ent->d_name);
        if (lstat64(subpath, &buffer)) {
            return -1;
        }

        if (isDirectory(buffer.st_mode)) {
            ++count;
        }
    }

    closedir(dir);
    free(cPath);

    return count;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_getFileCountImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    clearErrno();
    dirent64 *ent;
    char *cPath = fromByteArrayToPath(env, path);
    char subpath[PATH_MAX];
    DIR *dir = opendir(cPath);
    struct stat64 buffer = {};

    if (dir == nullptr) return -1;
    int count = 0;

    while ((ent = readdir64(dir))) {
        if (strlen(cPath) + 1 + strlen(ent->d_name) > PATH_MAX) {
            return -1;
        }

        sprintf(subpath, "%s%c%s", cPath, '/', ent->d_name);
        if (lstat64(subpath, &buffer)) {
            return -1;
        }

        if (isRegularFile(buffer.st_mode)) {
            ++count;
        }
    }

    closedir(dir);
    free(cPath);

    return count;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_getCountImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    clearErrno();
    struct dirent64 *ent;
    char *cPath = fromByteArrayToPath(env, path);
    DIR *dir = opendir(cPath);

    if (dir == nullptr) return -1;
    int count = 0;

    while ((ent = readdir64(dir))) {
        ++count;
    }

    closedir(dir);
    free(cPath);

    return count;
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_fs_operation_NativeCalls_close(
        JNIEnv *env,
        jobject thiz,
        jint descriptor
) {
    closeFileDescriptor(descriptor);
}
extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_fs_operation_NativeCalls_getFileDescriptor(
        JNIEnv *env,
        jobject thiz,
        jobject original
) {
    return getIndexFromFileDescriptor(env, original);
}