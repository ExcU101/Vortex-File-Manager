#include <cstdlib>
#include "dirent.h"
#include "string"
#include <cerrno>
#include "mntent.h" // Mount entry
#include "../log.h"
#include "classes.cpp"
#include "dirent.h"
#include "jni.h"
#include "attrs.cpp"
#include "operations/symlink.cpp"
#include "operations.cpp"

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

static jobject getUnixStatusStructure(
        JNIEnv *env,
        struct stat64 status
) {
    static jmethodID constructor(nullptr);
    if (!constructor) { constructor = findUnixStatusStructureInitMethod(env); }

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

static jobject doStat(
        JNIEnv *env,
        char *path,
        bool isLinkStatus
) {
    struct stat64 status = {};
    isLinkStatus ? isLinkExists(path, &status) : isExists(path, &status);
    free(path);

    return getUnixStatusStructure(env, status);
}

static jobject doStat(
        JNIEnv *env,
        int descriptor
) {
    struct stat64 status = {};
    isExists(descriptor, &status);

    return getUnixStatusStructure(env, status);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_allocateImpl(
        JNIEnv *env,
        jobject thiz,
        jlong size
) {
    size_t cSize = size;
    auto result = (uintptr_t) malloc(cSize);

    return result;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_lstatImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    return doStat(env, cPath, true);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_fstatImpl(
        JNIEnv *env,
        jobject thiz,
        jint descriptor
) {
    int cDescriptor = descriptor;

    return doStat(env, cDescriptor);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_statImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    return doStat(env, cPath, false);
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_truncateImpl__IJ(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jlong offset
) {
    int cDescriptor = descriptor;
    off64_t cOffset = offset;
    jint result = ftruncate64(cDescriptor, cOffset);

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_truncateImpl___3BJ(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path,
        jlong length
) {
    clearErrno();

    char *cPath = fromByteArrayToPath(env, path);
    off64_t cLength = length;
    jint result = truncate64(cPath, cLength);

    free(cPath);

    if (errno != 0) {

    }

    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_lseekImpl(
        JNIEnv *env,
        jobject thiz,
        jint descriptor,
        jlong offset,
        jint whence
) {
    clearErrno();
    int cDescriptor = descriptor;
    off64_t cOffset = offset;
    int cWhence = whence;

    jlong result = lseek64(cDescriptor, cOffset, cWhence);

    if (errno != 0) {

    }

    return result;
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

    if (unlink(cPath) != 0) {
        throwUnixException(env, errno, "unlink");
    }

    free(cPath);
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
Java_io_github_excu101_filesystem_unix_UnixCalls_readDirImpl(
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

static jobject openFileDescriptor(JNIEnv *env, int descriptor) {
    static jmethodID constructor(nullptr);
    if (!constructor) {
        constructor = findJavaFileDescriptorInitMethod(env);
    }

    jobject fileDescriptor = env->NewObject(
            findJavaFileDescriptorClass(env),
            constructor,
            descriptor
    );

    // TODO: Make it outside C++ code
    if (fileDescriptor == nullptr) {
        close(descriptor);
        return NULL;
    }
    env->SetIntField(fileDescriptor, findJavaFileDescriptorField(env), descriptor);

    return fileDescriptor;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_openImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path,
        jint flags,
        jint mode
) {
    char *cPath = fromByteArrayToPath(env, path);
    int cFlags = flags;
    auto cMode = (mode_t) mode;
    int descriptor = openFileDescriptor(cPath, cFlags, cMode, false);
    free(cPath);

    return openFileDescriptor(env, descriptor);
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
Java_io_github_excu101_filesystem_unix_UnixCalls_getFileSystemStatusImpl(
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
    if (!renameFile(cSource, cDest)) {

    }

    free(cSource);
    free(cDest);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_closeImpl(
        JNIEnv *env,
        jobject thiz,
        jint descriptor
) {
    return closeFileDescriptor(descriptor);
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
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_symlinkImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray target,
        jbyteArray link
) {
    clearErrno();
    char *cTarget = fromByteArrayToPath(env, target);
    char *cLink = fromByteArrayToPath(env, link);

    if (!createSymbolicLink(cTarget, cLink)) {
        throwUnixException(env, errno, "symlink");
    }

    free(cTarget);
    free(cLink);
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


long getDirectorySize(char *path) {
    DIR *dir;
    struct dirent64 *ent;
    struct stat64 *buf{};
    long result = 0L;

    dir = opendir(path);

    if (dir == nullptr) return -1L;

    while ((ent = readdir64(dir))) {
        char *name = ent->d_name;
        if (STATUS64(name, buf) == 0) {
            result += (long) buf->st_size;
            if (isDirectory(buf->st_mode)) {
                result += getDirectorySize(name);
            }
        }
    }

    closedir(dir);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_getDirectorySizeImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    long result = getDirectorySize(cPath);
    free(cPath);

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_getDescriptorImpl(
        JNIEnv *env,
        jobject thiz,
        jobject descriptor
) {
    return getIndexFromFileDescriptor(env, descriptor);
}

// Mount
extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixMountCalls_openMountEntryPointerImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path,
        jbyteArray type
) {
    clearErrno();

    char *cPath = fromByteArrayToPath(env, path);
    char *cType = fromByteArrayToPath(env, type);

    auto result = (jlong) setmntent(cPath, cType);

    free(cPath);
    free(cType);

    if (errno != 0) {

    }

    return result;
}

// https://android.googlesource.com/platform/bionic/+/master/libc/bionic/mntent.cpp
static struct mntent *
getmntentRecusively(FILE *fp, struct mntent *entry, char *buffer, int buf_length) {
    memset(entry, 0, sizeof *entry);
    while (fgets(buffer, buf_length, fp) != NULL) {
        int fsname0, fsname1, dir0, dir1, type0, type1, opts0, opts1;
        if (sscanf(buffer, " %n%*s%n %n%*s%n %n%*s%n %n%*s%n %d %d",
                   &fsname0, &fsname1, &dir0, &dir1, &type0, &type1, &opts0, &opts1,
                   &entry->mnt_freq, &entry->mnt_passno) == 2) {
            entry->mnt_fsname = &buffer[fsname0];
            buffer[fsname1] = '\0';
            entry->mnt_dir = &buffer[dir0];
            buffer[dir1] = '\0';
            entry->mnt_type = &buffer[type0];
            buffer[type1] = '\0';
            entry->mnt_opts = &buffer[opts0];
            buffer[opts1] = '\0';
            return entry;
        }
    }
    return NULL;
}

static jobject createUnixMountEntryStructure(JNIEnv *env, const struct mntent *entry) {
    static jmethodID constructor = NULL;

    if (constructor == NULL) {
        constructor = findUnixMountEntryStructureInitMethod(env);
    }

    jbyteArray name = createByteArray(env, entry->mnt_fsname);
    if (name == NULL) {
        return NULL;
    }

    jbyteArray dir = createByteArray(env, entry->mnt_dir);
    if (dir == NULL) {
        return NULL;
    }

    jbyteArray type = createByteArray(env, entry->mnt_type);
    if (type == NULL) {
        return NULL;
    }

    jbyteArray options = createByteArray(env, entry->mnt_opts);
    if (options == NULL) {
        return NULL;
    }

    jint dumpFrequency = entry->mnt_freq;
    jint passNumber = entry->mnt_passno;

    return env->NewObject(
            findUnixMountEntryStructureClass(env),
            constructor,
            name,
            dir,
            type,
            options,
            dumpFrequency,
            passNumber
    );
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixMountCalls_getMountEntryImpl(
        JNIEnv *env,
        jobject thiz,
        jlong pointer
) {
    clearErrno();

    FILE *cPointer = (FILE *) pointer;

    struct mntent entryBuffer = {};
    char buffer[BUFSIZ] = {};
    auto entry = getmntent_r(cPointer, &entryBuffer, buffer, sizeof buffer);

    if (errno != 0) {
        return NULL;
    }

    if (entry == NULL) {
        return NULL;
    }

    return createUnixMountEntryStructure(env, entry);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_calls_UnixMountCalls_closeMountEntryPointerImpl(
        JNIEnv *env,
        jobject thiz,
        jlong pointer
) {
    clearErrno();
    FILE *cPointer = (FILE *) pointer;

    endmntent(cPointer);
    if (errno != 0) {

    }
}