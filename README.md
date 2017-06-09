# Lantern-sdk

## How to use it

1. Setting app.gradle

  ```
  // Apply Lantern Plugin
  apply plugin: 'hello.thinkcode.demo.plugin'

  // Add Build script dependencies
  buildscript {
    repositories {
            jcenter()
    }
    dependencies {
      classpath 'com.lantern:lantern-injector:0.1.63'
      ...
    }
  }
  ```

2. Create your own application file (ex. MyApplication.java)
3. Add Lantern API to execute on your Application class
  ( Insert "PROJECT_KEY" in here )

  ```
  public class MyApplication extends Application {

    ...
    @Override
    public void onCreate() {
      super.onCreate();
      com.lantern.lantern.RYLA.getInstance().setContext("PROJECT_KEY", this).startResDump();
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
