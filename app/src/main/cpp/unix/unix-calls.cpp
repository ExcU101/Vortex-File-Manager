#include <cstdlib>
#include "dirent.h"
#include <cerrno>
#include "mntent.h" // Mount entry
#include "android/log.h"
#include "../jni/jni_classes.cpp"
#include "sys/sendfile.h"
#include "sys/socket.h"
#include "operations.сpp"
#include "status/status.h"
#include "../log.h"
#include <jni.h>
#include <grp.h>

static void clearErrno() {
    errno = 0;
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
    isLinkStatus ? lstat64(path, &status) : isExists(path, &status);
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

extern "C" JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_allocateImpl(
        JNIEnv *env,
        jobject thiz,
        jlong size
) {
    size_t cSize = size;
    auto result = (uintptr_t) malloc(cSize);

    return result;
}


extern "C" JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_lstatImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    return doStat(env, cPath, true);
}


extern "C" JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_fstatImpl__Ljava_io_FileDescriptor_2(
        JNIEnv *env,
        jobject thiz,
        jobject descriptor
) {
    return doStat(env, getIndexFromFileDescriptor(env, descriptor));
}


extern "C" JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_fstatImpl__I(
        JNIEnv *env,
        jobject thiz,
        jint descriptor
) {
    return doStat(env, descriptor);
}


extern "C" JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_statImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    return doStat(env, cPath, false);
}


extern "C" JNIEXPORT jint JNICALL
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
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_getGroupImpl(
        JNIEnv *env,
        jobject thiz,
        jint groupId
) {
    struct group *group = getgrgid(groupId);

    if (errno) {
        throwUnixException(env, errno, "getgrgid");
    }

    static jmethodID init = nullptr;

    if (!init) {
        init = findUnixGroupStructureInitMethod(env);
    }

    jbyteArray name = createByteArray(env, group->gr_name);
    jbyteArray password = createByteArray(env, group->gr_passwd);
    jint id = group->gr_gid;

    jsize gr_memLength = 0;
    for (char **gr_memIterator = group->gr_mem; *gr_memIterator; ++gr_memIterator) {
        ++gr_memLength;
    }

    jobjectArray members = env->NewObjectArray(
            gr_memLength,
            findClass(env, "javassist/bytecode/ByteArray"),
            nullptr
    );

    jsize gr_memIndex = 0;
    for (char **gr_memIterator = group->gr_mem; *gr_memIterator; ++gr_memIterator,
            ++gr_memIndex) {
        jobject member = createByteArray(env, *gr_memIterator);
        if (!member) {
            return nullptr;
        }
        env->SetObjectArrayElement(members, gr_memIndex, member);
        env->DeleteLocalRef(member);
    }

    return env->NewObject(
            findUnixGroupStructureClass(env),
            init,
            name,
            password,
            id,
            members
    );
}

extern "C" JNIEXPORT jint JNICALL
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


extern "C" JNIEXPORT jlong JNICALL
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


extern "C" JNIEXPORT void JNICALL
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


extern "C" JNIEXPORT void JNICALL
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


extern "C" JNIEXPORT void JNICALL
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


extern "C" JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_openDir(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    return openDirectory(env, fromByteArrayToPath(env, path));
}

static jobject newLinuxDirectoryEntryStruct(
        JNIEnv *env,
        const struct dirent64 *directory
) {
    static jmethodID constructor(nullptr);
    if (!constructor) {
        constructor = findUnixDirectoryEntryStructureInitMethod(env);
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
            findUnixDirectoryEntryStructureClass(env),
            constructor,
            inode,
            offset,
            recordLength,
            type,
            bytes
    );
}


extern "C" JNIEXPORT void JNICALL
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


extern "C" JNIEXPORT jobject JNICALL
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

//    getdents()

    struct dirent64 *directory = readdir64(dir);

    if (!directory) {
        return nullptr;
    }

    return newLinuxDirectoryEntryStruct(env, directory);
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


extern "C" JNIEXPORT jobject JNICALL
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


extern "C" JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_mkdirImpl(
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

    if (errno != 0) {
//        throwUnixException(env, errno, "mkdir");
    }
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


extern "C" JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_getFileSystemStatusImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    clearErrno();
    struct statvfs64 statvfs = {};
    char *cPath = fromByteArrayToPath(env, path);
    FILESYSTEM_STATUS64(cPath, &statvfs);

    free(cPath);

    return doStatVfs(env, &statvfs);
}

extern "C" JNIEXPORT void JNICALL
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


extern "C" JNIEXPORT jboolean JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_closeImpl(
        JNIEnv *env,
        jobject thiz,
        jint descriptor
) {
    return closeFileDescriptor(descriptor);
}

extern "C" JNIEXPORT jint JNICALL
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


extern "C" JNIEXPORT jint JNICALL
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


extern "C" JNIEXPORT void JNICALL
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
//        throwUnixException(env, errno, "symlink");
    }

    free(cTarget);
    free(cLink);
}


extern "C" JNIEXPORT jint JNICALL
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
        char *name = ent->d_name;

        if (isCurrentDirectory(name) == 0 || isParentDirectory(name) == 0) continue;

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

        if (isDirectory(name, buf)) {
            result += getDirectorySize(name);
        }
        else {

        }
    }

    closedir(dir);
    return result;
}

extern "C" JNIEXPORT jlong JNICALL
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


extern "C" JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_getDescriptorImpl(
        JNIEnv *env,
        jobject thiz,
        jobject descriptor
) {
    return getIndexFromFileDescriptor(env, descriptor);
}

// Mount

extern "C" JNIEXPORT jlong JNICALL
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


extern "C" JNIEXPORT jobject JNICALL
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


extern "C" JNIEXPORT void JNICALL
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


extern "C" JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_moveBytesImpl(
        JNIEnv *env, jobject thiz,
        jobject from_descriptor,
        jobject to_descriptor,
        jobject offset,
        jlong count
) {
    clearErrno();

    int inFileDescriptor = getIndexFromFileDescriptor(env, from_descriptor);
    int outFileDescriptor = getIndexFromFileDescriptor(env, to_descriptor);

    off64_t offVal = 0;
    off64_t *_offset = nullptr;
    if (offset != nullptr) {
        offVal = env->GetLongField(offset, findInt64RefValueField(env));
        _offset = &offVal;
    }
    size_t cCount = count;

    jlong size = sendfile64(
            outFileDescriptor,
            inFileDescriptor,
            _offset,
            cCount
    );
    printf("Errno %i", errno);
    if (errno != 0) {
        printf("%i", errno);
    }

    if (offset != nullptr) {
        env->SetLongField(offset, findInt64RefValueField(env), offVal);
    }

    return size;
}

extern "C" JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_createSocketPairImpl(
        JNIEnv *env,
        jobject thiz,
        jint domain,
        jint type,
        jint protocol,
        jobject first_descriptor,
        jobject second_descriptor
) {
    clearErrno();
    int sockets[2];

    socketpair(domain, type, protocol, sockets);

    if (errno != 0) {
        throwUnixException(env, errno, "createSocketPairImpl");
    }

    env->SetIntField(
            first_descriptor,
            findJavaFileDescriptorField(env),
            sockets[0]
    );
    env->SetIntField(
            second_descriptor,
            findJavaFileDescriptorField(env),
            sockets[1]
    );
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_changeOwnerImpl(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path,
        jint user_id,
        jint group_id
) {
    char *cPath = fromByteArrayToPath(env, path);

    if (!chown(cPath, user_id, group_id)) {
        throwUnixException(env, errno, "changeOwnerImpl");
    }

    free(cPath);
}

extern "C" JNIEXPORT jint JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_manipulateDescriptorImpl(
        JNIEnv *env, jobject thiz,
        jint descriptor,
        jint command
) {
    int result = fcntl(descriptor, command);

    if (result == -1) {
        throwUnixException(env, errno, "manipulateDescriptorImpl");
        return 0;
    }

    return result;
}