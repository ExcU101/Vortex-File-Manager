cmake_minimum_required(VERSION 3.18.1)

project(vortex-calls)

find_library(
        log-lib
        log
)

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

target_link_libraries(
        unix-calls
        classes
        ${log-lib}

)
