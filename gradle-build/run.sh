#!/usr/bin/env bash

cd $(dirname $0) 
pwd
cd build/native-image
./native-tests # this is a standard name for the native binary containing the test code
./gradle-build # this is named for the artifactId of the module