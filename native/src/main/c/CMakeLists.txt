cmake_minimum_required(VERSION 3.27)
project("crimsonmc-native")

set(JAVA_AWT_LIBRARY NotNeeded)
set(JAVA_JVM_LIBRARY NotNeeded)
set(JAVA_AWT_INCLUDE_PATH NotNeeded)

find_package(JNI REQUIRED)
find_package(ZLIB REQUIRED)

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "build/org/crimsonmc/natives/libs/${BUILD_ARCH}")

set(Sources "zlib_native.c")

include_directories(${JNI_INCLUDE_DIRS} ${ZLIB_INCLUDE_DIRS})

add_library(crimsonmc ${Sources})

target_link_libraries(crimsonmc
    ${ZLIB_LIBRARIES} # ZLIB
    )
