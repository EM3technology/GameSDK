package com.em3.gamesdk;

/**
 * Created by MaFanwei on 2020/3/18.
 */
public class PoseBean {
    public float[] pos;
    public float[] qua;

    public PoseBean() {
        pos = new float[]{0, 0, 0};
        qua = new float[]{0, 0, 0, 0};
    }

    public String toString() {
        String s = "pos = ";
        int i = 0;
        for (; i < pos.length; i++) {
            s = s + pos[i] + " ";
        }
        s = s+"qua = ";
        for (i = 0; i < qua.length; i++) {
            s = s + qua[i] + " ";
        }
        return s;
    }
}
