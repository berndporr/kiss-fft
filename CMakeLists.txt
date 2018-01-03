cmake_minimum_required(VERSION 3.5)
project(kiss-fft LANGUAGES C)
include(GNUInstallDirs)
add_library(kiss-fft STATIC kiss_fft.c kiss_fftr.c)
target_include_directories(kiss-fft PRIVATE .)
list(APPEND MYHEADERS kiss_fft.h kiss_fftr.h _kiss_fft_guts.h)
set_target_properties(kiss-fft PROPERTIES
    PUBLIC_HEADER "${MYHEADERS}")
install(TARGETS kiss-fft
  LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
  ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR}
  PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})
add_definitions(-Dkiss_fft_scalar=double)
