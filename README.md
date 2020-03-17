# GameSDK
em3 game sdk


[![Jitpack](https://jitpack.io/v/mafanwei/GameSDK.svg)](https://jitpack.io/#mafanwei/GameSDK)

## Quick Start

**1.** Add library to your project:

Add jitpack.io repository to your root build.gradle:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add library to dependencies
```gradle
dependencies {
    implementation 'com.github.mafanwei:GameSDK:Tag'
}
```
## Usage
**1.1** Init library in your application:
```java
 GameSDK.init(this);
 ```
 **1.2** implements ```GameSDK.IMUCallBack``` in your Activity:
 ```java
 public class ... extends ... implements GameSDK.IMUCallBack {
    @Override
    public void IMUChanged(int[] data) {
      //6 data, including x, y, z acceleration and x, y, z angular velocity. The sequence is as follows:
      //acc_x , acc_y , acc_z , gyro_x , gyro_y , gyro_z
    }
 }
 ```
 **2.1** Register callback in this Activity:
 ```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        GameSDK.registerCallback(this);//need implements GameSDK.IMUCallBack
        ...
    }
```
**2.2** Open IMU when your want:
```java
 GameSDK.openIMU();
```
**2.3** Release IMU when your want(normally we release it on destory):
```java
@Override
    protected void onDestroy() {
        super.onDestroy();
        ...
        GameSDK.unregisterCallback();
        GameSDK.releaseIMU();
        ...
    }
```
## Optional
When you call ``` GameSDK.openIMU();```, this method will try to obtain permissions, but this method does not have a callback, so if you want to automatically open the IMU after obtaining permissions, you need to add the following content in ```onResume```:
```java
    @Override
    protected void onResume() {
        super.onResume();
        ...
        GameSDK.registReceiver();
        ...
    }
```
And call this method at the right time:
```java
    @Override
    protected void onPause() {
        super.onPause();
        ...
        GameSDK.unregisterReceiver();
        ...
    }
```
Your also can stopIMU to use this:
```java
GameSDK.closeIMU();
```
but notice, It's not release ```IMUManager```. It's just like a pause. You also use this after ```GameSDK.closeIMU();```:
```java
GameSDK.openIMU();
```
to reopen it.

## Other
now it's fake 6dof,here is usage:

 **1.1** implements ```Fake6DofManager.QuaternionListener``` in your Activity:
 ```java
 public class ... extends ... implements Fake6DofManager.QuaternionListener {
    @Override
    public void quaternionChanged(float[] data) {
       //do something.
    }
 }
 ```
 **1.2** Register callback in this Activity:
 ```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        GameSDK.set6DofListener(this);
        ...
    }
```
**1.3** When you need data, call this method:
```java
 GameSDK.callFake6DofListener();//Always returns {0.0,0.0,0.0,0.0}
```
All Done！
## Screenshot
here is 6Dof:

![6Dof](https://raw.githubusercontent.com/mafanwei/GameSDK/master/screenshot/6dof.png)

here is IMU:

![imu](https://raw.githubusercontent.com/mafanwei/GameSDK/master/screenshot/imu.png)
