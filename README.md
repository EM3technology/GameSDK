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
    public void IMUChanged(float[] data) {
      //6 data, including x, y, z acceleration and x, y, z angular velocity. The sequence is as follows:
      //acc_x , acc_y , acc_z , gyro_x , gyro_y , gyro_z
      //acc unit is m/s2
      //gyro unit is rad/s
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
Now it's fake 6dof, here is 6Dof api:
```java
GameSDK.getProjectionMatrix4f(); //return Matrix(4 * 4): fov，ratio，near，far
GameSDK.getEyeFov(eye); //return eye fov.
GameSDK.getLeftHand6Dof(); //return left hand 6Dof.
GameSDK.getRightHand6Dof(); //return right hand 6Dof.
GameSDK.getEyePoseFromHead(eye); //return the relative position and rotation of an eye
GameSDK.getEyePoseFromHeadMat(eye); //return the transformation matrix of the opposite head of an eye (4 * 4)
GameSDK.getHeadPosePredictied(ms); //return predict head 6Dof pose after given ms.
```
Since version 0.0.6, you can hide unnecessary logs:
```java
GameSDK.hideLog();
```
## Brightness
You can change the brightness with the following code:
```java
GameSDK.changeBrightness(brightness); //range 0-511
```
And read brightness :
```java
GameSDK.getBrightness();
```
All Done！
## Screenshot
here is IMU:

![imu](https://raw.githubusercontent.com/mafanwei/GameSDK/master/screenshot/imu.png)
