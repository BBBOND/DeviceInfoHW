package ru.andr7e;

import android.app.ActivityManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by andre on 26.11.16.
 */
public class MemInfo
{
    private static final String TAG = MemInfo.class.getSimpleName();

    private static final String MB = "MB";
    private static final String GB = "GB";

    private static final String RAM_TYPE_PATH = "/sys/bus/platform/drivers/ddr_type/ddr_type";
    private static final String MMC_INFO_PATH = "/sys/class/mmc_host/mmc0/mmc0:0001/name";

    public static long getTotalMB(ActivityManager.MemoryInfo mi)
    {
        //Log.i(TAG, mi.availMem / 1024 / 1024 + "/" + mi.totalMem / 1024 / 1024);

        long total = 0;

        int currentapiVersion =  Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN)
        {
            total = mi.totalMem / 1024 / 1024;
        }

        return total;
    }

    public static String getTotalSize(ActivityManager.MemoryInfo mi)
    {
        long total = getTotalMB(mi);

        if (total > 0)
        {
            return Util.formatUnit(String.valueOf(total), MB);
        }

        return "";
    }

    public static String getTotalCapacity(ActivityManager.MemoryInfo mi)
    {
        long total = getTotalMB(mi);

        if (total > 0)
        {
            if (total > 512)
            {
                long unit512 = ((total - 1)) / 512 + 1;

                String capacity = String.valueOf(unit512 / 2);

                if (unit512 % 2 != 0)
                {
                    capacity += ".5";
                }

                return Util.formatUnit(capacity, GB);
            }
            else
            {
                return Util.formatUnit("0.5", GB);
            }
        }

        return "";
    }

    // DDR2, LPDDR3
    public static String getRamType ()
    {
        String path = RAM_TYPE_PATH;

        return IOUtil.getFileText(path);
    }

    public static String getModuleInfo(ActivityManager.MemoryInfo mi)
    {
        String capacity = getTotalCapacity(mi);
        String type = getRamType();

        return capacity + " " + type;
    }

    public static String getFlashName ()
    {
        String path = MMC_INFO_PATH;

        return IOUtil.getFileText(path);
    }
}
