package com.example.mybill.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.server.UserServer;

public class Register extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private EditText conPwd;
    private Button registerBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        conPwd = (EditText) findViewById(R.id.conPwd);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inAccount;
                String inPassword;
                String inConPwd;

                inAccount = account.getText().toString();
                inPassword = password.getText().toString();
                inConPwd = conPwd.getText().toString();

                if (!inPassword.equals(inConPwd)) {
                    //两次密码不一致
                    Toast toast = Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                UserServer userServer = new UserServer(Register.this);
                int re = userServer.addUser(inAccount, inPassword);
                //0:用户名存在，添加失败；1:添加成功
                if (re == 1) {
                    Toast toast = Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "用户名以存在", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //点击取消按钮，销毁界面
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
