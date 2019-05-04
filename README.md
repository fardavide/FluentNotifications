# Fluent Notifications

[![Download](https://api.bintray.com/packages/4face/FluentNotifications/studio.forface.fluentnotifications/images/download.svg)](https://bintray.com/4face/FluentNotifications/studio.forface.fluentnotifications/_latestVersion)  ![MinSDK](https://img.shields.io/badge/MinSDK-16-%23f44336.svg)  [![star this repo](http://githubbadges.com/star.svg?user=4face-studi0&repo=FluentNotifications&style=flat&color=fff&background=4caf50)](https://github.com/4face-studi0/FluentNotifications)
[![fork this repo](http://githubbadges.com/fork.svg?user=4face-studi0&repo=FluentNotifications&style=flat&color=fff&background=4caf50)](https://github.com/4face-studi0/FluentNotifications/fork)

###### DSL for create Android's Notifications in a fluent way with Kotlin

### [Full Doc here](https://4face-studi0.github.io/FluentNotifications/fluentnotifications/)

## Installation

`implementation( "studio.forface.fluentnotifications:fluentnotifications:$lastVersion" )`

## Basic example

```kotlin
context.showNotification( 123, "someTag" ) {

    class EmptyActivity: FragmentActivity()

    behaviour {
        importance = NotificationImportance.HIGH
        this + defaultVibration
    }

    channel( "channelId", "channelName" ) {
        description = "No description"
    }

    notification {
        smallIconRes = 0
        title = "Title"
        contentText = "Content"
        onContentAction { startActivity<EmptyActivity>() }
    }
}
```
