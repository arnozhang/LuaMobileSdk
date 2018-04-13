cmake_minimum_required(VERSION 3.4.1)
set (CMAKE_CXX_STANDARD 11)

project(lua)

include_directories(src)

file(GLOB_RECURSE SRC_DIRS ./src *.c)

add_library(lua SHARED ${SRC_DIRS})


find_library(log-lib log)

target_link_libraries(lua ${log-lib})
