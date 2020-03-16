package com.em3.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.em3.gamesdk.GameSDK;

public class MainActivity extends AppCompatActivity implements GameSDK.IMUCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameSDK.init(this);
        GameSDK.registerCallback(this);
        GameSDK.openIMU();
    }

    @Override
    public void IMUChanged(int[] data) {
        String s="";
        for(int i = 0;i< data.length;i++) {
            s= s + data[i]+" ";
        }
        Log.d("asdasdasddata::::",s);
    }
}
