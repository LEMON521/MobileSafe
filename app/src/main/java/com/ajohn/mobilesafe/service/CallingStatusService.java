package com.ajohn.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ajohn.mobilesafe.customtoast.PhoneAddressToast;
import com.ajohn.mobilesafe.db.dao.NumberAddressQueryDao;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.telephony.PhoneStateListener.LISTEN_NONE;

/**
 * 来去电状态服务
 * Created by John on 2016/10/28.
 */

public class CallingStatusService extends Service {

    private static final String TAG = "CallingStatusService";
    //    private static final int IN = 1;
//    private static final int OUT = 1;
    //来去电状态,1为去电状态,2为来点状态
    private int callStatue = 1;
    //电话管理器
    private TelephonyManager tm;

    //来电状态监听
    private InComingCallListener inComingCallListener;
    //去电广播接受者
    private OutGoingTelephonyReceiver outGoingTelephonyReceiver;

    //录音管理
    private MediaRecorder mRecorder = null;

    private SharedPreferences sp;


    //写入缓冲区大小
    private int m_Record_Size;
    //音频录制对象
    private AudioRecord mAudioRecord;
    //音频写入存储字节数组
    private byte[] m_Input_Bytes;

    //播放缓冲区大小
    private int m_Play_Size;
    //音频播放对象
    private AudioTrack mAudioTrack;

    //主线程
    private Thread Record2Play_Thread;
    //标志变量
    private boolean IsRecording = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config",MODE_PRIVATE);

        //注册去电广播接收者
        outGoingTelephonyReceiver = new OutGoingTelephonyReceiver();
//        Intent outGoingIntent = new Intent(this,OutGoingTelephonyReceiver.class);
//        outGoingIntent.setAction("android.intent.action.NEW_OUTGOING_CALL");
        IntentFilter outGoingIntentFilter = new IntentFilter();
        outGoingIntentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(outGoingTelephonyReceiver, outGoingIntentFilter);

        //来电服务
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        inComingCallListener = new InComingCallListener();

        //监听电话状态广播
        tm.listen(inComingCallListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(inComingCallListener, LISTEN_NONE);
        inComingCallListener = null;
    }

    //TODO 来/去电电话监听
    //来电监听事件,当有来电时,我们查询号码并用土司的形式显示出去
    private class InComingCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            String number = incomingNumber;
//            Log.e(TAG, "最大状态:" + number);
//            Log.e(TAG, "最小状态:" + incomingNumber);
            PhoneAddressToast myToast = PhoneAddressToast.getMyToast();
            switch (state) {
                //空闲时
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e(TAG, "电话处于空闲状态");
                    if (mRecorder != null) {
                        mRecorder.stop();
                        mRecorder.reset();
                        mRecorder.release();
                        mRecorder = null;
                        Log.e(TAG, "关闭录音");
                    }
                    callStatue = 1;

                    //让自定义toast消失
                    if (myToast != null) {
                        myToast.removeToast();
                        myToast = null;
                    }
                    break;
                //响铃时
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = NumberAddressQueryDao.getAddess(number);
                    //Toast.makeText(getApplicationContext(), address, LENGTH_LONG).show();
                    myToast.showToast(CallingStatusService.this, address);
                    Log.e(TAG, "来电话了,号码为:" + number);
                    callStatue = 2;
                    //                   MyMediaRecorder(callStatue, number);
//                    myAudioRecord();
                    break;
                //接通时
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e(TAG, "接通了电话,号码为:" + number);
                    //mRecorder.start();//开始录音
                    Log.e(TAG, "开始录音");
                    break;


            }
        }
    }


    /**
     * 去电广播接收者
     * Created by John on 2016/10/24.
     */

    private class OutGoingTelephonyReceiver extends BroadcastReceiver {
        /**
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            Log.e(TAG, number);
            String address = NumberAddressQueryDao.getAddess(number);
            //Toast.makeText(context,address,Toast.LENGTH_LONG).show();
            //PhoneAddressToast.showToast(CallingStatusService.this,address);
            PhoneAddressToast myToast = PhoneAddressToast.getMyToast();
            myToast.showToast(CallingStatusService.this, address);
        }
    }


    /**
     * 电话录音
     *
     * @param inOrOut 来电为1,去电为2
     * @param number  来电/去电号码
     * @throws IOException
     */
    public void MyMediaRecorder(int inOrOut, String number) {
        Log.e(TAG, "状态:" + inOrOut);
        Log.e(TAG, "准备状态num:" + number);
        String fileName = "";
        File dirFile = null;
        //检查SD卡状态
        //可用,则将文件保存在SDcard上面,不可用,则保存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhoneRecording";
            dirFile = new File(fileName);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            Log.e(TAG, "sd卡:" + fileName);
        } else {
            fileName = Environment.getDataDirectory().getName() + "/PhoneRecording";
            dirFile = new File(fileName);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            Log.e(TAG, "内部卡:" + fileName);
        }
        String date = new SimpleDateFormat("yyyyMMddkkmmss").format(new Date()) + "_";
        switch (inOrOut) {
            case 2:
                fileName += "/来电" + date + number + ".3gp";
                readyMediaRecorder(fileName);
                break;
            case 1:
                fileName += "/去电" + date + number + ".3gp";
                readyMediaRecorder(fileName);
                break;

            default:
                break;
        }


        //mRecorder.start();
    }

    //录音的准备工作
    private void readyMediaRecorder(String fileName) {
        mRecorder = new MediaRecorder();

        //设置音频源
        try {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            //设置文件输出格式
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(fileName);
            //设置音频编码格式
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.getStackTrace();
            Log.e(TAG, "SORRY,the MediaRecorder is failed");
        }
        Log.e(TAG, "准备好了录音");
    }


    /*
    private void myAudioRecord(){
        //获取用于录制的最小写入缓存区大小
        m_Record_Size=AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,AudioFormat.ENCODING_PCM_16BIT);

        //获取音频录制对象
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, m_Record_Size);

        //获取用于播放的最小播放缓冲区大小
        m_Play_Size = AudioTrack.getMinBufferSize(8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        // 实例化播放音频对象
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, m_Play_Size,
                AudioTrack.MODE_STREAM);

        //初始化数组
        m_Input_Bytes = new byte[m_Record_Size];

        Record2Play_Thread=new Thread(new Record2Play());
        Record2Play_Thread.start();
    }
    class Record2Play implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                byte[] mBytes;
                // 开始录音
                mAudioRecord.startRecording();
                mAudioTrack.play();
                while (IsRecording)
                {
                    int BytesSize=mAudioRecord.read(m_Input_Bytes, 0, m_Record_Size);
                    mBytes=new byte[BytesSize];

                    mBytes=m_Input_Bytes.clone();
                    mAudioTrack.write(mBytes, 0, mBytes.length);
                }
                mAudioRecord.stop();
                mAudioTrack.stop();
            }
            catch(Exception e)
            {
                Toast.makeText(CallingStatusService.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
*/
}
