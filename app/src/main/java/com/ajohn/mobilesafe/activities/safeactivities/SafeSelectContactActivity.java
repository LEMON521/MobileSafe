package com.ajohn.mobilesafe.activities.safeactivities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ajohn.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by John on 2016/10/21.
 */

public class SafeSelectContactActivity extends Activity {

    private final static String TAG = "SafeSelectContactActivity";

    //联系人数据库的连接
    private final static String get_contact_id = "content://com.android.contacts/raw_contacts";
    private final static String get_contact_data = "content://com.android.contacts/data";
    //content://com.android.contacts/data

    private ListView lv_safe_selectcontact;

    /**
     * TODO 用于存放联系人
     */
    private List<Map<String, String>> contects;
    private Map<String, String> contect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcontact);
//        Log.e(TAG,"action="+this.getIntent().getAction());
//        Log.e(TAG,"action="+this.getIntent().getStringExtra("citvity"));

        lv_safe_selectcontact = (ListView) findViewById(R.id.lv_safe_selectcontact);

        //让该页面创建的时候获取到联系人
        contects = getAllContects();

        lv_safe_selectcontact.setAdapter(new SimpleAdapter(this, contects, R.layout.activity_selectcontact_item,
                new String[]{"name", "phonenum"}, new int[]{R.id.tv_safe_selectcontect_item_name,
                R.id.tv_safe_selectcontect_item_phonenum}));

        //选择联系人页面条目的点击事件
        lv_safe_selectcontact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String number = contects.get(position).get("phonenum");

                //回传数据,因为这是两个Activity之间的数据传递,所以要使用另一种意图构造方法
                Intent intent = new Intent();
                intent.putExtra("number", number);
                setResult(1, intent);
                finish();
            }
        });
    }

    /**
     * TODO 获取到全部的联系人
     *
     * @return List<Map<String, String>>
     */
    private List<Map<String, String>> getAllContects() {
        //初始化联系人容器
        contects = new ArrayList<Map<String, String>>();
        Uri contact_id_uri = Uri.parse(get_contact_id);
        Uri contact_data_uri = Uri.parse(get_contact_data);

        String id = "";
        String minitype = "";

        //获取到内容观察者
        ContentResolver contentResolver = getContentResolver();
        //查询content://contacts----->(也就是查询contacts表)
        Cursor cursorId = contentResolver.query(contact_id_uri, new String[]{"_id", "contact_id"}, null, null, "_id");
        while (cursorId.moveToNext()) {

            //获取到contect表的id
            id = cursorId.getString(cursorId.getColumnIndex("contact_id"));
            //Log.e(TAG,"id====="+id);

            contect = new HashMap<String, String>();
            // TODO 注意啦!!!一定要判定contact_id列的值是否为null!
            if (id != null) {
                //根据获取到的id查询联系人的详细信息---->在data表中查询
                //在这里我们获取到的信息分别是 id,minitype,data1,从获取到的这些信息中,我们需要根据minitype的值加以区分
                Cursor cursorInfo = contentResolver.query(contact_data_uri, new String[]{"data1", "mimetype"},
                        "raw_contact_id=?", new String[]{id}, null);
//                Cursor cursorInfo = contentResolver.query(contact_data_uri, null,
//                        "contact_id = ?", new String[]{id}, null);

                contect = new HashMap<String, String>();
                //将联系人存入到map集合中
                while (cursorInfo.moveToNext()) {


                    String name = "";
                    String phonenum = "";
                    String value = "";
                    minitype = cursorInfo.getString(cursorInfo.getColumnIndex("mimetype"));
                    value = cursorInfo.getString(cursorInfo.getColumnIndex("data1"));
                    if (minitype.equals("vnd.android.cursor.item/name")) {
                        name = value;
                        contect.put("name", name);
                        //Log.e(TAG, "NAME===" + name);
                    } else if (minitype.equals("vnd.android.cursor.item/phone_v2")) {
                        phonenum = value;
                        contect.put("phonenum", phonenum);
                        //Log.e(TAG, "NAME===" + phonenum);
                    }


                }
                if (contect.get("name") != null && contect.get("phonenum") != null) {
                    contects.add(contect);
                    contect = null;
                }
                cursorInfo.close();
            }


        }//cursorId.close();


        return contects;
    }
}
