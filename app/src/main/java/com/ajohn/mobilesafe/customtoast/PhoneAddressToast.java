package com.ajohn.mobilesafe.customtoast;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajohn.mobilesafe.R;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;

/**
 * 这是一个自定义土司类
 * Created by John on 2016/11/2.
 */

public class PhoneAddressToast {


    private  WindowManager wm = null;
    private  LinearLayout ll_tools_customstoast;
    private  View view;
    private SharedPreferences sp;

    /**
     * 单例设计模式--懒汉式
     * TODO:在这里运用单例的原因:
     * 因为电话监听的状态改变的时候,onCallStateChanged的方法体就会重新从上到下执行一遍
     * 那么PhoneAddressToast myToast就会指向一个新的那么PhoneAddressToast,在我们清除新的PhoneAddressToast的时候
     * 原有的PhoneAddressToast还存在,那么我们就考虑单例设计模式,只让他存在一个对象,这样当电话状态改变的时候
     * 我们清除PhoneAddressToast myToast的显示就会清除掉
     */
    private PhoneAddressToast() {
    }

    private static PhoneAddressToast myToast = new PhoneAddressToast();

    public static PhoneAddressToast getMyToast() {
        return myToast;
    }


    public LinearLayout getLl_tools_customstoast() {
        return ll_tools_customstoast;
    }

    public void setLl_tools_customstoast(LinearLayout ll_tools_customstoast) {
        ll_tools_customstoast = ll_tools_customstoast;
    }

    public WindowManager getWm() {
        return wm;
    }

    public void setWm(WindowManager wm) {
        this.wm = wm;
    }

    /**
     * 字体红色,的自定义土司
     *
     * @param context 上下文
     * @param string  将要显示的内容
     */
    public void showToast(Context context, String string) {
        sp = context.getSharedPreferences("config",MODE_PRIVATE);
        wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
//        TextView tv_address = (TextView) new TextView(context);
//        tv_address.setText(string);
//        tv_address.setTextColor(Color.RED);

        //为自定义Toast设置样式
        view = new View(context);
        //将自定义的样式填充到View中
        view = View.inflate(context, R.layout.customs_toast_showaddress, null);

        //设置将要显示的地址信息
        TextView tv_address = (TextView) view.findViewById(R.id.tv_tools_customstoast_address);

        tv_address.setText(string);
        //实例化自定义土司中的LinearLayout,便于我们设置他的眼色背景
        ll_tools_customstoast = (LinearLayout) view.findViewById(R.id.ll_tools_customstoast);

//       String style[] = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};

        switch (sp.getInt("addressStyle",0)){
            case 0:
                ll_tools_customstoast.setBackgroundResource(R.drawable.call_locate_white);
                break;
            case 1:
                ll_tools_customstoast.setBackgroundResource(R.drawable.call_locate_orange);
                break;
            case 2:
                ll_tools_customstoast.setBackgroundResource(R.drawable.call_locate_blue);
                break;
            case 3:
                ll_tools_customstoast.setBackgroundResource(R.drawable.call_locate_gray);
                break;
            case 4:
                ll_tools_customstoast.setBackgroundResource(R.drawable.call_locate_green);
                break;
        }


        WindowManager.LayoutParams params = new WindowManager.LayoutParams();


        // TODO 以下代码是在Toast.java里面TN()方法中复制的
        //设置高
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置宽
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //1不可以或得焦点,2不可以触摸,3保持屏幕打开
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        //透明效果
        params.format = PixelFormat.TRANSLUCENT;
        //动画效果
        //params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        //类型
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        //标题
        //params.setTitle("Toast");

        wm.addView(view, params);
    }

    public void removeToast() {
        if (view != null) {
            wm.removeView(view);
            view = null;
        }
    }

}
