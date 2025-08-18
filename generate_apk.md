# How to Generate APK for HaramBlur

Follow these steps to generate an APK file that you can share with your friends:

## Using Android Studio

1. Open the project in Android Studio
2. From the menu, select **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**
3. Wait for the build process to complete
4. Android Studio will show a notification when the build is finished
5. Click on the "locate" link in the notification to find your APK file
6. The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`
7. Share this APK file with your friends

## Using Command Line (Alternative)

If you have the Android SDK installed and configured correctly, you can run:

```
./gradlew assembleDebug
```

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

## Installing the APK

To install the APK on an Android device:

1. Enable "Install from Unknown Sources" in your device settings
2. Transfer the APK file to your device
3. Tap on the APK file to install it
4. Follow the on-screen instructions to complete installation

## Minimum Requirements

- Android 8.0 (API level 26) or higher
- 2GB RAM recommended
- 50MB free storage space
