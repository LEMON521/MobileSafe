package com.ajohn.mobilesafe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ajohn.mobilesafe.activities.HomeActivity;
import com.ajohn.mobilesafe.utlis.StreamTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 启动页面
 * <p>
 * 1.显示Logo
 * <p>
 * 2.判断是否有网络
 * <p>
 * 3.是否升级
 * <p>
 * 4.判断是否合法
 * <p>
 * 5.校验是否有SDcard
 *
 * @author John
 */
@SuppressLint("HandlerLeak")
public class SplashActivity extends Activity {

    protected static final String TAG = "SplashActivity";// 标识,就是说打印的时候是隶属于SplashActivity类
    protected static final int ENTER_HOME = 1;
    protected static final int SHOW_UPDATE_DIALOG = 2;
    protected static final int URL_ERROR = 3;
    protected static final int NETWORK_ERROR = 4;
    protected static final int JSON_ERROR = 5;
    private TextView tv_splash_version;
    private TextView tv_splash_downloadinfo;

    private SharedPreferences sp;

    /**
     * 获取到服务器端的软件版本号
     */
    private String version;
    /**
     * 获取到的描述信息
     */
    private String description;
    /**
     * apk的下载地址
     */
    private String apkurl;

    /**
     * 向主线程发送消息的Handler
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 进入主界面
                case ENTER_HOME:
                    Log.e(TAG, "进入主页面" + msg.what);
                    enterHome();
                    break;
                // 弹出对话框升级
                case SHOW_UPDATE_DIALOG:
                    //
                    Log.e(TAG, "弹出升级对话框" + msg.what);
                    showUpdateDialog();
                    break;
                // URL异常
                case URL_ERROR:
                    Log.e(TAG, "URL异常" + msg.what);
                    Toast.makeText(SplashActivity.this, "URL异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                // 网络异常
                case NETWORK_ERROR:
                    Log.e(TAG, "网络异常" + msg.what);
                    // 在这里getApplicationContext()与SplashActivity.this等同
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                // JOSN解析异常
                case JSON_ERROR:
                    Log.e(TAG, "JOSN解析异常" + msg.what);
                    Toast.makeText(SplashActivity.this, "JOSN解析异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        copyDB();
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_downloadinfo = (TextView) findViewById(R.id.tv_splash_downloadinfo);
        // 设置版本名称,就要先动态的获取到版本名称
        tv_splash_version.setText(getVersion());

        if (checkAutoUpdate()) {
            // 检查升级
            checkVersion();
        } else {

            // handler.postAtTime(r, uptimeMillis);使Runnable
            // r添加到消息队列，将运行在一个特定的时间特定的uptimemillis。基地的时间是uptimemillis()。在深度睡眠中花费的时间将增加一个额外的延迟执行。运行将运行在线程handler附。

            // handler.postDelayed(r, delayMillis);使Runnable
            // r添加到消息队列，将运行在指定时间的流逝之后。运行将运行在线程handler附。基地的时间是uptimemillis()。在深度睡眠中花费的时间将增加一个额外的延迟执行。

            // 如果检查自动升级,那么就让他延迟进入主界面
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    enterHome();
                }
            }, 2000);

        }

    }

    /**
     * 弹出升级对话框
     */
    protected void showUpdateDialog() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("提示");
        builder.setMessage(description);

        // 强制升级----不推荐,或者升级漏洞的时候使用
        // builder.setCancelable(false);//取消按钮不可用
        // 针对弹出对话框的bug---用户点击返回键或者空白处时,对话框会消失,但是会停留在当前页面(缓冲页面)
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                enterHome();
            }
        });
        // 取消(下次再说)按钮的对应的事件
        builder.setNegativeButton("下次再说", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 令对话框消失
                dialog.dismiss();
                // 进入主页面
                enterHome();
            }
        });
        // 立刻升级(确定)按钮对应的事件
        builder.setPositiveButton("立刻升级", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 下载apk-------利用第三方API afinal.jar
                // 首先判定是否有SDcard,并且可用
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    FinalHttp finalHttp = new FinalHttp();
                    finalHttp.download(apkurl, Environment.getExternalStorageDirectory() + "/mobilesafe2.0.apk",
                            new AjaxCallBack<File>() {
                                // 应该注意对没有SDcard的手机的影响
                                // finalHttp.download(apkurl,
                                // Environment.getExternalStorageDirectory() +
                                // "/mobilesafe2.0.apk",
                                // new AjaxCallBack<File>() {

                                /**
                                 * 下载失败
                                 */
                                @Override
                                public void onFailure(Throwable t, int errorNo, String strMsg) {
                                    super.onFailure(t, errorNo, strMsg);
                                    t.printStackTrace();// 用于打印错误的日志
                                    Toast.makeText(getApplicationContext(), "下载失败了", Toast.LENGTH_SHORT).show();
                                }

                                /**
                                 * 下载过程
                                 */
                                @Override
                                public void onLoading(long count, long current) {
                                    super.onLoading(count, current);
                                    // 设置下载进度信息
                                    tv_splash_downloadinfo.setVisibility(View.VISIBLE);
                                    int progress = (int) (current * 100 / count);
                                    tv_splash_downloadinfo.setText("下载进度:" + progress + "%");
                                }

                                /**
                                 * 下载成功
                                 */
                                @Override
                                public void onSuccess(File file) {
                                    super.onSuccess(file);
                                    Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
                                    // 下载成功之后就安装akp
                                    installAPK(file);
                                }

                                /**
                                 * 安装apk
                                 *
                                 * 在这里我们引用系统默认的安装意图
                                 */
                                private void installAPK(File file) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    intent.setDataAndType(Uri.fromFile(file),
                                            "application/vnd.android.package-archive");
                                    // 一定不要忘记开启意图
                                    startActivity(intent);

                                }

                            });
                } else {
                    Toast.makeText(getApplicationContext(), "SD卡不可用", Toast.LENGTH_SHORT).show();
                }
                // 替换apk
                // enterHome();
            }
        });

        // 注意,这行代码不能忘记,如果不show的话对话框是不会出来的
        builder.show();

    }

    /**
     * 获取该app的版本号
     *
     * @return 获取该app的版本号
     */
    private String getVersion() {

        // 获取到包管理器
        PackageManager pm = getPackageManager();

        try {
            // 获取在系统上安装的应用程序包的整体信息。
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            // 获取版本号
            return "版本:" + info.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 检查是否有新版本,如果有就升级
     */
    private void checkVersion() {
        // 检查是否有新版本,那么就应该去服务器端检查,那么就应该联网
        // 在4.0以后,联网就需要在子线程中
        new Thread() {
            public void run() {
                // 将Handler的消息定义出来

                Message message = Message.obtain();
                long startTime = System.currentTimeMillis();// 获取到当前时间
                try {
                    // 请求网络,得到最新版本
                    URL url = new URL(getString(R.string.serverurl));// 在这里,我们将网络地址存放进了values文件夹下的config文件中
                    // 注意,应该用HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");// 设置请求方法
                    con.setConnectTimeout(4000);// 设置请求超时时间
                    //con.setReadTimeout(2000);
                    int code = con.getResponseCode();// 获取到返回码
                    System.out.println("网络访问返回码:" + code);
                    if (200 == code) {
                        // 请求成功
                        // 获取到返回的流,并将他们转换为String类型
                        String result = StreamTools.readFromStream(con.getInputStream());
                        Log.e(TAG, "result=======" + result);// 将结果打印出来

                        // 解析json,在这里,我们的服务器上面存放的是JSON形式的文件,所以我们要对返回的信息进行JSON解析
                        JSONObject obj = new JSONObject(result);
                        version = obj.getString("version");
                        description = obj.getString("description");
                        apkurl = obj.getString("apkurl");
                        Log.e(TAG, "version=======" + version);// 将结果打印出来
                        Log.e(TAG, "description=======" + description);// 将结果打印出来
                        Log.e(TAG, "apkurl=======" + apkurl);// 将结果打印出来

                        // 判断是否更新
                        if (getVersion().equals(version)) {
                            // 没有新版本,直接进入主界面
                            message.what = ENTER_HOME;// 设置消息
                        } else {
                            // 弹出对话框
                            message.what = SHOW_UPDATE_DIALOG;
                        }

                    } else {
                        message.what = NETWORK_ERROR;
                    }

                } catch (MalformedURLException e) {
                    // URL错误
                    e.printStackTrace();
                    Log.e(TAG, "URL错误");
                    message.what = URL_ERROR;
                } catch (IOException e) {
                    // 网络异常
                    e.printStackTrace();
                    Log.e(TAG, "网络异常");
                    message.what = NETWORK_ERROR;
                } catch (JSONException e) {
                    // JSON解析异常
                    e.printStackTrace();
                    Log.e(TAG, "JSON解析异常");
                    message.what = JSON_ERROR;

                } finally {
                    // 向handler发送消息
                    long endTime = System.currentTimeMillis();
                    long dTime = endTime - startTime;
                    if (dTime < 2000) {
                        SystemClock.sleep(2000 - dTime);
                    }
                    handler.sendMessage(message);
                }
            }

            ;
        }.start();

    }

    /**
     * 获取配置信息-----是否自动升级
     *
     * @return
     */
    private boolean checkAutoUpdate() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        return sp.getBoolean("update", true);
    }

    private void enterHome() {

        // 注册显式意图
        Intent intent = new Intent(this, HomeActivity.class);
        // 启动主页面
        startActivity(intent);
        // 销毁当前页面(也就是说当点击返回的时候不再退回当前页面)
        finish();

    }

    ;

    /**
     * > 界面失去了焦点，按钮就不可以相应点击事件
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * >activity被最小化了，并没有销毁，如果下次再去打开这个activity
     * >重新用户界面可见
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        enterHome();
    }

    /**
     * > 界面获取到了焦点，按钮可以相应点击事件
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 把assets目录下的address.db拷贝到/data/data/com.itheima.mobilesafe/files/address.db
     */
    private void copyDB() {
        File file = new File(getFilesDir(), "address.db");
        if (file.exists() && file.length() > 0) {
            Log.e(TAG,"数据库已经存在，不需要拷贝了...");
        } else {
            try {

                Log.e(TAG,"拷贝目录"+getAssets());
                InputStream is = SplashActivity.this.getResources().getAssets().open("address.db");

                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                Log.e(TAG,"拷贝成功");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG,"拷贝失败");
            }
        }
    }
}
