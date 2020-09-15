package com.example.lightcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener,
        View.OnClickListener{
    private List<Record> recordList = new ArrayList<>();
    private DBOpenHelper mDBOpenHelper;
    private CheckBox cb_auto;
    private TextView tv_light;
    private SensorManager mSensroMgr;
    private Button bt_begin;
    boolean isbegin = false;
    int[][] record = new int[11][2];
    int i = 0;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cb_auto = (CheckBox) findViewById(R.id.cb_auto);
        tv_light = (TextView) findViewById(R.id.tv_light);
        bt_begin = (Button) findViewById(R.id.bt_mainactivity_begin);
        mSensroMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mDBOpenHelper = new DBOpenHelper(this);

        Intent nameintent = getIntent();
        name = nameintent.getStringExtra("name");


        //Record record = new Record("xdd","2020/7/17 19:17:30","95");
        //recordList.add(record);
        recordList = mDBOpenHelper.getAllRecordData(name);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensroMgr.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensroMgr.registerListener(this, mSensroMgr.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float light_strength = event.values[0];
            int light_int = Math.round(light_strength);
            tv_light.setText(Time.getNowDateTimeFormat() + "\n当前光线强度为：" + light_int + "Lux");
            if(isbegin)
            {
                if(i<11) {
                    record[i][0] = light_int;
                    if(i == 0) {
                        record[i][1] = Time.getNowDateTime();
                    }
                    else  {
                        record[i][1] = Time.getNowDateTime() - record[0][1];
                        if(record[i][1] - record[i-1][1] == 0 || record[i][1] == 0)
                            i--;
                    }
                    i++;

                }
                else
                {
                    i = 0;
                    for(int i = 10; i > 1;i--)
                    {
                        record[i][1] = record[i][1] - record[i-1][1];
                    }

                    int average = average(record);
                    String time = Time.getNowDateTimeFormat();
                    mDBOpenHelper.add_record(name,time,String.valueOf(average));

                    bt_begin.setEnabled(true);
                    bt_begin.setVisibility(View.VISIBLE);

                    recordList = mDBOpenHelper.getAllRecordData(name);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(layoutManager);
                    RecordAdapter adapter = new RecordAdapter(recordList);
                    recyclerView.setAdapter(adapter);

                    Toast.makeText(this, "当前平均光照强度为：" + String.valueOf(average), Toast.LENGTH_LONG).show();
                }

            }

            if(cb_auto.isChecked())
            {
                //设置亮度
                float brightness = light_strength/600f;
                if (brightness>1)
                    brightness = 1;
                WindowManager.LayoutParams lp = this.getWindow().getAttributes();
                lp.screenBrightness = brightness;
                this.getWindow().setAttributes(lp);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //当传感器精度改变时回调该方法，一般无需处理
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_mainactivity_back) {
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
            finish();
        }
        if (view.getId() == R.id.bt_mainactivity_begin) {
            isbegin = true;
            bt_begin.setEnabled(false);
            bt_begin.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "正在收集数据中，请勿退出！", Toast.LENGTH_LONG).show();
        }

    }

    protected int average(int[][] recode) {
        int time = 0;
        int lux_time = 1;
        for(int i = 0;i < 10;i++)
        {
            time = time + recode[i+1][1];
            lux_time = lux_time + recode[i][0]*recode[i+1][1];
            Log.i("record",""+i+":"+record[i][0]+","+record[1+1][1]);
        }
        int light_average = lux_time/time;
        Log.i("record",""+light_average);
        return light_average;
    }
}