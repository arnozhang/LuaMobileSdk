cmake_minimum_required(VERSION 3.4.1)
set (CMAKE_CXX_STANDARD 11)

project(luamobilesdk)


include_directories(../lua/src)
include_directories(./src)

file(GLOB_RECURSE SRC_DIRS ./src *.cc)

add_library(luamobilesdk SHARED ${SRC_DIRS})


find_library(log-lib log)

target_link_libraries(luamobilesdk lua ${log-lib})
