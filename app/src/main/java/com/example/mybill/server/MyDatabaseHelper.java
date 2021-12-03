package com.example.mybill.server;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * INSERT 增 没有返回结果
 * DELETE 删除  没有返回结果
 * update 修改  无论是否成功，都没有返回结果
 * select 查  有返回结果， Cursor类
 */


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASENAME = "billdb.db";
    private static int VERSION = 1;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASENAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {




        createUsersTable(db); //创建用户表
        createBillTable(db);  //创建账单表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //创建用户表
    public void createUsersTable(SQLiteDatabase db) {
        String sqlString = "CREATE TABLE 'users' ('id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 'account' String(4) NOT NULL, 'password' String(18) NOT NULL)";
        db.execSQL(sqlString);
    }

    //创建账单表
    public void createBillTable(SQLiteDatabase db) {
        //String sql = "CREATE TABLE 'bills' ('id' INTEGER NOT NULL, 'type' integer NOT NULL, 'desc' String, 'amount' integer, 'date' String NOT NULL, 'userid' INTEGER, PRIMARY KEY ('id'), CONSTRAINT 'userid' FOREIGN KEY ('userid') REFERENCES 'users' ('id') ON DELETE NO ACTION ON UPDATE NO ACTION);";
        String sql = "CREATE TABLE 'bills' ( 'id' integer NOT NULL PRIMARY KEY AUTOINCREMENT, 'type' integer NOT NULL, 'desc' text, 'amount' integer, 'date' text NOT NULL, 'userid' integer NOT NULL, CONSTRAINT 'userid' FOREIGN KEY ('userid') REFERENCES 'users' ('id') ON DELETE NO ACTION ON UPDATE NO ACTION)";

        db.execSQL(sql);
    }



}
