#ifndef VORTEX_FILE_MANAGER_STATUS
#define VORTEX_FILE_MANAGER_STATUS

#include <climits>
#include "sys/stat.h"
#include "sys/statvfs.h"

#define STATUS(...) stat(__VA_ARGS__)
#define STATUS64(...) stat64(__VA_ARGS__)
#define DESCRIPTOR_STATUS(...) fstat(__VA_ARGS__)
#define DESCRIPTOR_STATUS64(...) fstat64(__VA_ARGS__)
#define LINK_STATUS(...) lstat(__VA_ARGS__)
#define LINK_STATUS64(...) lstat64(__VA_ARGS__)
#define FILESYSTEM_STATUS(...) statvfs(__VA_ARGS__)
#define FILESYSTEM_STATUS64(...) statvfs64(__VA_ARGS__)

int isCurrentDirectory(const char *name, unsigned int len);

int isParentDirectory(const char *name, unsigned int len);

int isCurrentDirectory(const char *name);

int isParentDirectory(const char *name);

int getStatus(const char *path, struct stat *buffer);

int getStatus(const int descriptor, struct stat *buffer);

int getStatus64(const char *path, struct stat64 *buffer);

int getStatus64(const int descriptor, struct stat64 *buffer);

bool isExists(const char *path, struct stat *buffer);

bool isExists(const char *path, struct stat64 *buffer);

bool isExists(int descriptor, struct stat *buffer);

bool isExists(int descriptor, struct stat64 *buffer);

bool isDirectory(unsigned int mode);

bool isDirectory(const char *path, struct stat *buffer);

bool isDirectory(int descriptor, struct stat *buffer);

bool isDirectory(const char *path, struct stat64 *buffer);

bool isDirectory(int descriptor, struct stat64 *buffer);

bool isRegularFile(unsigned int mode);

bool isRegularFile(const char *path, struct stat *buffer);

bool isRegularFileAt(int descriptor, struct stat *buffer);

#endif //VORTEX_FILE_MANAGER_STATUS
