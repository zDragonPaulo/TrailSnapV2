# TrailSnapV2

Simple Android app for capturing, annotating and sharing outdoor trails and hikes with photos, GPS tracks and goals.

## People involved
- Martinho Caeiro - 23917
- Paulo Abade - 23919
- Rafael Narciso - 24473

## How to run (Android Studio)

Prerequisites
- Android Studio (recommended: Arctic Fox or newer)
- Android SDK (install via Android Studio)
- JDK 11 or newer
- A connected Android device with USB debugging enabled, or an Android emulator

Quick steps
1. Clone the repo
   ```
   git clone https://github.com/zDragonPaulo/TrailSnapV2.git
   cd TrailSnapV2
   ```

2. Open the project in Android Studio
   - Launch Android Studio → Open → select the project folder (the Gradle project).
   - Wait for Gradle sync and dependency download to finish.

3. Configure local SDK path (if needed)
   - Android Studio usually configures this automatically.
   - If required, set sdk.dir in local.properties to your SDK path:
     ```
     sdk.dir=/path/to/Android/Sdk
     ```

4. Run the app
   - Start an emulator or connect a device.
   - In Android Studio: select the target and click Run (or use Run > Run 'app').

5. Build from the command line
   - Debug APK and install to a connected device:
     ```
     ./gradlew assembleDebug
     ./gradlew installDebug
     ```
   - Release build (configure signing in app/build.gradle or use a signing config):
     ```
     ./gradlew assembleRelease
     # or to produce an AAB
     ./gradlew bundleRelease
     ```

Notes
- If you hit Gradle/SDK errors, open Android Studio's Build window for specific guidance and ensure your compileSdkVersion and installed SDK platforms match.

```
