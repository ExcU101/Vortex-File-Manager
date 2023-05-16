#include "status.h"
#include <iostream>

int isCurrentDirectory(const char *name, unsigned int len) {
    if (len == 1 && name[0] == '.') return 1;
    return 0;
}

int isParentDirectory(const char *name, unsigned int len) {
    if (len == 2 && (name[0] == '.' && name[1] == '.')) return 1;
    return 0;
}

int isCurrentDirectory(const char *name) {
    return isCurrentDirectory(name, *(&name + 1) - name);
}

int isParentDirectory(const char *name) {
    return isParentDirectory(name, *(&name + 1) - name);
}

int getStatus(const char *path, struct stat *buffer) {
    return STATUS(path, buffer);
}

int getStatus(const int descriptor, struct stat *buffer) {
    return DESCRIPTOR_STATUS(descriptor, buffer);
}

int getStatus64(const char *path, struct stat64 *buffer) {
    return STATUS64(path, buffer);
}

int getStatus64(const int descriptor, struct stat64 *buffer) {
    return DESCRIPTOR_STATUS64(descriptor, buffer);
}

bool isExists(const char *path, struct stat *buffer) {
    return getStatus(path, buffer) == 0;
}

bool isExists(const char *path, struct stat64 *buffer) {
    return getStatus64(path, buffer) == 0;
}

bool isExists(int descriptor, struct stat *buffer) {
    return getStatus(descriptor, buffer) == 0;
}

bool isExists(int descriptor, struct stat64 *buffer) {
    return getStatus64(descriptor, buffer);
}

bool isDirectory(unsigned int mode) {
    return S_ISDIR(mode);
}

bool isDirectory(const char *path, struct stat *buffer) {
    stat(path, buffer);
    return isDirectory(buffer->st_mode);
}

bool isDirectory(int descriptor, struct stat *buffer) {
    fstat(descriptor, buffer);
    return isDirectory(buffer->st_mode);
}

bool isDirectory(const char *path, struct stat64 *buffer) {
    stat64(path, buffer);
    return isDirectory(buffer->st_mode);
}

bool isDirectory(int descriptor, struct stat64 *buffer) {
    fstat64(descriptor, buffer);
    return isDirectory(buffer->st_mode);
}

bool isRegularFile(unsigned int mode) {
    return S_ISREG(mode);
}

bool isRegularFile(const char *path, struct stat *buffer) {
    stat(path, buffer);
    return isRegularFile(buffer->st_mode);
}

bool isRegularFileAt(int descriptor, struct stat *buffer) {
    fstat(descriptor, buffer);
    return isRegularFile(buffer->st_mode);
}