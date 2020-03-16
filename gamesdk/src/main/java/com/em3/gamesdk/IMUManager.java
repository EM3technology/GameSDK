package com.em3.gamesdk;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.em3.gamesdk.Constant.DEBUG;
import static com.em3.gamesdk.Constant.INTENT_ACTION_GRANT_USB;

/**
 * Created by MaFanwei on 2020/3/16.
 */
public class IMUManager {

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private SerialInputOutputManager mSerialIoManager;
    private static UsbSerialPort sPort = null;
    private static UsbManager manager;
    private Context context;
    private int whichDevice;

    private BroadcastReceiver mUsbReceiver;
    private List<UsbSerialDriver> availableDrivers;
    private IMUDataListener listener;

    public static interface IMUDataListener {
        public void getIMUData(byte[] data);
    }

    public List<UsbSerialDriver> getAvailableDrivers() {
        return availableDrivers;
    }

    public IMUManager(Context context, IMUDataListener imuDataListener, int wanted) {
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        this.context = context;
        listener = imuDataListener;
        whichDevice = wanted;
        mUsbReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(INTENT_ACTION_GRANT_USB)) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        openDevice(whichDevice);
                    } else {
                        Log.e(Constant.TAG, "USB permission denied");
                    }
                }
            }
        };
    }

    public void registerReceiver() {
        context.registerReceiver(mUsbReceiver, new IntentFilter(INTENT_ACTION_GRANT_USB));
    }

    public void unregisterReceiver() {
        context.unregisterReceiver(mUsbReceiver);
    }

    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.e(Constant.TAG, e.getMessage());
                }

                @Override
                public void onNewData(byte[] data) {
                    listener.getIMUData(data);
                }
            };

    public void prepareDevice() {
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            ProbeTable customTable = new ProbeTable();
            customTable.addProduct(0x1234, 0x0001, CdcAcmSerialDriver.class);
            customTable.addProduct(1155, 22336, CdcAcmSerialDriver.class);

            UsbSerialProber prober = new UsbSerialProber(customTable);
            List<UsbSerialDriver> drivers = prober.findAllDrivers(manager);
            availableDrivers = drivers;
            if (drivers == null) {
                Log.e(Constant.TAG, "available Drivers is Empty");
                return;
            }
        }
        UsbSerialDriver driver = availableDrivers.get(whichDevice);

        if (!manager.hasPermission(driver.getDevice())) {
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(INTENT_ACTION_GRANT_USB), 0);
            manager.requestPermission(driver.getDevice(), usbPermissionIntent);
            if (DEBUG)
                Log.i(Constant.TAG, "connection = null,try to get permission");
            return;
        }
        openDevice(whichDevice);
    }

    private void openDevice(int whichDevice) {
        UsbSerialDriver driver = availableDrivers.get(whichDevice);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        sPort = driver.getPorts().get(0); // Most devices have just one port (port 0)
        try {
            sPort.open(connection);
            sPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            startIoManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            if (DEBUG)
                Log.i(Constant.TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            if (DEBUG)
                Log.i(Constant.TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    public void closeDevice() {
        startIoManager();
        if (sPort != null) {
            try {
                sPort.close();
            } catch (IOException e) {
                // Ignore.
            }
            sPort = null;
        }
    }

}
