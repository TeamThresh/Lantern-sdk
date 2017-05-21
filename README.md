# Lantern-sdk

## How to use it

1. Setting app.gradle

  ```
  // Apply Lantern Plugin
  apply plugin: 'hello.thinkcode.demo.plugin'

  // Add Build script dependencies
  buildscript {
    ...
    dependencies {
      classpath 'lantern.thresh.io:demoPlugin:1.0.0-SNAPSHOT'
      ...
    }
  }
  ```

2. Create your own application file (ex. MyApplication.java)
3. Add Lantern API to execute on your Application class

  ```
  public class MyApplication extends Application {

    ...
    @Override
    public void onCreate() {
      super.onCreate();
      com.lantern.lantern.RYLA.getInstance().setContext(this).startResDump();
      ...
    }
    ...
  }
  ```

4. Add permission in AndroidManifest.xml

  ```
  ...
  <uses-permission android:name="android.permission.INTERNET"/>
  ...
  ```

5. Generate your APK file!
