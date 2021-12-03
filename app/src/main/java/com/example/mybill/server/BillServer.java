package com.example.mybill.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mybill.model.BillModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;


public class BillServer extends MyDatabaseHelper {

//    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
//    String id = sharedPreferences.getString("user_id", null);

    public BillServer(@Nullable Context context) {
        super(context);
    }


    /**
     * 添加一个账单记录
     *
     * @param type   账单类型 0 支出，1  收入， 2 借贷
     * @param desc   描述
     * @param amount 金额
     * @param date   时间  format xxxx-xx-xx
     * @param ctx    上下文
     */
    public void addBill(Integer type, String desc, Integer amount, String date, Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString("user_id", null); //userId
        SQLiteDatabase db = getWritableDatabase();

        String typeS = type + "";
        String amountS = amount + "";
//        String id = LOGINUSERID + "";  //登录用户的id

        String sql = "INSERT into bills VALUES(null,?,?,?,?,?);";
        String arr[] = {typeS, desc, amountS, date, userid};

        db.execSQL(sql, arr);

        db.close();
    }

    //查询所有
    public Cursor findAll(String userid) {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "select * from bills where userid=?";

        Cursor c = db.rawQuery(sql, new String[]{userid});

        return c;
    }

    /**
     * @param date   查询时间： xxxx-xx;  年-月
     * @param userid 用户id
     * @return 返回 Cursor 结果集
     */

    public Cursor findByDate(String date, String userid) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor resutl;
        String sql = "select * from bills where date like ? and userid=?";
        String args[] = new String[]{date+"%%", userid};

        resutl = db.rawQuery(sql, args);

        return resutl;
    }

    // 删除账单    传账单的 id
    public void del(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from bills where id = ?";
        Object args[] = new Object[]{id};
        db.execSQL(sql, args);
    }

    //修改 单个账单信息


    public void update(BillModel bill) {
        SQLiteDatabase db = getWritableDatabase();

        Integer id = bill.getId();
        Integer type = bill.getType();
        Integer money = bill.getMoney();
        String desc = bill.getDesc();
        String date = bill.getDate();

        String sql = "update bills set type=?,'desc'=?,amount=?,date=? where id=?";
        Object args[] = new Object[]{type, desc, money, date, id};

        db.execSQL(sql, args);
    }


    //账单模拟数据
//    public void BillMock() {
//        addBill(0, "买鞋", 200);
//        addBill(0, "买鞋", 200);
//        addBill(1, "兼职", 200);
//        addBill(0, "买鞋", 200);
//        addBill(0, "买鞋", 200);
//        addBill(2, "亲戚", 300);
//    }


}
