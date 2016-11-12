import android.test.AndroidTestCase;
import android.util.Log;

import com.ajohn.mobilesafe.bean.AppInfo;
import com.ajohn.mobilesafe.db.BlackNumberDBHelper;
import com.ajohn.mobilesafe.db.dao.BlackNumberDao;
import com.ajohn.mobilesafe.engine.AppManagerProvider;

import java.util.List;

/**
 * Created by lemon on 2016/11/9.
 */

public class Test extends AndroidTestCase{

    public void test() throws Exception {
        BlackNumberDBHelper helper;
        helper = new BlackNumberDBHelper(getContext());
        helper.getWritableDatabase();
    }
    public void testA() throws Exception {
        BlackNumberDao dao;
        dao = new BlackNumberDao(getContext());
        long result = dao.addBlackNum("13937260956","2");
        System.out.print(result);
    }
    public void testAddBlackers()
    {
        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
        for (int i = 0;i<100;i++) {
            long result = blackNumberDao.addBlackNum("139372609"+i,i%3+"");
            System.out.print(result);
        }
    }
    public  void testGetAppInfo(){
        AppManagerProvider provider = new AppManagerProvider();
        List<AppInfo> allAppInfos = provider.getAllAppInfos(getContext());
        for (AppInfo info: allAppInfos) {
            System.out.print(info.toString());
            Log.e("应用程序信息为：",info.toString());

        }
    }

}
