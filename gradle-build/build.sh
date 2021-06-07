#!/usr/bin/env bash

# this build has customized build.gradle and
# settings.gradle configurations
cd $(dirname $0)
#./gradlew nativeBuild
./gradlew nativeTest nativeBuild
