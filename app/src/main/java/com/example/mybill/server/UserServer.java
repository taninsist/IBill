package com.example.mybill.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class UserServer extends MyDatabaseHelper {

    public UserServer(@Nullable Context context) {
        super(context);
    }

    //注册一个用户
    public int addUser(String account, String password) {
        int result = 1; // 用于返回函数调用的结果 0:用户名存在，添加失败；1:添加成功
        SQLiteDatabase db = getWritableDatabase();

        boolean isAdd = true;

        //先判断用户名是否存在
        String selSql = "SELECT * FROM 'users' where account=?;";
        Cursor re = db.rawQuery(selSql, new String[]{account});
        if (re.getCount() != 0) {
            //说明该用户名以存在
            result = 0; //用户存在，添加失败
            isAdd = false;
        }

        if (isAdd) {
            String addSql = "insert into users values(null,?,?);";
            Object arr[] = {account, password};
            db.execSQL(addSql, arr);
        }
        db.close();
        return result;
    }

    //login
    public Cursor login(String account, String password) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT id,account FROM 'users' WHERE account=? and password=?";
        String arr[] = {account, password};
        Cursor c = db.rawQuery(sql, arr);
        int re = c.getCount(); //re为查询到的结果，0为没有找到，即登陆失败；1为查询到一条结果，表示登陆成功
        //db.close();
        System.out.println(re);
        return c;
    }

    //whoami
    public int whoami(String id, String account) {
        Cursor result;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM 'users' WHERE id=? and account=?";
        String arr[] = {id, account};
        result = db.rawQuery(sql, arr);
        return result.getCount();

    }

    public void loginOut(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
