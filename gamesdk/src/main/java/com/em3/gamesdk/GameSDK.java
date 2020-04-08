package com.em3.gamesdk;

import android.content.Context;
import android.renderscript.Matrix4f;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

import static com.em3.gamesdk.Constant.DEBUG;
import static com.em3.gamesdk.Constant.TAG;

/**
 * Created by MaFanwei on 2020/3/16.
 */
public class GameSDK {

    private static Context mContext;
    private static GameSDK gameSDK;
    private static IMUManager imuManager;
    private static IMUManager.IMUDataListener imuDataListener;
    private static float[] imuData;
    private static IMUCallBack imuCallBack;
    private static int brightness = Constant.NO_BRIGHTNESS;

    public static interface IMUCallBack {
        public void IMUChanged(float[] data);
    }

    private GameSDK() {
    }

    public static void init(Context context) {
        mContext = context;
        imuDataListener = new IMUManager.IMUDataListener() {
            @Override
            public void getIMUData(byte[] data) {
                if (checkAndSet(data)) {
                    if (imuCallBack != null)
                        imuCallBack.IMUChanged(imuData);
                } else {
                    if (DEBUG)
                        Log.w(TAG, "data not pass checkAndSet. ignore");
                }
            }
        };
        imuManager = new IMUManager(context, imuDataListener, 0);
    }

    private static boolean checkAndSet(byte[] data) {
        String s = "";
        int i = 0;
        for (; i < data.length; i++) {
            s = s + data[i] + " ";

        }
        if (data.length != 50) {
            if (DEBUG)
                Log.w(TAG, "data is not normal length:" + data.length + " update imu or you should use old version");
            return false;
        }

        imuData = new float[(data.length - 2) / 4 - 1];

        //acc
        for (i = 0; i < 3; i++) {
            imuData[i] = (float) (toSignedInt(new String(data, 2 + i * 4, 4)) / (32768.0 / 80.0));
        }

        //gry
        for (; i < 6; i++) {
            imuData[i] = (float) (toSignedInt(new String(data, 2 + i * 4, 4)) / (16.384 * 57.30));
        }

        //bright
        brightness = toInt(new String(data, 2 + i * 4, 4));
        i++;

        //3DOF
        for (; i < 10; i++) {
            imuData[i] = (float) (toInt(new String(data, 2 + i * 4, 4)) / 10000.0);
        }

        //other data
        for (; i < imuData.length; i++) {
            imuData[i] = toInt(new String(data, 2 + i * 4, 4));
        }

        int flag = toInt(new String(data, data.length - 4, 2));
        int result = 0;
        for (i = 0; i < imuData.length * 2; i++) {
            result += toInt(new String(data, 2 + i * 2, 2));
        }
        return flag == (result & 0xff);
    }

    private static int toSignedInt(String s) {
        return (Integer.valueOf(s, 16).shortValue());
    }

    public static int toInt(String s) {
        return Integer.parseInt(s, 16);
    }

    public static void registerCallback(IMUCallBack callBack) {
        imuCallBack = callBack;
    }

    public static void unregisterCallback() {
        imuCallBack = null;
    }

    public static void releaseIMU() {
        imuManager.closeDevice();
        imuManager = null;
    }

    public static void openIMU() {
        imuManager.prepareDevice();
    }

    public static void closeIMU() {
        imuManager.closeDevice();
    }

    public static void registReceiver() {
        imuManager.registerReceiver();
    }

    public static void unregisterReceiver() {
        imuManager.unregisterReceiver();
    }

    private static Matrix4f getIdentity() {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.loadIdentity();
        return matrix4f;
    }

    public static Matrix4f getProjectionMatrix4f() {
        Matrix4f matrix4f = getIdentity();
        matrix4f.set(0, 0, 100);
        return matrix4f;
    }

    public static float getEyeFov(Constant.Eye eye) {
        return 100;
    }

    public static PoseBean getLeftHand6Dof() {
        return new PoseBean();
    }

    public static PoseBean getRightHand6Dof() {
        return new PoseBean();
    }

    public static PoseBean getEyePoseFromHead(Constant.Eye eye) {
        return new PoseBean();
    }

    public static Matrix4f getEyePoseFromHeadMat(Constant.Eye eye) {
        return getIdentity();
    }

    public static PoseBean getHeadPosePredictied(int predictMs) {
        return new PoseBean();
    }

    public static boolean changeBrightness(String brightness) {
        try {
            UsbSerialPort port = imuManager.getPort();
            if (port == null) {
                Log.w(TAG, "have you open device?");
                return false;
            } else {
                int value = Integer.parseInt(brightness);
                if (value >= 0 && value <= 511) {
                    String cmd = "brightnessset " + brightness;
                    port.write(cmd.getBytes(), 0);
                    return true;
                } else {
                    Log.w(TAG, "brightness must in 0~511");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "change brightness IOException:" + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            Log.e(TAG, "brightness must be Integer!!");
            return false;
        }
    }

    public static void hideLog(boolean needHide) {
        DEBUG = needHide;
    }

    public static int getBrightness() {
        return brightness;
    }
}
