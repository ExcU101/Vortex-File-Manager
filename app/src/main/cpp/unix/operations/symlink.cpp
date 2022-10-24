#include "unistd.h"

static bool createSymbolicLink(const char *path, const char *link) {
    return symlink(path, link) == 0;
}