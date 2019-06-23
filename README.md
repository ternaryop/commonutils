commonutils
===========

Android common utils for my own projects

Local Maven distribution
========================

    ./gradlew uploadArchives

Bintray distribution
========================

    ./gradlew bintrayUpload


Android Studio configuration
============================

1. Run -> Edit Configuration
2. Add a `Gradle` task

|      Name      | Gradle Project      | Tasks          |
|:--------------:|---------------------|----------------|
| uploadArchives | ../app/build.gradle | uploadArchives |