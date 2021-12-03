package com.example.mybill.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

import com.example.mybill.R;
import com.example.mybill.server.UserServer;

public class Welcome extends AppCompatActivity {

    private WebView wecomeWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        wecomeWeb = (WebView) findViewById(R.id.wecomeWeb);

        wecomeWeb.loadUrl("file:android_asset/index.html");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */

                if (whoami()) {
//            //以登录
                    Intent intent = new Intent(getApplicationContext(), Bill.class);
                    startActivity(intent);
                } else {
                    //为登录跳login.xml
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }

            }
        }, 16000);//3秒后执行Runnable中的run方法

    }


    public Boolean whoami() {
        boolean result = false;
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("user_id", null);
        String account = sharedPreferences.getString("user_account", null);

        if (id != null && account != null) {
            UserServer userServer = new UserServer(getApplicationContext());
            int re = userServer.whoami(id, account);
            if (re == 1) {
                result = true;
            }
        }
        return result;
    }

    //active不在可见时执行该生命周期函数
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}


