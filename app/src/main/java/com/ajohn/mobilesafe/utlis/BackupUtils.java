package com.ajohn.mobilesafe.utlis;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lemon on 2016/11/10.
 */


public class BackupUtils {
    /**
     * 短信备份
     *
     * @param context 上下文
     * @param path    路径
     * @return
     */
    public static void smsBackup(Context context, String path,CountCallBack back) throws IOException {

        //设置一个XML序列化器，用来保存信息（也就是说将信息以XML到形式保存起来）
        Xml.newSerializer();//序列化器
        Xml.newPullParser();//解析器

        XmlSerializer xmlSerializer = Xml.newSerializer();
        File file = new File(path);

        FileOutputStream os = new FileOutputStream(file);
        xmlSerializer.setOutput(os, "utf-8");//设置序列器到输入流，以及编码形式-->utf—8

        xmlSerializer.startDocument("utf-8",true);//设置序列化器到头部描述信息，是否是一个独立到文件

        //设置开头    <smss>
        xmlSerializer.startTag(null,"smss");//设置开始的Tag，对应的，还有结束Tag

        //获取到广播接收者
        ContentResolver resolver = context.getContentResolver();

        //把所有当短信备份，包括收／发／草稿箱
        Uri smsuri = Uri.parse("content://sms");
        Cursor smsCursor = resolver.query(smsuri, new String[]{"address", "date", "type", "body"}, null, null, null);

        // TODO 将获取到的总条目数传递给接口，再由接口传递给调用接口的方法体
        back.getMax(smsCursor.getCount());
        int progress = 0;
        while (smsCursor.moveToNext()) {

            xmlSerializer.startTag(null,"sms");//开始sms节点

            xmlSerializer.startTag(null,"address");//开始address节点
            String address = smsCursor.getString(smsCursor.getColumnIndex("address"));
            xmlSerializer.text(address);//设置address节点的内容
            xmlSerializer.endTag(null,"address");//address的结束节点

            xmlSerializer.startTag(null,"date");//date的开始节点
            String date = smsCursor.getString(smsCursor.getColumnIndex("date"));
            xmlSerializer.text(date);//date节点的内容
            xmlSerializer.endTag(null,"date");//date的结束节点

            xmlSerializer.startTag(null,"type");//type的开始节点
            String type = smsCursor.getString(smsCursor.getColumnIndex("type"));
            xmlSerializer.text(type);//设置type的内容
            xmlSerializer.endTag(null,"type");//type的结束节点

            xmlSerializer.startTag(null,"body");//body的开始节点
            String body = smsCursor.getString(smsCursor.getColumnIndex("body"));
            xmlSerializer.text(body);//body的内容
            xmlSerializer.endTag(null,"body");//body的结束节点


            xmlSerializer.endTag(null,"sms");//sms的结束节点
            SystemClock.sleep(1000);
            //储存完一条，我们就将该条目所在的位置数传递给调用他的方法
            progress++;
            back.getProgress(progress);
        }
        smsCursor.close();//不要忘记关闭Cursor
        xmlSerializer.endTag(null,"smss");//设置描述文件的结束Tag  </smss>
        xmlSerializer.endDocument();//设置描述文件到结束标志


    }

    /**
     * 回调的接口
     *
     * 此接口用来动态的获取到当前读取到到短信条目数
     * 以及短信的最大条目数
     */
    public interface CountCallBack{
        /**
         * 该方法可以获取到的当前的条目数
         * @param progress 当前的条目数
         */
        public void getProgress(int progress);

        /**
         * 该方法可以获取到最大到条目数
         * @param max 最大条目数
         */
        public void getMax(int max);
    }
}
