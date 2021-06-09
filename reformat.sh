#!/usr/bin/env bash
for m in imperative buildpacks aot library-with-resource maven-build reactive; do 
    echo $m 
    mvn -f $m/pom.xml io.spring.javaformat:spring-javaformat-maven-plugin:0.0.28:apply
done 
 