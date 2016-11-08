package com.ajohn.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajohn.mobilesafe.R;

public class CustomwedgitTTIView extends RelativeLayout {

    private TextView cwv_tv_title;
    private TextView cwv_tv_description;

    /**
     * 初始化布局文件
     */
    private void initView(Context context) {
        // 把布局文件转换为View
        // 最后一个参数:添加谁进来,就是布局文件的父亲,也就是说这个布局文件挂载在该参数(类)上
        View.inflate(context, R.layout.customwidget_tti, CustomwedgitTTIView.this);


        cwv_tv_title = (TextView) findViewById(R.id.cwv_tv_title);
        cwv_tv_description = (TextView) findViewById(R.id.cwv_tv_description);

    }

    // 要设置样式的时候使用
    public CustomwedgitTTIView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    // 在布局文件实例化的时候使用
    public CustomwedgitTTIView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    // 代码实例化的时候使用
    public CustomwedgitTTIView(Context context) {
        super(context);
        initView(context);
    }


    /**
     * 设置组合控件的--标题--信息
     */
    public void setTitle(String text) {
        cwv_tv_title.setText(text);
    }

    /**
     * 设置组合控件的--状态--信息
     */
    public void setDescription(String text) {
        cwv_tv_description.setText(text);
    }


}


