package com.ajohn.mobilesafe.activities.appmanageractivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.bean.AppInfo;
import com.ajohn.mobilesafe.engine.AppManagerProvider;

import java.util.ArrayList;

/**
 * Created by lemon on 2016/11/11.
 */
public class AppManagerActivity extends Activity {
    private TextView tv_appmanamger_ram;
    private TextView tv_appmanamger_sdcard;
    private TextView tv_appmanager_showtype;
    private ListView lv_appmanager_info;
    private LinearLayout ll_appmanager_loading;

    private ArrayList<AppInfo> allAppInfos;//所有程序的详细信息
    private ArrayList<AppInfo> systemInfos;//系统程序信息
    private ArrayList<AppInfo> userInfos;   //用户程序信息
    private ArrayList<AppInfo> newInfos;//临时存放信息，可能为系统信息，也可能为用户信息
    // private AppInfo appInfo;//单个应用程序信息


    private ShowAppInfoAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter = new ShowAppInfoAdapter();
                    lv_appmanager_info.setAdapter(adapter);
                    ll_appmanager_loading.setVisibility(View.GONE);
                    break;
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);

        tv_appmanamger_ram = (TextView) findViewById(R.id.tv_appmanamger_ram);
        tv_appmanamger_sdcard = (TextView) findViewById(R.id.tv_appmanamger_sdcard);
        tv_appmanager_showtype = (TextView) findViewById(R.id.tv_appmanager_showtype);
        lv_appmanager_info = (ListView) findViewById(R.id.lv_appmanager_info);
        ll_appmanager_loading = (LinearLayout) findViewById(R.id.ll_appmanager_loading);

        tv_appmanamger_ram.setText("内存可用：" + getSurplusSpace(Environment.getDataDirectory().getAbsolutePath()));
        tv_appmanamger_sdcard.setText("SD卡剩余空间：" + getSurplusSpace(Environment.getExternalStorageDirectory().getAbsolutePath()));

        //ListView的滚动事件
        //TODO 此事件的目的在于覆盖ListView中的显示程序数量的信息，也就是说，造成ListView显示程序数量条目的改变，显示信息不动的假象
        lv_appmanager_info.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            //滑动时候的事件
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //防止加载完毕没有数据的时候出现空指针异常
                if (userInfos==null||systemInfos==null)
                    return;


                if (firstVisibleItem > userInfos.size()) {
                    tv_appmanager_showtype.setText("系统程序：（" + systemInfos.size() + "）");
                } else {
                    tv_appmanager_showtype.setText("用户程序：（" + userInfos.size() + "）");
                }
            }
        });

        fillData();
    }

    /**
     * 加载应用程序数据
     */
    private void fillData() {
        ll_appmanager_loading.setVisibility(View.VISIBLE);
        //加载数据可能是一个耗时操作，所以我们应该将其放入子线程中
        new Thread() {
            @Override
            public void run() {
                super.run();
                //SystemClock.sleep(3000);
                allAppInfos = (ArrayList<AppInfo>) AppManagerProvider.getAllAppInfos(AppManagerActivity.this);
                systemInfos = new ArrayList<AppInfo>();
                userInfos = new ArrayList<AppInfo>();

                /**
                 * 在这里，我们将全部程序信息分开了，
                 * 系统应用和用户自己安装的应用
                 *
                 */
                for (AppInfo info : allAppInfos) {
//                    (info.isSystem())?(systemInfos.add(info):(userInfos.add(info));
                    if (info.isSystem()) {
                        systemInfos.add(info);
                    } else {
                        userInfos.add(info);
                    }
                }
                handler.sendEmptyMessage(0);

            }
        }.start();
    }

    /**
     * 此方法用来获取传入文件夹路径的剩余空间
     *
     * @param path 将要获取该文件夹的空间路径
     */
    private String getSurplusSpace(String path) {
        //检索一个文件系统的整体信息空间。这是一个包装器为Unix全体员工()。
        StatFs fs = new StatFs(path);

        //获取到每一块的储存空间
        long availableBlocks = fs.getAvailableBlocks();

        //获取一共多少块
        long blockSize = fs.getBlockSize();

        return Formatter.formatFileSize(this, availableBlocks * blockSize);
    }

    private class ShowAppInfoAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            //+2的原因，因为我们自己在List View上面添加了两条信息
            return systemInfos.size() + userInfos.size() + 1 + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder holder;
            int newPosition;
            //这里的判断null指的时该View是否被创建---
            //convertView instanceof RelativeLayout指的是：该View是否是跟convertView同一类型
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(AppManagerActivity.this, R.layout.appmanager_showappinfo_items, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_appmanager_showappitems_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_appmanager_showappitems_name);
                // holder.tv_appsize = (TextView) view.findViewById(R.id.tv_appmanager_showappitems_appsize);
                holder.tv_location = (TextView) view.findViewById(R.id.tv_appmanager_showappitems_location);
                holder.btn_uninstall = (Button) view.findViewById(R.id.btn_appmanager_showappitems_uninstall);
                view.setTag(holder);//将Holder与View绑定起来
            }
            //在第0个位置时，我们插入一个TextView，用来显示用户应用的条目数
            if (position == 0) {
                TextView tv = new TextView(AppManagerActivity.this);
                tv.setText("用户程序：（" + userInfos.size() + "）");
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                return tv;
            } else if (position == userInfos.size() + 1) {//在用户信息显示完毕时，插入一个TextView，用来显示系统信息条目数
                TextView tv = new TextView(AppManagerActivity.this);
                tv.setText("系统程序：（" + systemInfos.size() + "）");
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                return tv;
            } else if (position != 0 && position <= userInfos.size()) {//显示用户程序信息
                newPosition = position - 1;
                newInfos = userInfos;
            } else {
                newPosition = position - userInfos.size() - 2;
                newInfos = systemInfos;
            }


            //得到应用程序的信息
            AppInfo appinfo = newInfos.get(newPosition);
            holder.iv_icon.setImageDrawable(appinfo.getIcon());
            holder.tv_name.setText(appinfo.getName());
            // holder.tv_appsize.setText(appinfo.getPackName());
            holder.tv_location.setText(appinfo.isSystem() ? "手机内存" : "SD卡");
            //holder.btn_uninstall.setOnClickListener(null);

            return view;
        }
    }

    static class ViewHolder {
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_location;
        private TextView tv_appsize;
        private Button btn_uninstall;
    }
}
