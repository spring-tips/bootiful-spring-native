#!/usr/bin/env bash

cd $(dirname .)
cd target
./native-tests # this is a standard name for the native binary containing the test code
./maven # this is named for the artifactId of the module