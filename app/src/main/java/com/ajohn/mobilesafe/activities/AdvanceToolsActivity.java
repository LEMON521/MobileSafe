package com.ajohn.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.toolsactivities.ChangePositionToolsActivity;
import com.ajohn.mobilesafe.activities.toolsactivities.QueryAddressToolsActivity;
import com.ajohn.mobilesafe.view.CustomwedgitTTIView;

/**
 * Created by John on 2016/10/27.
 */

public class AdvanceToolsActivity extends Activity {
    private TextView tv_tools_queryaddress;
    private CustomwedgitTTIView cwv_tti_tools_setbackground;
    private CustomwedgitTTIView cwv_tti_tools_setposition;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_advancetools);
        sp = getSharedPreferences("config",MODE_PRIVATE);

        /**
         * 查询号码归属地
         */
        tv_tools_queryaddress = (TextView) findViewById(R.id.tv_tools_queryaddress);
        tv_tools_queryaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQueryActivity = new Intent(AdvanceToolsActivity.this,QueryAddressToolsActivity.class);
                AdvanceToolsActivity.this.startActivity(intentQueryActivity);
            }
        });

        /**
         * 设置来去电号码自定义土司的背景颜色
         */
        final String style[] = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
        cwv_tti_tools_setbackground = (CustomwedgitTTIView) findViewById(R.id.cwv_tti_tools_setbackground);
        cwv_tti_tools_setbackground.setTitle("设置归属地提示框风格");
        cwv_tti_tools_setbackground.setDescription("当前风格为--"+style[sp.getInt("addressStyle",0)]);
        cwv_tti_tools_setbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder singleDialogBuilder = new AlertDialog.Builder(AdvanceToolsActivity.this);
                singleDialogBuilder.setTitle("归属地提示框风格");
                //在这里我们将提示框的形式设置为单选按钮的形式
                //参数CharSequence[] items:将要显示的条目
                //int checkedItem:默认选中的条目
                //DialogInterface.OnClickListener listener:单击条目的事件
                singleDialogBuilder.setSingleChoiceItems(style, sp.getInt("addressStyle",0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cwv_tti_tools_setbackground.setDescription("当前风格为--"+style[which]);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("addressStyle",which);
                        editor.commit();
                        dialog.dismiss();
                    }
                });
                singleDialogBuilder.setNegativeButton("cancel",null);
                singleDialogBuilder.show();//一定不要忘记

            }
        });











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
