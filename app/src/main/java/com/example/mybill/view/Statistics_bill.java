package com.example.mybill.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.model.BillModel;
import com.example.mybill.server.BillServer;
import com.example.mybill.util.FormatDate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Statistics_bill extends AppCompatActivity {
    private String USERID;
    //获取当前 年月
    Calendar calendar = Calendar.getInstance();
    int nowYear = calendar.get(Calendar.YEAR); //现在的年份
    int nowMonth = calendar.get(Calendar.MONTH) + 1; //现在的月份
    String nowSelectDate = nowYear + "-" + nowMonth; //统计界面的查询时间 默认当前年月

    private Button selectBtn; // 查询按钮
    private EditText selectDateYear; //查询的年份
    private EditText selectDateMonth; //查询的月份
    private TextView pay; // 显示支出
    private TextView income; // 显示收入
    private TextView loan; // 显示借贷
    private TextView count; // 显示总和


    private Calendar calstar; //时间选择器
    private DatePickerDialog datepstar; //时间选择器适配器

    //列表相关组件
    private ListView showSelectBills; //
    List<BillModel> data = new ArrayList<BillModel>();
    private MyAdapter myAdapter;
    int current = 0;


    private void getAllDatas(String date) { //e.g:2021-10
        data.clear();

        BillServer billServer = new BillServer(this);
        Cursor result = billServer.findByDate(date, USERID);

        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            //取出的数据顺序：id,xh,xm，与select查询语句中指定的列名一一对应
            BillModel bill = new BillModel();
            bill.setId(result.getInt(0));
            bill.setType(result.getInt(1));
            bill.setDesc(result.getString(2));
            bill.setMoney(result.getInt(3));
            bill.setDate(result.getString(4));
            data.add(bill);
        }
        result.close();

        //渲染支出 收入 借贷 总计
        renderActivity(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_bill);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        USERID = sharedPreferences.getString("user_id", null);

        init(); //界面初始化，默认当前月数据

        //点击时间查询
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String y = selectDateYear.getText().toString();
                String m = selectDateMonth.getText().toString();

                try {
                    Integer.parseInt(selectDateYear.getText().toString());
                    Integer.parseInt(selectDateMonth.getText().toString());
                    if (y.length() != 4 || m.length() != 2) {
                        throw new NumberFormatException();
                    } else if (!(Integer.parseInt(selectDateMonth.getText().toString()) <= 12)) {
                        throw new NumberFormatException();
                    }

                } catch (NumberFormatException err) {
                    Toast toast = Toast.makeText(getApplicationContext(), "请输入正确的年份与月份", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                BillServer billServer = new BillServer(Statistics_bill.this);
                Cursor result = billServer.findByDate(y + "-" + m, USERID);
                int i = result.getCount();
                //有数据指定查找的数据
                if (i > 0) {
                    data.clear();
                    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
                        //取出的数据顺序：id,xh,xm，与select查询语句中指定的列名一一对应
                        BillModel bill = new BillModel();
                        bill.setId(result.getInt(0));
                        bill.setType(result.getInt(1));
                        bill.setDesc(result.getString(2));
                        bill.setMoney(result.getInt(3));
                        bill.setDate(result.getString(4));
                        data.add(bill);
                    }
                    myAdapter.notifyDataSetChanged();
                    renderActivity(data);
                    result.close();
                } else {
                    data.clear();
                    renderActivity(data);
                    myAdapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "该月没有数据", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //listView 点击改变item选中
        showSelectBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current = i;
                myAdapter.notifyDataSetChanged();
            }
        });

        showSelectBills.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                current = i;
                myAdapter.notifyDataSetChanged();
                return false;
            }
        });

        //注册上下文菜单
        registerForContextMenu(showSelectBills);


    }

    //界面初始化
    private void init() {
        //获取所有控件
        selectBtn = (Button) findViewById(R.id.selectBtn);
        selectDateYear = (EditText) findViewById(R.id.inYear); //查询的年份
        selectDateMonth = (EditText) findViewById(R.id.inMonth); //查询的月份
        pay = (TextView) findViewById(R.id.pay); // 显示支出
        income = (TextView) findViewById(R.id.income); // 显示收入
        loan = (TextView) findViewById(R.id.loan); // 显示借贷
        count = (TextView) findViewById(R.id.count); // 显示总和
        showSelectBills = (ListView) findViewById(R.id.showSelectBills);


        //渲染当前年月
        selectDateYear.setText(nowYear + "");
        selectDateMonth.setText(nowMonth + "");

        //渲染列表
        getAllDatas(nowYear + "-" + nowMonth);
        myAdapter = new MyAdapter();
        showSelectBills.setAdapter(myAdapter);

    }

    //listView 适配器
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();//集合的大小
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            final Bill.MyAdapter.ViewHolder viewHolder = new Bill.MyAdapter.ViewHolder();
            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {
                //加载用户自定义的布局文件
//                convertView = LayoutInflater.from(Bill.this).inflate(R.layout.activity_bill_list_model, null);
                convertView = LayoutInflater.from(Statistics_bill.this).inflate(R.layout.activity_bill_list_model, null);
            }
            //当前数据显示黄色
            if (current == position) {
                convertView.setBackgroundColor(Color.YELLOW);
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            //初始化组件
            viewHolder.typeT = (TextView) convertView.findViewById(R.id.typeT);
            viewHolder.moneyT = (TextView) convertView.findViewById(R.id.moneyT);
            viewHolder.descT = (TextView) convertView.findViewById(R.id.descT);
            viewHolder.dateT = (TextView) convertView.findViewById(R.id.dateT);

            //监听单击事件

            //获取当前的数据
            BillModel bill = data.get(position);//从集合中取出当前的数据

            //对集合中的对象进行可读性处理
            String typeT;
            if (bill.getType() == 0) {
                typeT = "支";
            } else if (bill.getType() == 1) {
                typeT = "收";
            } else {
                typeT = "借";
            }

//            FormatDate formatDate = new FormatDate();
//            String pattern = "yyyy-MM-dd";
//            String dateS;
//            long dateL = Long.parseLong(bill.getDate());
//            long currenDate = formatDate.getCurTimeLong();
//            long difference = currenDate - dateL;
//            if (difference < 86400000L) {
//                dateS = "今天";
//            } else if (difference < 86400000 * 2) {
//                dateS = "昨天";
//            } else {
//                dateS = formatDate.getDateToString(dateL, pattern);
//            }
////            System.out.println("当前：" + currenDate);


            //给3个标签组件赋值
            viewHolder.typeT.setText(typeT);
            viewHolder.moneyT.setText(bill.getMoney() + "元");
            viewHolder.descT.setText(bill.getDesc());
            viewHolder.dateT.setText(bill.getDate());

            return convertView; //返回convertView
        }

        class ViewHolder {
            TextView typeT;
            TextView moneyT;
            TextView descT;
            TextView dateT;
        }
    }

    //上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("请选择你的操作：");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "删除");
        menu.add(Menu.NONE, Menu.FIRST + 2, 1, "修改");
    }

    //响应上下文菜单
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                //删除
                //先提示
                Dialog dialog = new AlertDialog.Builder(Statistics_bill.this)
                        .setTitle("删除学生信息")
                        .setMessage("确定删除：\"" + data.get(current).getDesc() + "\"的数据吗？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除数据
                                //账单id
                                int id = data.get(current).getId();
                                //调用删除方法，删除数据库里的内容
                                BillServer billServer = new BillServer(getApplicationContext());
                                billServer.del(id);
                                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                                //重新获取数据，刷新  性能低
                                //getAlldatas();
                                data.remove(current);
                                current--;
                                myAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
                break;
            case Menu.FIRST + 2:
                BillModel bill = data.get(current);
                //显示修改页面
                Bundle bundle = new Bundle();
                Intent intent = new Intent(Statistics_bill.this, Edit_bill.class);
                // 传递要修改的数据给修改页面

                bundle.putSerializable("billInfo", bill);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        return true;
    }

    //渲染支出 收入 借贷 总计
    public void renderActivity(List<BillModel> bills) {
        int payOut = 0;
        int incomeOut = 0;
        int loanOut = 0;
        int countOut = 0;


        for (int i = 0; i < bills.size(); i++) {
            BillModel bill = bills.get(i);
            int type = bill.getType();
            if (type == 0) {
                //是支出
                payOut += bill.getMoney();

            } else if (type == 1) { countOut -= bill.getMoney();

                //是收入
                incomeOut += bill.getMoney();
                countOut += bill.getMoney();
            } else {
                //是借贷
                loanOut += bill.getMoney();
                countOut += bill.getMoney();
            }
        }



        pay.setText("支出￥" + payOut);
        income.setText("收入￥" + incomeOut);
        loan.setText("借贷￥" + loanOut);
        count.setText("总计￥" + countOut);
    }

    //刷新主界面的数据，覆盖此方法
    @Override
    protected void onRestart() {
        super.onRestart();
        //重新刷新ViewList 数据
        reRender();
    }

    //重新刷新ViewList 数据
    public void reRender() {
        getAllDatas(nowYear + "-" + nowMonth);
        myAdapter.notifyDataSetChanged();
    }
}


