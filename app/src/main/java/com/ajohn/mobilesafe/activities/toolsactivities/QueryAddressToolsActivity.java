package com.ajohn.mobilesafe.activities.toolsactivities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.db.dao.NumberAddressQueryDao;

/**
 * TODO 查询电话号码地区页面
 * Created by John on 2016/10/27.
 */

public class QueryAddressToolsActivity extends Activity {

    private static String TAG = "QueryAddressActivity";
    private EditText et_tools_phonenumber;
    private TextView tv_tools_queryshow;

    //震动管理器
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancetools_queryaddress);
        et_tools_phonenumber = (EditText) findViewById(R.id.et_tools_phonenumber);
        tv_tools_queryshow = (TextView) findViewById(R.id.tv_tools_queryshow);

        //获取到震动服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        //实时查询(在我们输入的时候,动态的进行查询)--所以我们要将事件添加到输入框改变的时候
        et_tools_phonenumber.addTextChangedListener(new TextWatcher() {

            //输入框改变之前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //输入框改变的时候
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e(TAG,"s:"+s+"\nstart:"+start+"\nbefore:"+before+"\ncount:"+count);
                queryAddress(s.toString().trim());
            }

            //输入框改变之后的时候
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void queryAddress(View view){
        String number = et_tools_phonenumber.getText().toString().trim();

        queryAddress(number);


    }

    private void queryAddress(String number) {
        if (TextUtils.isEmpty(number)) {
            //频率:     震动,停止,震动,停止,震动,停止
            long[] pattern = {500,500,1000,1000,2000,2000};
            vibrator.vibrate(pattern,-1);
            Toast.makeText(this,"NO~NO~NO~,哥,号码不能为空~~",Toast.LENGTH_SHORT).show();

            //TODO 为控件添加动画
            Animation etAnimation = AnimationUtils.loadAnimation(this,R.anim.shake_y);
            et_tools_phonenumber.startAnimation(etAnimation);
        }else {
            String address = NumberAddressQueryDao.getAddess(number);
            tv_tools_queryshow.setText("该号码的地区为:"+address);
        }
    }
}
