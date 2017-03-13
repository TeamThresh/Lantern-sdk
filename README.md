# Lantern-sdk

## How to use it

1. Create your own application file (ex. MyApplication.java)
2. Add Lantern API to execute in MyApplication.java

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
3. Change main application class in AndroidManifest.xml

  ```
  <application
      android:name=".MyApplication"
      ...>
  ...
  </application>
  ```
4. Add permission in AndroidManifest.xml

  ```
  ...
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  ...
  ```
