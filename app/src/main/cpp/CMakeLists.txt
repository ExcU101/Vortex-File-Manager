cmake_minimum_required(VERSION 3.18.1)

project("vortex-calls")

add_library(
        unix-calls
        SHARED
        unix/unix-calls.cpp
        unix/attrs.cpp
        unix/operations.cpp
        unix/classes.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        unix-calls
        ${log-lib}
)
