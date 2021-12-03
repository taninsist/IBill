package com.example.mybill.util;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {


    // 账单文字类型（string） 转 数字(int)
    public int getTypeInt(String stringType) {
        //记录添加的类型  0：支出；1：收入；2：借贷
        if ("支出".equals(stringType)) {
            return 0;
        } else if ("收入".equals(stringType)) {
            return 1;
        } else {
            return 2;
        }
    }

    //判断是否符合密码标准
    public boolean isPassword(String password) {
        String regex = "^(?![0-9])(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        Boolean isMatch = m.matches();
        Log.i("tag", "isPassword 是否正则匹配" + isMatch);
        return isMatch;
    }

    public boolean isAccount(String account) {
        boolean result = isPassword(account);
        Log.i("tag", "isPassword 是否正则匹配" + result);

        return result;
    }


}
