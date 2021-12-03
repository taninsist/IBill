package com.example.mybill.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.model.UserModel;
import com.example.mybill.server.UserServer;


public class Login extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private Button LoginBtn;
    private TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        LoginBtn = (Button) findViewById(R.id.loginBtn);
        registerText = (TextView) findViewById(R.id.registerBtn);


        //点击登录
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inAccount;
                String inPassword;

                inAccount = account.getText().toString();
                inPassword = password.getText().toString();



                UserServer userServer = new UserServer(Login.this);
                Cursor resp = userServer.login(inAccount, inPassword);

                int cou = resp.getCount();
                if (cou == 0) {
                    //登录失败
                    Toast toast = Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else {

                    if (cou != 1) {
                        Toast toast = Toast.makeText(getApplicationContext(), "error account count > 1,in Login.xml", Toast.LENGTH_SHORT);
                        toast.show();

                    }

                    for (resp.moveToFirst(); !resp.isAfterLast(); resp.moveToNext()) {
                        UserModel user = new UserModel();
                        user.setId(resp.getInt(0));
                        user.setAccount(resp.getString(1));
                        //记录登录信息
                        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("user_id", user.getId() + "");
                        editor.putString("user_account", user.getAccount());
                        editor.commit();

                        resp.close();
                    }

                    Toast toast = Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT);
                    toast.show();

//
//                    //登录成功
                    Intent intent = new Intent(Login.this, Bill.class);
                    startActivity(intent);
                    finish();
                    //模拟数据
                    //BillServer billServer = new BillServer(Login.this);
                    //billServer.BillMock(); //模拟账单数据
                }


            }
        });

        //跳转注册页面
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentW = new Intent(Login.this, Register.class);
                startActivity(intentW);
            }
        });


    }

    //active不在可见时执行该生命周期函数
//    @Override
//    protected void onStop() {
//        super.onStop();
//        finish();
//    }
}
