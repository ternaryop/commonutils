commonutils
===========

Android common utils for my own projects

Local Maven distribution
========================

    ./gradlew publishToMavenLocal

Maven Central distribution
========================

    ./gradlew publish --no-daemon --no-parallel
    ./gradlew closeAndReleaseRepository

The properties shown below are present inside the global ``~/.gradle/gradle.properties` file

    signing.password
    signing.secretKeyRingFile
    mavenCentralUsername
    mavenCentralPassword

It's possible to manually close and release the library (this replaces the gradle task `closeAndReleaseRepository`).

1. Goto to [sonatype](https://s01.oss.sonatype.org/)
2. Click on 'Staging Repositories'
3. Select the item, close then release

Android Studio configuration
============================

1. Run -> Edit Configuration
2. Add a `Gradle` task

|      Name      | Gradle Project      | Tasks               |
|:--------------:|---------------------|---------------------|
| Local Install  | ../app/build.gradle | publishToMavenLocal |