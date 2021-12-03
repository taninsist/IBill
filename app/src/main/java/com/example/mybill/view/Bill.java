package com.example.mybill.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.MainActivity;
import com.example.mybill.R;
import com.example.mybill.model.BillModel;
import com.example.mybill.server.BackupTask;
import com.example.mybill.server.BillServer;
import com.example.mybill.server.MyDatabaseHelper;
import com.example.mybill.server.UserServer;
import com.example.mybill.util.FormatDate;

import java.io.Serializable;
import java.net.ResponseCache;
import java.util.ArrayList;
import java.util.List;

public class Bill extends AppCompatActivity {
    String USERID;

    private ImageView addBtn;
    private ImageView menuBtn;
    private ListView billListView;
    private List<BillModel> data = new ArrayList<BillModel>();
    private MyAdapter myAdapter;
    private int current = 0; //当前的记录序号

    FormatDate formatDate = new FormatDate();

    //读写手机权限
//    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    List<String> mPermissionList = new ArrayList<>();


    // private ImageView welcomeImg = null;
    private static final int PERMISSION_REQUEST = 1;
    // 检查权限

//    private void checkPermission() {
//        mPermissionList.clear();
//
//        //判断哪些权限未授予
//        for (int i = 0; i < permissions.length; i++) {
//            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
//                mPermissionList.add(permissions[i]);
//            }
//        }
//        /**
//         * 判断是否为空
//         */
//        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
//
//        } else {//请求权限方法
//            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
//            ActivityCompat.requestPermissions(Bill.this, permissions, PERMISSION_REQUEST);
//        }
//    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST:
//
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//                break;
//        }
//    }

    //获取所有数据
    private void getAllDatas() {
        data.clear();

        BillServer billServer = new BillServer(this);
        Cursor result = billServer.findAll(USERID);

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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        checkPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        addBtn = (ImageView) findViewById(R.id.addBtn);
        menuBtn = (ImageView) findViewById(R.id.menuBtn);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        USERID = sharedPreferences.getString("user_id", null);

        billListView = (ListView) findViewById(R.id.billList);

        getAllDatas();
        myAdapter = new MyAdapter();
        billListView.setAdapter(myAdapter);


        billListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current = i;
                myAdapter.notifyDataSetChanged();
            }
        });

        billListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                current = i;
                myAdapter.notifyDataSetChanged();
                return false;
            }
        });

        registerForContextMenu(billListView); //注册上下文菜单
//
//        //弹出菜单
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Bill.this, view);
                popupMenu.inflate(R.menu.menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        switch (itemId) { //弹出统计时间
                            case R.id.statistics:
//                                Intent intent1 = new Intent(Bill.this, Statistics_bill.class);
                                Intent intent1 = new Intent(Bill.this, Statistics_bill.class);
                                startActivity(intent1);
                                break;
                            case R.id.loginOut:
                                UserServer userServer = new UserServer(Bill.this);
                                userServer.loginOut(Bill.this);
                                Intent intent = new Intent(Bill.this, Login.class);
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.backupDb:
                                dataBackup(); //备份
                                break;
                            case R.id.recoverDb:
                                System.out.println(213);
                                dataRecover(); //恢复
                                reRender();
                                break;
                            case R.id.help:
                                Intent intent2 = new Intent(getApplicationContext(),Help.class);
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        //点击添加记录事件
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Bill.this, NewBill.class);
                startActivity(intent);
            }
        });
    }

    //刷新主界面的数据，覆盖此界面时执行
    @Override
    protected void onRestart() {
        super.onRestart();
        //重新刷新ViewList 数据
        reRender();
    }


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
            final ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {
                //加载用户自定义的布局文件
//                convertView = LayoutInflater.from(Bill.this).inflate(R.layout.activity_bill_list_model, null);
                convertView = LayoutInflater.from(Bill.this).inflate(R.layout.activity_bill_list_model, null);
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
//        }
        }
    }

    @Override //
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("请选择你的操作：");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "删除");
        menu.add(Menu.NONE, Menu.FIRST + 2, 1, "修改");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                //删除
                //先提示
                Dialog dialog = new AlertDialog.Builder(Bill.this)
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
                Intent intent = new Intent(Bill.this, Edit_bill.class);
                // 传递要修改的数据给修改页面

                bundle.putSerializable("billInfo", bill);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        return true;
    }


    //重新刷新ViewList 数据
    public void reRender() {
        getAllDatas();
        myAdapter.notifyDataSetChanged();
    }

    //数据恢复
    private void dataRecover() {
        // TODO Auto-generated method stub
        new BackupTask(this).execute("restroeDatabase");
    }

    //数据备份
    private void dataBackup() {
        // TODO Auto-generated method stub
        new BackupTask(this).execute("backupDatabase");
    }
}
