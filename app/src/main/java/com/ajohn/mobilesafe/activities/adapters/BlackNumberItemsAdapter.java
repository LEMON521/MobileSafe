package com.ajohn.mobilesafe.activities.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.bean.BlackNumberBean;
import com.ajohn.mobilesafe.db.dao.BlackNumberDao;
import com.ajohn.mobilesafe.db.dao.NumberAddressQueryDao;

import java.util.ArrayList;



/**
 * 黑名单ListView的填充器
 * Created by John on 2016/11/7.
 */

public class BlackNumberItemsAdapter extends BaseAdapter {
    private Context context;
    private BlackNumberBean blacker;
    private ArrayList<BlackNumberBean> blackers;
    public BlackNumberItemsAdapter(Context context){
        this.context = context;
        BlackNumberDao blackNumberDao = new BlackNumberDao(context);
        blackers = (ArrayList<BlackNumberBean>) blackNumberDao.queryAllBlackNum();
    }
    /**
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView!=null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.black_lv_blacknum_items,null);
            viewHolder.tv_lv_black_name = (TextView) view.findViewById(R.id.tv_lv_black_name);
            viewHolder.tv_lv_black_number = (TextView) view.findViewById(R.id.tv_lv_black_number);
            viewHolder.tv_lv_black_mode = (TextView) view.findViewById(R.id.tv_lv_black_mode);
            viewHolder.tv_lv_black_address = (TextView) view.findViewById(R.id.tv_lv_black_address);
            view.setTag(viewHolder);
        }




        blacker = blackers.get(position);
        String number = blacker.getNumber();
        String mode = blacker.getMode();
        viewHolder.tv_lv_black_name.setText("未知号码");
        viewHolder.tv_lv_black_number.setText(number);
        viewHolder.tv_lv_black_mode.setText(getPositionMode(mode));
        viewHolder.tv_lv_black_address.setText(getAddress(number));

        return view;
    }
    /**
     */
    @Override
    public int getCount() {
        return blackers.size();
    }

    /**
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }


    private String getName(String number){
        return "未知号码";
    }
    private String getAddress(String number){
        return NumberAddressQueryDao.getAddess(number);
    }

    /**
     * 获取拦截模式
     * @param mode 0,1,2
     * @return  0-电话,1-短信,2-电话+短信
     */
    private String getPositionMode(String mode){
        String result = "";
        switch (Integer.valueOf(mode)){
            case 0:
                result = "拦截模式:电话";
                break;
            case 1:
                result = "拦截模式:短信";
                break;
            case 2:
                result = "拦截模式:电话+短信";
                break;
        }
        return result;
    }

    static class ViewHolder{
        TextView tv_lv_black_name ;
        TextView tv_lv_black_number ;
        TextView tv_lv_black_mode ;
        TextView tv_lv_black_address ;
    }
}
