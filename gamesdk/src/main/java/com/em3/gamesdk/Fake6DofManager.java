package com.em3.gamesdk;

/**
 * Created by MaFanwei on 2020/3/17.
 */
public class Fake6DofManager {

    public static QuaternionListener quaternionListener;

    public static void setQuaternionListener(QuaternionListener listener) {
        quaternionListener = listener;
    }

    public static interface QuaternionListener {
        public void quaternionChanged(float[] data);
    }

    public static void callListener() {
        float[] data = new float[]{0,0,0,0};
        quaternionListener.quaternionChanged(data);
    }
}
