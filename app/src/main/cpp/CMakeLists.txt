cmake_minimum_required(VERSION 3.18.1)

project("vortex-calls")

add_library(
        classes
        SHARED
        jni-classes.h
        jni-classes.cpp
)

add_library(
        unix-calls
        SHARED
        unix/unix-calls.cpp
        unix/attrs.cpp
        unix/operations.cpp
)

add_library(
        unix-observer-service
        SHARED
        unix/observer/unix-observer.cpp
)

add_library(
        unix-file-channel
        SHARED
        unix/channel/unix-file-channel.cpp
)

add_library(
        unix-buffer
        SHARED
        unix/buffer/unix-buffer.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        classes
        unix-calls
        unix-observer-service
        unix-file-channel
        unix-buffer
        ${log-lib}
)
