package com.em3.example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.em3.gamesdk.GameSDK;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements GameSDK.IMUCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameSDK.registerCallback(this);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameSDK.openIMU();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameSDK.registReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameSDK.unregisterReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameSDK.unregisterCallback();
        GameSDK.releaseIMU();
    }

    @Override
    public void IMUChanged(int[] data) {
        String s = "";
        for (int i = 0; i < data.length; i++) {
            s = s + data[i] + " ";
        }
        Log.d("IMUChanged::::", s);
    }
}
