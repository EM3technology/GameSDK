package com.em3.gamesdk;

import android.content.Context;
import android.renderscript.Matrix4f;
import android.util.Log;

import static com.em3.gamesdk.Constant.DEBUG;

/**
 * Created by MaFanwei on 2020/3/16.
 */
    public class GameSDK {

    private static Context mContext;
    private static GameSDK gameSDK;
    private static IMUManager imuManager;
    private static IMUManager.IMUDataListener imuDataListener;
    private static float[] imuData = {0, 0, 0, 0, 0, 0};
    private static IMUCallBack imuCallBack;

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
                if (check(data)) {
                    if (imuCallBack != null)
                        imuCallBack.IMUChanged(imuData);
                } else {
                    if (DEBUG)
                        Log.w(Constant.TAG, "data not pass check. ignore");
                }
            }
        };
        imuManager = new IMUManager(context, imuDataListener, 0);
    }

    private static boolean check(byte[] data) {
        String s = "";
        int i = 0;
        for (; i < data.length; i++) {
            s = s + data[i] + " ";

        }
        if (data.length != 30) {
            if (DEBUG)
                Log.w(Constant.TAG, "data is not normal length:" + data.length);
            return false;
        }

        for (i = 0; i < 3; i++) {
            imuData[i] = (float) (toSignedInt(new String(data, 2 + i * 4, 4)) / (32768.0 / 20.0));
        }

       /* for (; i < 6; i++) {
            imuData[i] = new Integer(toInt(new String(data, 2 + i * 4, 4)) * 2000 / 32768).shortValue();
        }*/

        for (; i < 6; i++) {
            imuData[i] = (float) (toSignedInt(new String(data, 2 + i * 4, 4)) / (16.384 * 57.30));
        }

        int flag = toInt(new String(data, 26, 2));
        int result = 0;
        for (i = 0; i < imuData.length * 2; i++) {
            result += toInt(new String(data, 2 + i * 2, 2));
        }
        return flag == (result & 0xff);
    }

    private static int toSignedInt(String s) {
        return (Integer.valueOf(s, 16).shortValue());
        // return Integer.parseInt(s,16) & 0x0FFFF;
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
        matrix4f.set(0,0,100);
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

}
