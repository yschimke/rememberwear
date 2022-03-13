fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android test

```sh
[bundle exec] fastlane android test
```

Runs all the tests

### android internal_link

```sh
[bundle exec] fastlane android internal_link
```

Submit a new Internal Build and get link

### android internal

```sh
[bundle exec] fastlane android internal
```

Submit a new Internal Testing Build to Google Play

### android beta

```sh
[bundle exec] fastlane android beta
```

Submit a new Beta Build to Google Play

### android deploy

```sh
[bundle exec] fastlane android deploy
```

Deploy a new version to the Google Play

### android screenshots

```sh
[bundle exec] fastlane android screenshots
```



### android increment

```sh
[bundle exec] fastlane android increment
```



----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
