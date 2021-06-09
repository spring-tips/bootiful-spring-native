# Script 

## Basics with Buildpacks
* traditional  
  * at some point i start talking about buildpacks. let's see if i can copy and paste the buildpacks segment from kubernetes native java video? when i start talking about buildpacks, i introduce it in the pom.xml, but maybe i could then jump to the kubernetes-native-java segment?
* reactive (i did this one twice in the video, so skip to the end of the video and work backwarsd to the beginning. i scrapped the entire first take)

## Local Builds 
* i think i should do a separate section on how to setup graalvm on ur local machine 
* Gradle - i spoke about graalvm configuration on my machine here. remove that. no need for it. ill do a standalone section on that. i redid the gradle build two times. keep the third. skip the first two. in the third, i dont bother with the tests, only the nativeBuild. OK, the very last thing on this video should be me testing the code while ive got the build at hand. the thing before that is the nativeBuild. so i can euther keep them nextto each other or move the test to a separate section, along with testing with maven. TODO: film a sequence showig the pitiful but valid test in the codebase. then 
* Maven
## Testing 
* check out that we have test binaries for both the gradle and Maven apps. basically, i shot the testing sequence for maven and gradle in the maven and gradle videos. i need to also film a sequence introducing the test code for each of the projects. ill do that along side each of the test builds in the original maven/gradle videos. 
* NEW PLAN: introduce testing as u introduce the local builds. the problem is that the gradle build didnt work by itself with just nativeTest, i had to also do NativeBuild, so i may as well just introduce em both anywya 

## AOT 
* understanding what it does with spring.factories
* future possibilities 

## Hints 
* Reflection
* Resources
* Serialization 
* JDK Proxies
* AOT Proxies
* Native Configuration 
