package com.em3.example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.em3.gamesdk.Constant;
import com.em3.gamesdk.GameSDK;
import com.em3.gamesdk.IMUSensorManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements GameSDK.IMUCallBack, IMUSensorManager.SensorDataChangedListener {

    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameSDK.registerCallback(this);
        GameSDK.setSensorDataChangedListener(this);
        textView = findViewById(R.id.tv);
        editText = findViewById(R.id.edt_brightness);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameSDK.openIMU();
            }
        });
        findViewById(R.id.btn_set_brightness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ans = GameSDK.changeBrightness(editText.getText().toString());
                Toast.makeText(MainActivity.this, "写入结果：" + ans, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_get_brightness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int brightness = GameSDK.getBrightness();
                        if (brightness == Constant.NO_BRIGHTNESS) {
                            Toast.makeText(MainActivity.this, "获取亮度失败！可能是没打开设备", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "当前亮度：" + brightness, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        findViewById(R.id.btn_reset_3dof).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean b = GameSDK.reset3Dof();
                Toast.makeText(MainActivity.this, "重置3dof："+b, Toast.LENGTH_SHORT).show();
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
    public void IMUChanged(float[] data) {
        String s = "";
        for (int i = 0; i < data.length; i++) {
            s = s + data[i] + "   ";
        }
        Log.d("IMUChanged::::", s);
        final String finalS = s;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String text = textView.getText().toString();
                if (text.length() > 500) {
                    text = text.substring(0, 500);
                }
                text = finalS + text;
                textView.setText(text);
            }
        });
    }

    @Override
    public void onSensorDataChanged(int light, int proximity) {
        Log.d("onSensorDataChanged","light: "+light +" proximity: "+proximity);
    }
}
