package com.example.mybill.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.server.BillServer;
import com.example.mybill.util.FormatDate;
import com.example.mybill.util.Tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NewBill extends AppCompatActivity {
    private Button addBtn; //添加按钮
    private EditText inDesc; //描述输入框控件
    private EditText inAmount; //金额输入框控件
    private Spinner billTypesList; //账单类型控件
    private ArrayAdapter<CharSequence> adapterBillList;
    private List<CharSequence> billListDatas;
    private ImageView closeBtn;

    //选泽时间
    private TextView selectDate;
    private Calendar calstar; //时间选择器
    private DatePickerDialog datepstar; //时间选择器适配器

    Tools tools = new Tools();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);


        final BillServer billServer = new BillServer(this);

//        closeBtn = (ImageView) findViewById(R.id.closeBtn);
        inDesc = (EditText) findViewById(R.id.inDesc);
        inAmount = (EditText) findViewById(R.id.inAmount);
        addBtn = (Button) findViewById(R.id.addBtn);
        billTypesList = findViewById(R.id.typeList);
        //实例化集合
        billListDatas = new ArrayList<CharSequence>();
        //设置选项的内容
        billListDatas.add("支出");
        billListDatas.add("收入");
        billListDatas.add("借贷");
        //定义下拉列表项
        adapterBillList = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, billListDatas);
        billTypesList.setAdapter(adapterBillList);
        selectDate = (TextView) findViewById(R.id.selectDate);
        init();

        //点击添加按钮
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String desc = inDesc.getText().toString(); //记录添加的描述
                String amount = inAmount.getText().toString();
                if ("".equals(desc) || "".equals(amount)){
                    Toast toast = Toast.makeText(getApplicationContext(), "描述或金额未填写", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                try {
                    Integer.valueOf(inAmount.getText().toString());//捕获金额是否格式化异常
                }catch (NumberFormatException err){
                    Toast toast = Toast.makeText(getApplicationContext(), "金额请输入数字", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }


                int inType = tools.getTypeInt(billTypesList.getSelectedItem().toString());
                String date = selectDate.getText().toString();

                //判断输入的金额是数字


                billServer.addBill(inType, desc, Integer.valueOf(inAmount.getText().toString()), date, NewBill.this);
                Toast toast = Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });

//        //关闭添加界面
//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    public void init() {
        //添加时间默认今天
        FormatDate formatDate = new FormatDate();
        String pattern = "yyyy-MM-dd";
        selectDate.setText(formatDate.getDateToString(new Date().getTime(), pattern));


        calstar = Calendar.getInstance();
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(321);
                datepstar = new DatePickerDialog(NewBill.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        String monthS = month + 1 + "";
                        String day = "" + dayOfMonth;
                        if (month + 1 < 10) {
                            monthS = "0" + monthS;
                        }
                        if (dayOfMonth < 10) {
                            day = "0" + day;
                        }

                        String c = year + "-" + (monthS) + "-" + day;
                        selectDate.setText(c);
                    }
                }, calstar.get(Calendar.YEAR), calstar.get(Calendar.MONTH), calstar.get(Calendar.DAY_OF_MONTH));
                datepstar.getDatePicker().setMaxDate(new Date().getTime());
                datepstar.show();
            }
        });
    }



}
