# Naarad-kotlin-sdk


For documentation on how to use this SDK, you can refer here: [Documentation](https://d3bhwtjk5vuhv4.cloudfront.net/docs).

This repo also contains an example application under the `app` folder. The app uses the Naarad module to integrate with the Naarad SDK. The process used is also detailed in the docs above.


## Add Naarad to your Android app


**Note: Naarad only supports Android applications as of now!**

If you have not registered your app, do it here: [link](https://d3bhwtjk5vuhv4.cloudfront.net/getting_started)

Once an app is successfully registered, a few application changes are required for integration to be complete. Below steps assume you have an Android application setup locally with the registered package name.

### Add Naarad SDK to your app

In your project's build.gradle file, add the following line:

``
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
``

**Note: You can also add the repository using your settings.gradle file.**

In your module (app-level) Gradle file, add the dependencies for the Naarad SDK.

```
dependencies {
  .....
  implementation 'com.github.Wert1996:Naarad-kotlin-sdk:main-SNAPSHOT'
  implementation 'com.google.firebase:firebase-messaging-ktx:23.1.2'
}
```

### Initialise Naarad on your app

On app startup, initialise Naarad.

```
import com.naarad.sdk.auth.NaaradAuth
   .......
   NaaradAuth(context, dappName, apiKey, walletAddress).initialiseApp()
   ....
```

Once successful, you should start receiving push notifications in their default format.

### Customize your notifications

Add a service that extends NaaradMessagingService. You would be able to add custom code for handling messages through this service.
```
<service
  android:name=".java.NotificationHandlerService"
  android:exported="false">
  <intent-filter>
    <action android:name="com.google.firebase.MESSAGING_EVENT" />
  </intent-filter>
</service>
```


To build your custom notification, override the method 

`buildNotification(remoteMessage: NaaradRemoteMessage): NotificationCompat.Builder`

Return a custom notification object from the above method.
