package com.ajohn.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.appmanageractivity.AppManagerActivity;
import com.ajohn.mobilesafe.activities.conmunicationactivities.ManageBlackNumberActivity;
import com.ajohn.mobilesafe.view.CustomwidgetTEEBBView;


public class HomeActivity extends Activity {

    /**
     * 列表信息
     */
    // 名称
    private static final String iconName[] = {"手机防盗", "通讯卫士", "应用管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    // 图片
    private static final int iconPicture[] = {R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
            R.drawable.settings};
    private static final java.lang.String TAG = "HomeActivity";

    // 主页面控件
    private GridView home_gv_list;

    // GridView下的控件
    private ImageView home_gv_list_item_iv;
    private TextView home_gv_list_item_tv;

    private SharedPreferences sp;
    private AlertDialog alertDialog;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        home_gv_list = (GridView) findViewById(R.id.home_gv_list);
        home_gv_list.setAdapter(new HomeAdapter());
        home_gv_list.setOnItemClickListener(new OnItemClickListener() {

            /**
             *
             * @param adapterView 该点击事件的GridView
             * @param view 点击的控件View
             * @param position 点击的该控件的id
             * @param x 列表中指定位置关联的行标识
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long x) {
                switch (position) {
                    case 0://"手机防盗",
                        enterSafe();
                        break;
                    case 1://"通讯卫士",
                        enterManageBlack();
                        break;
                    case 2://"应用管理",
                        enterApplicationManager();
                        break;
                    case 3://"进程管理",

                        break;
                    case 4://"流量统计",

                        break;
                    case 5://"手机杀毒",

                        break;
                    case 6://"缓存清理",

                        break;
                    case 7://"高级工具",
                        enterTools();
                        break;
                    case 8://"设置中心"

                        enterSetting();
                        break;

                    default:
                        break;
                }
            }
        });
    }


    //*********************************HomeActivity.Adapter*********************************
    private class HomeAdapter extends BaseAdapter {

        // 单个元素的样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.home_gv_list_items, null);
            home_gv_list_item_iv = (ImageView) view.findViewById(R.id.home_gv_list_item_iv);
            home_gv_list_item_tv = (TextView) view.findViewById(R.id.home_gv_list_item_tv);
            home_gv_list_item_iv.setImageResource(iconPicture[position]);
            home_gv_list_item_tv.setText(iconName[position]);
            return view;
        }

        // 获取到元素的个数
        @Override
        public int getCount() {

            return iconName.length;
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        /**
         * 获取列表中指定位置关联的行标识。
         */
        @Override
        public long getItemId(int position) {

            return position;
        }

    }

    //*********************************防盗功能*********************************

    private TextView safe_tv_dialog_title;
    private EditText safe_et_dialog_password;
    private EditText safe_et_dialog_passwordconfirm;
    private Button safe_btn_dialog_enter;
    private Button safe_btn_dialog_cancel;

    /**
     * 判断弹出哪个对话框
     */
    private void enterSafe() {
        if (isSetPassward()) {
            Log.e(TAG, "进入输入密码页面" + isSetPassward());
            showPasswordDialog();

        } else {
            Log.e(TAG, "进入设置密码页面" + isSetPassward());
            showSetPasswordDialog();

        }
    }

    /**
     * 设置密码对话框
     */
    private void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        CustomwidgetTEEBBView view = new CustomwidgetTEEBBView(this);

        /**
         * 实例化自定义控件的内部子控件,以便于操作他们的属性
         */
        safe_tv_dialog_title = view.getTv_title();
        safe_et_dialog_password = view.getEt_password();
        safe_et_dialog_passwordconfirm = view.getEt_passwordconfirm();
        safe_btn_dialog_enter = view.getBtn_enter();
        safe_btn_dialog_cancel = view.getBtn_cancel();

        safe_tv_dialog_title.setText("请设置密码");
        safe_et_dialog_password.setHint("请输入密码");
        safe_et_dialog_passwordconfirm.setHint("请再次输入密码");


        safe_btn_dialog_enter.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String password = safe_et_dialog_password.getText().toString().trim();
                String passwordconfirm = safe_et_dialog_passwordconfirm.getText().toString().trim();
                //2.判断密码是否为空
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordconfirm)) {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(passwordconfirm)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", password);
                    editor.commit();
                    alertDialog.dismiss();
                    showPasswordDialog();
                } else {
                    Toast.makeText(HomeActivity.this, "两次输入的密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
                    safe_et_dialog_password.setText("");
                    safe_et_dialog_passwordconfirm.setText("");
                    return;
                }

            }
        });

        safe_btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
    }

    /**
     * 输入密码对话框
     */
    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        CustomwidgetTEEBBView view = new CustomwidgetTEEBBView(this);

        /**
         * 实例化自定义控件的内部子控件,以便于操作他们的属性
         */
        safe_tv_dialog_title = view.getTv_title();
        safe_et_dialog_password = view.getEt_password();
        safe_et_dialog_passwordconfirm = view.getEt_passwordconfirm();
        safe_btn_dialog_enter = view.getBtn_enter();
        safe_btn_dialog_cancel = view.getBtn_cancel();

        safe_tv_dialog_title.setText("请输入密码");
        safe_et_dialog_password.setHint("请输入密码");
        safe_et_dialog_passwordconfirm.setVisibility(View.GONE);

        safe_btn_dialog_enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = safe_et_dialog_password.getText().toString().trim();
                if (sp.getString("password", null).equals(password)) {

                    if (!sp.getBoolean("configured", false)) {
                        Log.e(TAG, "进入了安全页面");
                        Intent intent = new Intent(HomeActivity.this, SafeActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e(TAG, "进入了安全页面");
                        Intent intent = new Intent(HomeActivity.this, SafeActivity.class);
                        startActivity(intent);
                    }
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(HomeActivity.this, "密码有误,请重新输入", Toast.LENGTH_SHORT).show();
                    safe_et_dialog_password.setText("");
                    return;
                }
            }
        });

        safe_btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();

    }

    /**
     * 检查是否已经设置了密码
     *
     * @return true:设置了密码
     */
    private boolean isSetPassward() {
        return sp.getString("password", null) != null;
    }

    //*********************************通讯卫士页面*********************************
    private void enterManageBlack() {
        intent = new Intent(HomeActivity.this, ManageBlackNumberActivity.class);
        startActivity(intent);
    }

    //*********************************应用管理页面*********************************
    private void enterApplicationManager() {
        intent = new Intent(HomeActivity.this, AppManagerActivity.class);
        startActivity(intent);
    }

    //*********************************设置页面*********************************
    private void enterSetting() {
        intent = new Intent(HomeActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    //*********************************高级工具页面*********************************
    private void enterTools() {
        intent = new Intent(HomeActivity.this, AdvanceToolsActivity.class);
        startActivity(intent);
    }
}


