package lib.android.timingbar.com.util;

import android.content.Context;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * DataHelper
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/10/9
 */

public class DataHelper {
    /**
     * 返回缓存文件夹
     */
    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState ().equals (Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir ();//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                file = new File (getCacheFilePath (context));
                makeDirs (file);
            }
            return file;
        } else {
            return context.getCacheDir ();
        }
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @param context
     * @return
     */
    public static String getCacheFilePath(Context context) {
        String packageName = context.getPackageName ();
        return "/mnt/sdcard/" + packageName;
    }

    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    public static File makeDirs(File file) {
        if (!file.exists ()) {
            file.mkdirs ();
        }
        return file;
    }
    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null)
            return;
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close ();
                }
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

}
