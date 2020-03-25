package com.em3.gamesdk;

/**
 * Created by MaFanwei on 2020/3/16.
 */
public class Constant {
    public static final String TAG = "EM3GameSDK::";
    public static final String INTENT_ACTION_GRANT_USB = "intent_action_grant_usb";
    public static boolean DEBUG = BuildConfig.DEBUG;
    public static final int NO_BRIGHTNESS = -5;

    public enum Eye {
        left_eye,
        right_eye
    }
}
