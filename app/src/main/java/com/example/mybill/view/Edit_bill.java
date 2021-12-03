package com.example.mybill.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.model.BillModel;
import com.example.mybill.server.BillServer;
import com.example.mybill.util.FormatDate;
import com.example.mybill.util.Tools;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Edit_bill extends AppCompatActivity {
    BillModel intentBill; //billList 传递过来的bill

    Tools tools = new Tools();
    FormatDate formatDate = new FormatDate();
    String pattern = "yyyy-MM-dd";

    private Button editBtn; //修改按钮
    private EditText inDesc; //描述输入框控件
    private EditText inAmount; //金额输入框控件
    private Spinner billTypesList; //账单类型控件
    private ArrayAdapter<CharSequence> adapterBillList;
    private List<CharSequence> billListDatas;
    //选泽时间
    private TextView selectDate;
    private Calendar calstar; //时间选择器
    private DatePickerDialog datepstar; //时间选择器适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);

        inDesc = (EditText) findViewById(R.id.inDesc);
        inAmount = (EditText) findViewById(R.id.inAmount);
        editBtn = (Button) findViewById(R.id.editBtn); //确定编辑按钮
        billTypesList = findViewById(R.id.typeList);
        selectDate = (TextView) findViewById(R.id.selectDate);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intentBill = (BillModel) bundle.getSerializable("billInfo");
        //界面数据初始化
        init();

        //确认修改
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editDesc = inDesc.getText().toString();
                String editMoney = inAmount.getText().toString();
                String editType = billTypesList.getSelectedItem().toString();
                String editDate = selectDate.getText().toString();
                BillModel bill = new BillModel();

                bill.setDesc(editDesc);
                bill.setMoney(Integer.parseInt(editMoney));
                bill.setType(tools.getTypeInt(editType));
                bill.setDate(editDate);
                bill.setId(intentBill.getId());

                BillServer billServer = new BillServer(getApplicationContext());
                billServer.update(bill);
                Toast toast = Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });


    }

    public void init() {
        //获取界面传递的信息

        inDesc.setText(intentBill.getDesc());
        inAmount.setText(intentBill.getMoney() + "");
        billTypesList.setSelection(intentBill.getType(), true);
        selectDate.setText(intentBill.getDate());

        calstar = Calendar.getInstance();
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(321);
                datepstar = new android.app.DatePickerDialog(Edit_bill.this, new DatePickerDialog.OnDateSetListener() {
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