cmake_minimum_required(VERSION 3.18.1)

project(vortex-calls)

add_library(
        classes
        SHARED
        jni/jni_class_names.h
        jni/jni_classes.h
        jni/jni_classes.cpp
)

add_library(
        unix-calls
        SHARED
        unix/unix-calls.cpp
        unix/buffer/unix-buffer.cpp
        unix/operations.сpp
        unix/observer/unix-observer.cpp
        unix/channel/unix-file-channel.cpp
        unix/status/status.h
        unix/status/status.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        classes
        unix-calls
        ${log-lib}
)
