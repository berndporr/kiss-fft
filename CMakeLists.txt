cmake_minimum_required(VERSION 3.1.0)

project(kiss-fft VERSION 1.2.0 LANGUAGES CXX)

set (CMAKE_CXX_STANDARD 11)
set(CMAKE_POSITION_INDEPENDENT_CODE ON)

if(NOT CMAKE_BUILD_TYPE AND NOT CMAKE_CONFIGURATION_TYPES)
        set(CMAKE_BUILD_TYPE "RelWithDebInfo")
endif()

include(GNUInstallDirs)

cmake_policy(SET CMP0048 NEW) # set VERSION in project()
cmake_policy(SET CMP0042 NEW) # enable MACOSX_RPATH by default

if (MSVC)
  add_compile_options(/W4)
else()
  add_compile_options(-Wall -Wconversion -Wextra -pedantic)
endif()

add_library(kiss-fft STATIC kiss_fft.cpp kiss_fftr.cpp)

target_include_directories(kiss-fft PRIVATE .)

list(APPEND MYHEADERS kiss_fft.h kiss_fftr.h _kiss_fft_guts.h)

set_target_properties(kiss-fft PROPERTIES
  VERSION ${PROJECT_VERSION}
  PUBLIC_HEADER "${MYHEADERS}")

install(TARGETS kiss-fft EXPORT kiss-fft-targets
  LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
  ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR}
  PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})

install(EXPORT kiss-fft-targets
  DESTINATION lib/cmake/kiss-fft
  FILE kiss-fft-config.cmake
)
