package com.example.andre;

import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;

import com.example.andre.androidshell.ShellExecuter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by andrey on 03.03.16.
 */
public class InfoList
{
    public static void addItem (ArrayList< Pair<String, String> > objList, String key, String value)
    {
        if ( ! value.isEmpty())
        {
            objList.add(new Pair<String, String>(key, value));
        }
    }

    public static ArrayList< Pair<String, String> > buildDriversInfoList()
    {
        ArrayList< Pair<String, String> > objList = new ArrayList< Pair<String, String> >();

        ArrayList<String> driverList = InfoUtils.getPlatformDeviceList();

        Collections.sort(driverList);

        int i = 0;
        for (String driver : driverList)
        {
            addItem(objList, driver, " ");
            i++;
        }

        return objList;
    }

    public static ArrayList< Pair<String, String> > buildDriversInfoListUpload()
    {
        ArrayList< Pair<String, String> > objList = new ArrayList< Pair<String, String> >();

        ArrayList<String> driverList = InfoUtils.getPlatformDeviceList();

        Collections.sort(driverList);

        addItem(objList, "Drivers", TextUtils.join("\n", driverList));

        return objList;
    }

    public static ArrayList< Pair<String, String> > buildInfoList(boolean isRootMode, boolean isAppendAddress)
    {
        ShellExecuter exec = new ShellExecuter();

        ArrayList< Pair<String, String> > objList = new ArrayList< Pair<String, String> >();

        String platform = InfoUtils.getPlatform();

        addItem(objList, InfoUtils.MANUFACTURER, InfoUtils.getManufacturer());
        addItem(objList, InfoUtils.MODEL, InfoUtils.getModel());
        addItem(objList, InfoUtils.BRAND, InfoUtils.getBrand());

        addItem(objList, InfoUtils.RESOLUTION, InfoUtils.getResolution());

        addItem(objList, InfoUtils.PLATFORM, platform);

        addItem(objList, "Android Version", InfoUtils.getAndroidVersion());
        addItem(objList, "API", InfoUtils.getAndroidAPI());

        addItem(objList, "Kernel", InfoUtils.getKernelVersion(exec));

        //
        HashMap<String,String> hash = InfoUtils.getDriversHash(exec, isAppendAddress);

        //
        String cmdline = "";

        if (isRootMode)
        {
            cmdline = InfoUtils.getCmdline(exec);

            if ( ! cmdline.isEmpty() && InfoUtils.isMtkPlatform(platform))
            {
                String lcmName = InfoUtils.getLcmName(cmdline);

                if ( ! lcmName.isEmpty())
                {
                    hash.put(InfoUtils.LCM, lcmName);
                }
            }
        }

        hash.put(InfoUtils.SOUND, InfoUtils.getSoundCard(exec));

        if (InfoUtils.isRkPlatform(platform))
            hash.put(InfoUtils.WIFI,  InfoUtils.getRkWiFi(exec));

        String[] keyList = {
                InfoUtils.PMIC,
                InfoUtils.RTC,
                InfoUtils.LCM,
                InfoUtils.TOUCHPANEL,
                InfoUtils.ACCELEROMETER,
                InfoUtils.ALSPS,
                InfoUtils.MAGNETOMETER,
                InfoUtils.GYROSCOPE,
                InfoUtils.CHARGER,
                InfoUtils.CAMERA,
                InfoUtils.CAMERA_BACK,
                InfoUtils.CAMERA_FRONT,
                InfoUtils.LENS,
                InfoUtils.WIFI,
                InfoUtils.SOUND,
                InfoUtils.MODEM,
                InfoUtils.UNKNOWN,
                //InfoUtils.DRIVERS
        };

        for (String key : keyList)
        {
            if (hash.containsKey(key))
            {
                String value = hash.get(key);

                addItem(objList, key, value);
            }
        }

        //
        addItem(objList, InfoUtils.RAM,   InfoUtils.getRamType(exec));
        addItem(objList, InfoUtils.FLASH, InfoUtils.getFlashName(exec));

        addItem(objList, "Baseband", Build.getRadioVersion());

        addItem(objList, "cmdline", cmdline);

        //addItem(objList, "Partitions", InfoUtils.getPartitions(platform, exec));

        return objList;
    }

    public static ArrayList< Pair<String, String> > buildProjectConfigList()
    {
        String[] keyList = {
                InfoUtils.PLATFORM,
                InfoUtils.RESOLUTION,
                InfoUtils.PMIC,
                InfoUtils.RTC,
                InfoUtils.LCM,
                InfoUtils.TOUCHPANEL,
                InfoUtils.ACCELEROMETER,
                InfoUtils.ALSPS,
                InfoUtils.MAGNETOMETER,
                InfoUtils.GYROSCOPE,
                InfoUtils.CHARGER,
                InfoUtils.CAMERA,
                InfoUtils.CAMERA_BACK,
                InfoUtils.CAMERA_FRONT,
                InfoUtils.LENS,
                InfoUtils.SOUND,
                InfoUtils.MODEM,
                InfoUtils.VERSION,
                InfoUtils.UNKNOWN
        };

        ArrayList< Pair<String, String> > objList = new ArrayList< Pair<String, String> >();

        HashMap<String,String>  hash = MtkUtil.getProjectDriversHash();

        for (String key : keyList)
        {
            if (hash.containsKey(key))
            {
                String value = hash.get(key);

                addItem(objList, key, value);
            }
        }

        return objList;
    }
}
