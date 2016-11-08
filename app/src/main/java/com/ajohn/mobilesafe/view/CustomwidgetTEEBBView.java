package com.ajohn.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajohn.mobilesafe.R;

/**
 * Created by John on 2016/10/18.
 */

public class CustomwidgetTEEBBView extends LinearLayout {

    private TextView tv_title;
    private EditText et_password;
    private EditText et_passwordconfirm;
    private Button btn_enter;
    private Button btn_cancel;

    public CustomwidgetTEEBBView(Context context) {
        super(context);
        initView(context);
    }

    public CustomwidgetTEEBBView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomwidgetTEEBBView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context)
    {
        View.inflate(context, R.layout.customwidget_teebb,CustomwidgetTEEBBView.this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_password= (EditText) findViewById(R.id.et_password);
        et_passwordconfirm= (EditText) findViewById(R.id.et_passwordconfirm);
        btn_enter= (Button) findViewById(R.id.btn_enter);
        btn_cancel= (Button) findViewById(R.id.btn_cancel);
    }

    //*****************************以下方法为设置子控件的属性方法*****************************

    /**
     * 设置标题内容
     * @param string 需要设置的信息
     */
    public void setTv_titleText(String string){
        tv_title.setText(string);
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public EditText getEt_password() {
        return et_password;
    }

    public EditText getEt_passwordconfirm() {
        return et_passwordconfirm;
    }

    public Button getBtn_enter() {
        return btn_enter;
    }

    public Button getBtn_cancel() {
        return btn_cancel;
    }

    public void setEt_password(EditText et_password) {
        this.et_password = et_password;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public void setEt_passwordconfirm(EditText et_passwordconfirm) {
        this.et_passwordconfirm = et_passwordconfirm;
    }

    public void setBtn_enter(Button btn_enter) {
        this.btn_enter = btn_enter;
    }

    public void setBtn_cancel(Button btn_cancel) {
        this.btn_cancel = btn_cancel;
    }
}
