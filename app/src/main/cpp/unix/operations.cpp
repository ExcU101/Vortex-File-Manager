#include "unistd.h"
#include "cstdio"
#include "sys/stat.h"
#include "sys/file.h"

static bool makeDirectory(const char *path, mode_t mode) {
    return mkdir(path, mode) == 0;
}

static int openFileDescriptor(const char *path, int flags, mode_t mode, bool is64) {
    return is64 ? open64(path, flags, mode) : open(path, flags, mode);
}

static bool closeFileDescriptor(int pointer) {
    return close(pointer) == 0;
}

static bool renameFile(const char *source, const char *dest) {
    return rename(source, dest) == 0;
}

static bool removeFile(const char *path) {
    return remove(path) == 0;
}