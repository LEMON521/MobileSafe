package com.ajohn.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.toolsactivities.ChangePositionToolsActivity;
import com.ajohn.mobilesafe.activities.toolsactivities.QueryAddressToolsActivity;
import com.ajohn.mobilesafe.utlis.BackupUtils;
import com.ajohn.mobilesafe.view.CustomwedgitTTIView;

import java.io.File;
import java.io.IOException;

/**
 * Created by John on 2016/10/27.
 */

public class AdvanceToolsActivity extends Activity {
    private TextView tv_tools_queryaddress;
    private TextView tv_tools_smsbackup;
    private CustomwedgitTTIView cwv_tti_tools_setbackground;
    private CustomwedgitTTIView cwv_tti_tools_setposition;
    private SharedPreferences sp;
    private ProgressDialog dialog;
    private BackupUtils.CountCallBack callback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancetools);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        /**
         * 查询号码归属地
         */
        tv_tools_queryaddress = (TextView) findViewById(R.id.tv_tools_queryaddress);
        tv_tools_queryaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQueryActivity = new Intent(AdvanceToolsActivity.this, QueryAddressToolsActivity.class);
                AdvanceToolsActivity.this.startActivity(intentQueryActivity);
            }
        });

        /**
         * 短信备份
         */
        tv_tools_smsbackup = (TextView) findViewById(R.id.tv_tools_smsbackup);
        //在这里，我们不再创建新的页面去做备份处理，我们将这个方法写在utils包里面
        tv_tools_smsbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先判断外部SD卡是否可用
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    final File backupFile = new File(path, "smsback.xml");
                    /**
                     * 读取数据库是一个耗时的过程，那么如果将该方法放入到主线程中
                     * 就有可能阻塞主线程
                     *
                     * 也有可能从服务器中读取数据，所以，我们应该将该方法写在子线程中
                     */
                    dialog = new ProgressDialog(AdvanceToolsActivity.this);
                    dialog.setMessage("正在备份短信...");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.show();//前往不能忘记show出来
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                //因为次此方法在子线程中，那么我们就不能将修改主线程的方法或者动作放在子线程
                                //要么使用Handler，要么使用带有Handler的方法或者事例

                                //在这里，我们使用ProgressDialog来实现
                                SystemClock.sleep(3000);
                                BackupUtils.smsBackup(AdvanceToolsActivity.this, backupFile.getAbsolutePath(), callback);

                                dialog.dismiss();
                                //Toast.makeText(AdvanceToolsActivity.this,"恭喜，短信备份成功",Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                //Toast.makeText(AdvanceToolsActivity.this,"很遗憾，短信备份失败",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }.start();

                } else {
                    Toast.makeText(AdvanceToolsActivity.this, "SD卡不可用暂时无法备份", Toast.LENGTH_SHORT).show();
                }


            }
        });
//        tv_tools_smsbackup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentBackupActivity = new Intent(AdvanceToolsActivity.this, SMSBackUpToolsActivity.class);
//                AdvanceToolsActivity.this.startActivity(intentBackupActivity);
//            }
//        });

        //***********************************备份短信相关***************************************
        callback = new BackupUtils.CountCallBack() {
            @Override
            public void getProgress(int progress) {
                dialog.setProgress(progress);
                Log.e("progress",progress+"");
            }

            @Override
            public void getMax(int max) {
                dialog.setMax(max);
                Log.e("max",max+"");
            }
        };
        //**********************************更改自定义Toast背景相关***************************************
        /**
         * 设置来去电号码自定义土司的背景颜色
         */
        final String style[] = {"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};
        cwv_tti_tools_setbackground = (CustomwedgitTTIView) findViewById(R.id.cwv_tti_tools_setbackground);
        cwv_tti_tools_setbackground.setTitle("设置归属地提示框风格");
        cwv_tti_tools_setbackground.setDescription("当前风格为--" + style[sp.getInt("addressStyle", 0)]);
        cwv_tti_tools_setbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder singleDialogBuilder = new AlertDialog.Builder(AdvanceToolsActivity.this);
                singleDialogBuilder.setTitle("归属地提示框风格");
                //在这里我们将提示框的形式设置为单选按钮的形式
                //参数CharSequence[] items:将要显示的条目
                //int checkedItem:默认选中的条目
                //DialogInterface.OnClickListener listener:单击条目的事件
                singleDialogBuilder.setSingleChoiceItems(style, sp.getInt("addressStyle", 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cwv_tti_tools_setbackground.setDescription("当前风格为--" + style[which]);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("addressStyle", which);
                        editor.commit();
                        dialog.dismiss();
                    }
                });
                singleDialogBuilder.setNegativeButton("cancel", null);
                singleDialogBuilder.show();//一定不要忘记

            }
        });


        //***********************************更改自定义Toast相关***************************************
        /**
         * 改变显示归属地自定义Toast的位置
         *
         * 还有待完善
         */
        cwv_tti_tools_setposition = (CustomwedgitTTIView) findViewById(R.id.cwv_tti_tools_setposition);
        cwv_tti_tools_setposition.setTitle("归属地显示框位置");
        cwv_tti_tools_setposition.setDescription("设置归属地显示框的显示位置");
        cwv_tti_tools_setposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvanceToolsActivity.this, ChangePositionToolsActivity.class);
                startActivity(intent);
                //finish();
            }
        });


    }


}
