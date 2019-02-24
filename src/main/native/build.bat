cd build
cmake .. -G"Visual Studio 15 2017 Win64" -DVCPKG_TARGET_TRIPLET:STRING=x64-windows-static -DCMAKE_TOOLCHAIN_FILE=C:/Tools/vcpkg/scripts/buildsystems/vcpkg.cmake
cd ..