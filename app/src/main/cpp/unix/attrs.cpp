#include "sys/stat.h"
#include "sys/statvfs.h"

#define STATUS(...) stat(__VA_ARGS__)
#define STATUS64(...) stat64(__VA_ARGS__)
#define LINK_STATUS(...) lstat(__VA_ARGS__)
#define LINK_STATUS64(...) lstat64(__VA_ARGS__)
#define FILESYSTEM_STATUS(...) statvfs(__VA_ARGS__)
#define FILESYSTEM_STATUS64(...) statvfs64(__VA_ARGS__)

static bool statusFileSystem(char *path, struct statvfs64 *stat) {
    return FILESYSTEM_STATUS64(path, stat) == 0;
}

static bool statusFileSystem(char *path, struct statvfs *stat) {
    return FILESYSTEM_STATUS(path, stat) == 0;
}

static bool isLink(int mode) {
    return S_ISLNK(mode);
}

static bool isDirectory(int mode) {
    return S_ISDIR(mode);
}

static bool isRegularFile(int mode) {
    return S_ISREG(mode);
}

static bool isFifo(int mode) {
    return S_ISFIFO(mode);
}

static bool isSocket(int mode) {
    return S_ISSOCK(mode);
}

// Readable
static bool isReadableForUser(int mode) {
    return (mode & S_IRUSR) == S_IRUSR;
}

static bool isReadableForGroup(int mode) {
    return (mode & S_IRGRP) == S_IRGRP;
}

static bool isReadableForOther(int mode) {
    return (mode & S_IROTH) == S_IROTH;
}

static bool isReadable(int mode) {
    return isReadableForUser(mode) && isReadableForGroup(mode) && isReadableForOther(mode);
}

// Writeable
static bool isWriteableForUser(int mode) {
    return (mode & S_IWUSR) == S_IWUSR;
}

static bool isWriteableForGroup(int mode) {
    return (mode & S_IWGRP) == S_IWGRP;
}

static bool isWriteableForOther(int mode) {
    return (mode & S_IWOTH) == S_IWOTH;
}

static bool isWriteable(int mode) {
    return isWriteableForUser(mode) && isWriteableForGroup(mode) && isWriteableForOther(mode);
}

// Executable
static bool isExecutableForUser(int mode) {
    return (mode & S_IXUSR) == S_IXUSR;
}

static bool isExecutableForGroup(int mode) {
    return (mode & S_IXGRP) == S_IXGRP;
}

static bool isExecutableForOther(int mode) {
    return (mode & S_IXOTH) == S_IXOTH;
}

static bool isExecutable(int mode) {
    return isExecutableForUser(mode) && isExecutableForGroup(mode) && isExecutableForOther(mode);
}

static bool isLinkExists(const char *path, struct stat *buffer) {
    return LINK_STATUS(path, buffer) == 0;
}

static bool isLinkExists(const char *path, struct stat64 *buffer) {
    return LINK_STATUS64(path, buffer) == 0;
}

static bool isExists(const char *path, struct stat *buffer) {
    return STATUS(path, buffer) == 0;
}

static bool isExists(const char *path, struct stat64 *buffer) {
    return STATUS64(path, buffer) == 0;
}

