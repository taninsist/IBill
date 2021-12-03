package com.example.mybill.model;

import java.io.Serializable;

public class BillModel implements Serializable {
    private int id;
    private int type;
    private int money;
    private String desc;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BillModel() {

    }

    public BillModel(int id, int type, int money, String desc, String date) {
        this.id = id;
        this.type = type;
        this.money = money;
        this.desc = desc;
        this.date = date;
    }
}
