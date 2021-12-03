package com.example.mybill;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import com.example.mybill.view.Welcome;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);


        init();
    }


    //active不在可见时执行该生命周期函数
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void init(){

    }

}
