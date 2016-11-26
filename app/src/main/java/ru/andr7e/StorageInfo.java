package ru.andr7e;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by andre on 26.11.16.
 */
public class StorageInfo
{
    private static final String B  = "B";
    private static final String KB = "kB";
    private static final String MB = "MB";
    private static final String GB = "GB";
    private static final String TB = "TB";

    private static final String[] units = new String[]{B, KB, MB, GB, TB};

    private static final int NEW_LONG_API = Build.VERSION_CODES.JELLY_BEAN_MR2;

    public static String getAvailableInternalSize()
    {
        File path = Environment.getDataDirectory();

        StatFs stat = new StatFs(path.getPath());
        long blockSize = getBlockSizeLong(stat);
        long availableBlocks = getAvailableBlocksLong(stat);

        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalSize()
    {
        File path = Environment.getDataDirectory();

        StatFs stat = new StatFs(path.getPath());
        long blockSize = getBlockSizeLong(stat);
        long totalBlocks = getBlockCountLong(stat);

        return formatSize(totalBlocks * blockSize);
    }

    public static String formatSize(long size)
    {
        if (size > 0)
        {
            int power = (int) (Math.log10(size) / Math.log10(1024));

            return new DecimalFormat("#.##").format(size / Math.pow(1024, power)) + " " + units[power];
        }

        return "0";
    }

    // wrapper
    public static long getBlockSizeLong(StatFs stat)
    {
        if (android.os.Build.VERSION.SDK_INT < NEW_LONG_API)
        {
            return stat.getBlockSize();
        }

        return stat.getBlockSizeLong();
    }

    public static long getBlockCountLong(StatFs stat)
    {
        if (android.os.Build.VERSION.SDK_INT < NEW_LONG_API)
        {
            return stat.getBlockCount();
        }

        return stat.getBlockCountLong();
    }

    public static long getAvailableBlocksLong(StatFs stat)
    {
        if (android.os.Build.VERSION.SDK_INT < NEW_LONG_API)
        {
            return stat.getAvailableBlocks();
        }

        return stat.getAvailableBlocksLong();
    }
}
