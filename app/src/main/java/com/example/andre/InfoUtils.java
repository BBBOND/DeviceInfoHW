package com.example.andre;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.example.andre.androidshell.ShellExecuter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by andrey on 24.02.16.
 */
public class InfoUtils
{
    public static final String UNKNOWN       = "Other";

    public static final String MANUFACTURER  = "Manufacturer";
    public static final String MODEL         = "Model";
    public static final String BRAND         = "Brand";

    public static final String PLATFORM      = "Platform";
    public static final String RESOLUTION    = "Resolution";
    public static final String LCM           = "LCM";
    public static final String TOUCHPANEL    = "Touchscreen";
    public static final String ACCELEROMETER = "Accelerometer";
    public static final String ALSPS         = "Als/ps";
    public static final String MAGNETOMETER  = "Magnetometer";
    public static final String GYROSCOPE     = "Gyroscope";
    public static final String CHARGER       = "Charger";
    public static final String LENS          = "Lens";
    public static final String CAMERA        = "Camera";
    public static final String CAMERA_BACK   = "Camera Back";
    public static final String CAMERA_FRONT  = "Camera Front";
    public static final String PMIC          = "PMIC";
    public static final String RTC           = "RTC";
    public static final String SOUND         = "Sound";
    public static final String WIFI          = "Wi-Fi";
    public static final String MODEM         = "Modem";
    public static final String RAM           = "RAM";
    public static final String FLASH         = "Flash";
    public static final String VERSION       = "Version";
    public static final String DRIVERS       = "Drivers";
    public static final String EXTRA         = "Extra";

    static ArrayList<String> mtkCameraListCached;
    static ArrayList<String> qcomCameraListCached;
    static ArrayList<String> qcomLensListCached;

    public static final String[] cameraPrefixList  = {"OV\\d+\\w*", "GC\\d+\\w*", "SP\\d+\\w*", "IMX\\d+\\w*", "HI\\d+\\w*", "GT2\\d+\\w*", "SIV\\d+\\w*", "S5K\\w*", "MT9\\w*", "T4K\\w*"};

    public static String getPlatform()
    {
        return Build.HARDWARE;
    }

    public static String getModel()
    {
        return Build.MODEL;
    }

    public static String getBrand()
    {
        return Build.BRAND;
    }

    public static String getManufacturer()
    {
        return Build.MANUFACTURER;
    }

    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer))
        {
            return model;
        }
        else
        {
            return manufacturer + " " + model;
        }
    }

    public static String getAndroidVersion()
    {
        return Build.VERSION.RELEASE;
    }

    public static String getAndroidAPI()
    {
        return Integer.toString(Build.VERSION.SDK_INT);
    }

    public static String getResolution ()
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int width  = metrics.widthPixels;
        int height = metrics.heightPixels;

        return String.format("%dx%d", height, width);
    }

    public static int getScreenWidth ()
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int width  = metrics.widthPixels;

        return width;
    }

    public static int getScreenHeight ()
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int height = metrics.heightPixels;

        return height;
    }

    // Shell

    public static String getKernelVersion (ShellExecuter se)
    {
        String command = "cat /proc/version";

        return se.execute(command);
    }

    public static String getFlashName (ShellExecuter se)
    {
        String command = "cat /sys/class/mmc_host/mmc0/mmc0:0001/name";

        return se.execute(command);
    }

    public static String getCpufreq (ShellExecuter se)
    {
        String command = "cat /proc/cpufreq/cpufreq_freq";

        return se.execute(command);
    }

    public static String getRamType (ShellExecuter se)
    {
        String command = "cat /sys/bus/platform/drivers/ddr_type/ddr_type";

        return se.execute(command);
    }

    public static String getSoundCard (ShellExecuter se)
    {
        String command = "cat /proc/asound/card0/id";

        return se.execute(command);
    }

    public static boolean isMtkPlatform(String platform)
    {
        return platform.toUpperCase().startsWith("MT");
    }

    public static boolean isRkPlatform(String platform)
    {
        return platform.toUpperCase().startsWith("RK");
    }

    public static boolean isQualcomPlatform(String platform)
    {
        return platform.toUpperCase().startsWith("QCOM") || platform.toUpperCase().startsWith("MSM");
    }

    public static String getRkPartitions (ShellExecuter se)
    {
        String command  = "cat /proc/mtd";

        return se.execute(command);
    }

    public static String getMtkPartitionsGPT (ShellExecuter se)
    {
        String command = "cat /proc/partinfo";

        return se.execute(command);
    }

    public static String getMtkPartitionsMBR (ShellExecuter se)
    {
        String command = "cat /proc/dumchar_info";

        return se.execute(command);
    }

    public static String getRkNandInfo (ShellExecuter se)
    {
        String command = "cat /proc/rknand";

        return se.execute(command);
    }

    public static String getRkWiFi (ShellExecuter se)
    {
        String command = "cat /sys/class/rkwifi/chip";

        return se.execute(command);
    }

    //

    public static String makeFullNameI2C (String name, String address, boolean enable)
    {
        if (enable)
        return name + " (i2c " + address + ")";

        return name;
    }

    public static boolean isActiveDeviceI2C(File dir)
    {
        for (File file : dir.listFiles())
        {
            if (file.isDirectory())
            {
                String name = file.getName();

                if (name.length() > 2)
                {
                    if (name.charAt(1) == '-')
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getActiveDeviceI2C(File dir)
    {
        for (File file : dir.listFiles())
        {
            if (file.isDirectory())
            {
                String name = file.getName();

                if (name.length() > 2)
                {
                    if (name.charAt(1) == '-')
                    {
                        return name;
                    }
                }
            }
        }
        return "";
    }

    public static boolean isQcomParentDirI2C(String name)
    {
        if (name.equals("CwMcuSensor"))
        {
            return true;
        }

        return false;
    }

    public static boolean isRockchipParentDirI2C(String name)
    {
        if (name.equals("sensors") || name.equals("lightsensor"))
        {
            return true;
        }

        return false;
    }

    public static ArrayList<String> getDeviceListDeepI2C(File dir, boolean isAppendAddress)
    {
        ArrayList<String> list = new ArrayList<String>();

        for (File file : dir.listFiles())
        {
            if (file.isDirectory())
            {
                String name = file.getName();

                String active = getActiveDeviceI2C(file);

                if ( ! active.isEmpty())
                {
                    if (isQcomParentDirI2C(name))
                    {
                        // /sys/bus/i2c/drivers/cwMcuSensor/0-003a/subsystem/drivers
                        //String subPath = dir.getAbsolutePath() + "/" + name + "/" + active + "/subsystem/drivers/";

                        //qcom
                        //  /sys/bus/i2c/drivers/cwMcuSensor/0-003a/driver/0-003a/subsystem/drivers
                        String subPath = dir.getAbsolutePath() + "/" + name + "/" + active + "/driver/" + active + "/subsystem/drivers/";

                        System.out.println(subPath);

                        File subDir = new File(subPath);

                        ArrayList<String> subList = getDeviceListQcomI2C(subDir);

                        for (String subName : subList)
                        {
                            System.out.println(subName);

                            if ( ! list.contains(subName))
                            {
                                if ( ! isQcomParentDirI2C(subName)) list.add(subName);
                            }
                        }
                    }
                    else if (isRockchipParentDirI2C(name))
                    {
                        //rk
                        //  /sys/bus/i2c/drivers/sensors/0-001d/name
                        String subPath = dir.getAbsolutePath() + "/" + name + "/" + active + "/name";

                        //System.out.println(subPath);

                        String subName = IOUtil.getFileText (subPath);

                        String fullName = makeFullNameI2C(subName, active, isAppendAddress);

                        if ( ! list.contains(fullName))
                        {
                            if ( ! isRockchipParentDirI2C(subName)) list.add(fullName);
                        }
                    }
                    else
                    {
                        //System.out.println(name);

                        String fullName = makeFullNameI2C(name, active, isAppendAddress);

                        if ( ! list.contains(fullName)) list.add(fullName);
                    }
                }
            }
        }
        return list;
    }

    public static ArrayList<String> getDeviceListI2C(File dir)
    {
        ArrayList<String> list = new ArrayList<String>();

        System.out.println(dir);

        File[] files = dir.listFiles();

        if (files == null) return list;

        for (File file : files)
        {
            if (file.isDirectory())
            {
                String name = file.getName();

                if (isActiveDeviceI2C(file))
                {
                    list.add(name);
                }
            }
        }
        return list;
    }

    public static ArrayList<String> getDeviceListQcomI2C(File dir)
    {
        ArrayList<String> list = new ArrayList<String>();

        System.out.println(dir);

        File[] files = dir.listFiles();

        if (files == null) return list;

        for (File file : files)
        {
            if (file.isDirectory())
            {
                String name = file.getName();

                //if (isActiveDeviceI2C(file))
                {
                    list.add(name);
                }
            }
        }
        return list;
    }

    public static String[] getDriversList(boolean isAppendAddress)
    {
        String path = "/sys/bus/i2c/drivers/";

        File dir = new File(path);

        ArrayList<String> list = getDeviceListDeepI2C(dir, isAppendAddress);

        /*
        // TEST
        list.add("S5K4HY");
        list.add("SP2560");
        list.add("SPDIF");
        list.add("OV8865");
        */

        return list.toArray(new String[0]);
    }

    public static ArrayList<String> getPlatformDeviceList()
    {
        ArrayList<String> list = new ArrayList<String>();

        String path = "/sys/bus/platform/drivers/";

        File dir = new File(path);

        System.out.println(dir);

        File[] files = dir.listFiles();

        if (files == null) return list;

        for (File file : files)
        {
            if (file.isDirectory())
            {
                String name = file.getName();

                list.add(name);
            }
        }

        // TEST
        //list.add("T4K85D7");
        //list.add("qcom,T4K85D8");

        return list;
    }

    //

    public static boolean isPrefixMatched (String[] prefixList, String value)
    {
        for (String prefix : prefixList)
        {
            if (value.startsWith(prefix) || value.contains("_" + prefix))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isCameraMatched (String[] prefixList, String value)
    {
        for (String prefix : prefixList)
        {
            if (isPatternMatched(prefix, value))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isPatternMatched(String pattern, String value)
    {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public static ArrayList<String> getRkLcdList(ArrayList<String> driverList)
    {
        /*
        rk616-mipi
        ssd2828
        rk3026-lvds
        rk610-lcd
        rk32-mipi
        */

        String[] lcdListPrefixList  = {"SSD2828"};
        String[] rkLcdListPrefixList  = {"-MIPI", "-LVDS", "-LCD"};

        ArrayList<String> rkLcdList  = new ArrayList<String>();

        for (String line : driverList)
        {
            String value = line.toUpperCase();

            int pos = value.indexOf("(");

            if (pos != -1)
            {
                value = value.substring(0, pos).trim();
            }

            if (isPrefixMatched(lcdListPrefixList, value) || (value.startsWith("RK") && isPrefixMatched(rkLcdListPrefixList, value)))
            {
                rkLcdList.add(line);
            }
        }

        return rkLcdList;
    }

    public static String cutSensorName(String name)
    {
        int index = name.indexOf(" ");

        if (index != -1) return name.substring(0, index);

        return name;
    }

    public static HashMap<String,String> getDriversHash(ShellExecuter se, boolean isAppendAddress, Context context)
    {
        String[] pmicPrefixList    = {"ACT", "WM83", "TPS", "MT63", "FAN53555", "NCP6", "MAX"};
        String[] touchPrefixList   = {"GT", "FT", "S3", "GSL", "EKTF", "MSG", "MTK-TPD", "-TS", "SYNAPTIC"};
        String[] chargerPrefixList = {"BQ", "FAN", "NCP", "CW2", "SMB1360"};
        String[] alspsPrefixList   = {"EPL", "APDS", "STK3", "LTR", "CM", "AP", "TMD", "RPR", "TMG", "AL", "US"};

        String[] accelerometerPrefixList  = {"LIS", "KX", "BMA", "MMA", "MXC", "MC", "LSM303D",  "LSM330D", "ADXL"}; // "KXT", "KXC"
        String[] magnetometerPrefixList   = {"AKM", "YAMAHA53", "BMM", "MMC3", "QMC", "LSM303M", "LSM330M", "S62"};
        String[] gyroscopeListPrefixList  = {"MPU"};

        String[] list = InfoUtils.getDriversList(isAppendAddress);

        HashMap<String,String> hm = new HashMap<String,String>();

        ArrayList<String> cameraList  = new ArrayList<String>();
        ArrayList<String> lensList    = new ArrayList<String>();
        ArrayList<String> touchList   = new ArrayList<String>();
        ArrayList<String> chargerList = new ArrayList<String>();
        ArrayList<String> alspsList   = new ArrayList<String>();
        ArrayList<String> pmicList    = new ArrayList<String>();

        ArrayList<String> accelerometerList = new ArrayList<String>();
        ArrayList<String> magnetometerList = new ArrayList<String>();
        ArrayList<String> gyroscopeList    = new ArrayList<String>();

        ArrayList<String> driverList = getPlatformDeviceList();

        ArrayList<String> otherList = new ArrayList<String>();

        for (String line : list)
        {
            String value = line.toUpperCase();

            int pos = value.indexOf("(");

            if (pos != -1)
            {
                value = value.substring(0, pos).trim();
            }

            if (value.endsWith("ACCEL"))
            {
                accelerometerList.add(line);
            }
            else if (value.endsWith("GYRO"))
            {
                gyroscopeList.add(line);
            }
            else if (value.endsWith("AF")) {
                lensList.add(line);
            }
            else if (isCameraMatched(cameraPrefixList, value)) {
                cameraList.add(line);
            }
            else if (isPrefixMatched(alspsPrefixList, value)) {
                alspsList.add(line);
            }
            else if (isPrefixMatched(accelerometerPrefixList, value)) {
                accelerometerList.add(line);
            }
            else if (isPrefixMatched(magnetometerPrefixList, value)) {
                magnetometerList.add(line);
            }
            else if (isPrefixMatched(gyroscopeListPrefixList, value)) {
                gyroscopeList.add(line);
            }
            else if (isPrefixMatched(pmicPrefixList, value) || value.contains("REGULATOR") )
            {
                pmicList.add(line);
            }
            else if (isPrefixMatched(chargerPrefixList, value) || value.contains("CHG") || value.contains("CHARGER"))
            {
                chargerList.add(line);
            }
            else if (isPrefixMatched(touchPrefixList, value) || value.endsWith("-TS") || value.endsWith("_TS") || value.endsWith("-TPD")) {
                touchList.add(line);
            } else if (value.startsWith("RTC"))
            {
                hm.put(InfoUtils.RTC, line);
            }
            else
            {
                otherList.add(line);
            }
        }

        String platform = getPlatform().toUpperCase();

        if (isMtkPlatform(platform))
        {
            if (mtkCameraListCached == null || mtkCameraListCached.isEmpty())
            {
                ArrayList<String> procCameraList = MtkUtil.getProcCameraList(se);

                if ( ! procCameraList.isEmpty())
                {
                    mtkCameraListCached = procCameraList;
                }
                else
                {
                    mtkCameraListCached = MtkUtil.getCameraList();
                }
            }

            cameraList.addAll(mtkCameraListCached);
        }
        // From platform drivers
        else if (isRkPlatform(platform))
        {
            ArrayList<String> rkLcdList  = getRkLcdList(driverList);

            if ( ! rkLcdList.isEmpty())  hm.put(InfoUtils.LCM,    TextUtils.join("\n", rkLcdList));
        }
        else if (isQualcomPlatform(platform))
        {
            if (qcomCameraListCached == null || qcomCameraListCached.isEmpty())
            {
                String ps = getProcessList(se);

                int pid = QcomUtil.getCameraPid(ps);

                String str = getQcomCamLib(se, pid);

                qcomCameraListCached  = QcomUtil.getCameraList(str);

                qcomLensListCached  = QcomUtil.getLensList(se, qcomCameraListCached);
            }

            cameraList.addAll(qcomCameraListCached);

            lensList.addAll(qcomLensListCached);
        }

        //
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        Sensor a = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (a != null) {
            if (accelerometerList.isEmpty()) accelerometerList.add(cutSensorName(a.getName()));
        }

        Sensor l = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (l != null) {
            if (alspsList.isEmpty()) alspsList.add(cutSensorName(l.getName()));
        }

        Sensor g = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (g != null) {
            if (gyroscopeList.isEmpty()) gyroscopeList.add(cutSensorName(g.getName()));
        }

        Sensor m = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (m != null) {
            if (magnetometerList.isEmpty()) magnetometerList.add(cutSensorName(m.getName()));
        }

        if ( ! cameraList.isEmpty())   hm.put(InfoUtils.CAMERA,     TextUtils.join("\n", cameraList));
        if ( ! lensList.isEmpty())     hm.put(InfoUtils.LENS,       TextUtils.join("\n", lensList));
        if ( ! touchList.isEmpty())    hm.put(InfoUtils.TOUCHPANEL, TextUtils.join("\n", touchList));
        if ( ! accelerometerList.isEmpty()) hm.put(InfoUtils.ACCELEROMETER,   TextUtils.join("\n", accelerometerList));
        if ( ! magnetometerList.isEmpty())  hm.put(InfoUtils.MAGNETOMETER,    TextUtils.join("\n", magnetometerList));
        if ( ! gyroscopeList.isEmpty())     hm.put(InfoUtils.GYROSCOPE,       TextUtils.join("\n", gyroscopeList));
        if ( ! alspsList.isEmpty())    hm.put(InfoUtils.ALSPS,      TextUtils.join("\n", alspsList));
        if ( ! pmicList.isEmpty())     hm.put(InfoUtils.PMIC,       TextUtils.join("\n", pmicList));
        if ( ! otherList.isEmpty())    hm.put(InfoUtils.UNKNOWN,    TextUtils.join("\n", otherList));

        if ( ! chargerList.isEmpty())  hm.put(InfoUtils.CHARGER,    TextUtils.join("\n", chargerList));
        else
            hm.put(InfoUtils.CHARGER, "USE PMIC");

        if ( ! driverList.isEmpty())    hm.put(InfoUtils.DRIVERS,    TextUtils.join("\n", driverList));

        return hm;
    }

    public static String getLcmName(String cmdline)
    {
        String[] list = cmdline.split(" ");

        for (String line : list) {
            if (line.startsWith("lcm"))
            {
                return line.substring(6);
            }
        }

        return "";
    }

    // su

    public static String getCmdline(ShellExecuter se)
    {
        String command = "cat /proc/cmdline";

        return se.suexecute(command);
    }

    public static String getQcomHwInfo(ShellExecuter se)
    {
        String command = "cat /proc/hwinfo";

        return se.execute(command);
    }

    public static String getQcomLcdName(String hwinfo)
    {
        String[] list = hwinfo.split("\n");

        for (String line : list) {
            if (line.startsWith("LCD"))
            {
                return line.substring(4).trim();
            }
        }

        return hwinfo;
    }

    public static String getProcessList(ShellExecuter se)
    {
        String command = "ps";

        return se.execute(command);
    }

    public static String getQcomCamLib(ShellExecuter se, int pid)
    {
        String command = "cat /proc/" + pid + "/maps";

        return se.suexecute(command);
    }
}
