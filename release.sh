# To bump the minor version use:
# ./gradlew marNextVersion -Prelease.incrementer=incrementMinor

# Bumpt patch version for release version by default
 ./gradlew release

# Upload to bintray
 ./gradlew bintrayUpload