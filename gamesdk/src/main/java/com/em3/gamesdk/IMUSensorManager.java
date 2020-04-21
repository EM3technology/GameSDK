package com.em3.gamesdk;

public class IMUSensorManager {
    private int sensorLight = Constant.NO_SENSOR_DATA;
    private int sensorProximity = Constant.NO_SENSOR_DATA;
    private SensorDataChangedListener sensorDataChangedListener;

    public void setNewSensorData(int light, int proximity) {
        if (sensorDataChangedListener != null && light != sensorLight || proximity != sensorProximity) {
            sensorLight = light;
            sensorProximity = proximity;
            sensorDataChangedListener.onSensorDataChanged(light, proximity);
        }
    }

    public static interface SensorDataChangedListener {
        public void onSensorDataChanged(int light, int proximity);
    }

    public void setSensorDataChangedListener(SensorDataChangedListener sensorDataChangedListener) {
        this.sensorDataChangedListener = sensorDataChangedListener;
    }
}
