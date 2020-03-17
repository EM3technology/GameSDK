package com.em3.example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.em3.gamesdk.Fake6DofManager;
import com.em3.gamesdk.GameSDK;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements GameSDK.IMUCallBack, Fake6DofManager.QuaternionListener {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameSDK.registerCallback(this);
        GameSDK.set6DofListener(this);
        textView = findViewById(R.id.tv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameSDK.openIMU();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameSDK.callFake6DofListener();
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
        final String finalS = s;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(finalS);
            }
        });

    }

    @Override
    public void quaternionChanged(float[] data) {
        String s = "";
        for (int i = 0; i < data.length; i++) {
            s = s + data[i] + " ";
        }
        Log.d("quaternionChanged:::",s);
    }
}
