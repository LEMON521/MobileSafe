package com.ajohn.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.service.CallingStatusService;
import com.ajohn.mobilesafe.view.CustomwedgitTTCView;

public class SettingActivity extends Activity {
	private CustomwedgitTTCView setting_cwv_update;
	private CustomwedgitTTCView setting_cwv_isshownumaddress;

	/**
	 * 设置信息数据的持久化
	 */
	private SharedPreferences sp;
	int count = 0;

	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		editor = sp.edit();

		// TODO 这个好玩,有什么方法可以一目了然
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		//自动升级
		setting_cwv_update = (CustomwedgitTTCView) findViewById(R.id.setting_cwv_update);
		setting_cwv_update.setTitle("自动升级");

		// 点击整个控件用来改变CheckBox的勾选状态
		setUpdateDiscriptionText();
		setting_cwv_update.setOnClickListener(new OnClickListener() {


            @Override
			public void onClick(View v) {

			//	Editor editor = sp.edit();//如果定义在这里,会增加读写负担,那么我就就可以考虑到在Activity销毁的时候将配置信息再全部提交
				// 获取到配置文件的update的值,再将该值设置给控件的勾选状态
				//setting_cwv_update.setChecked(sp.getBoolean("update", false));
				if (setting_cwv_update.isChecked()) {
					// 已勾选,就设置为非勾选
					setting_cwv_update.setChecked(false);
					setting_cwv_update.setDescription("自动升级状态--关闭");
					editor.putBoolean("update", false);

				} else {
					// 非勾选,就设置为已勾选
					setting_cwv_update.setChecked(true);
					setting_cwv_update.setDescription("自动升级状态--开启");
					editor.putBoolean("update", true);

				}
				
				//System.out.println("点击"+(count++)+"次");
			}
		});


        //显示来去电号码
        setting_cwv_isshownumaddress = (CustomwedgitTTCView) findViewById(R.id.setting_cwv_isshownumaddress);
        setting_cwv_isshownumaddress.setTitle("显示来去电号码归属地");
        setting_cwv_isshownumaddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, CallingStatusService.class);
                //	Editor editor = sp.edit();//如果定义在这里,会增加读写负担,那么我就就可以考虑到在Activity销毁的时候将配置信息再全部提交
                // 获取到配置文件的update的值,再将该值设置给控件的勾选状态
                //setting_cwv_update.setChecked(sp.getBoolean("update", false));
                if (setting_cwv_isshownumaddress.isChecked()) {
                    // 已勾选,就设置为非勾选
                    setting_cwv_isshownumaddress.setChecked(false);
                    setting_cwv_isshownumaddress.setDescription("显示来去电号码状态--关闭");
                    editor.putBoolean("showNumAddress", false);
                    SettingActivity.this.stopService(intent);
                } else {
                    // 非勾选,就设置为已勾选
                    setting_cwv_isshownumaddress.setChecked(true);
                    setting_cwv_isshownumaddress.setDescription("显示来去电号码状态--开启");
                    editor.putBoolean("showNumAddress", true);
                    SettingActivity.this.startService(intent);
                }

                //System.out.println("点击"+(count++)+"次");
            }
        });

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		editor.commit();
	}

	//设置自动更新的描述信息
	private void setUpdateDiscriptionText() {
		// 获取到配置文件的update的值,再将该值设置给控件的勾选状态
		setting_cwv_update.setChecked(sp.getBoolean("update", false));

		if (setting_cwv_update.isChecked()) {
			// 已勾选,就设置描述信息为--开启状态
			setting_cwv_update.setDescription("自动升级状态--开启");
		} else {
			// 非勾选,就设置描述信息为--关闭状态
			setting_cwv_update.setDescription("自动升级状态--关闭");
		}

	}

	//**************************************************************



}
