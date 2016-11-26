package ru.andr7e;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

import ru.andr7e.androidshell.ShellExecuter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.hardware.SensorManager;


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
    public static final String ALSPS2         = "Alsps";
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

    public static final String BUILD         = "Build";
    public static final String CPU           = "CPU";
    public static final String CORES         = "Cores";
    public static final String REVISION      = "Revision";
    public static final String FAMILY        = "Family";
    public static final String ABI           = "ABI";
    public static final String CLOCK_SPEED   = "Clock speed";
    public static final String GOVERNOR      = "Governor";
    public static final String GPU           = "GPU";
    public static final String GPU_CLOCK     = "GPU clock";
    public static final String MEMORY        = "Memory";
    public static final String DISK          = "Disk";
    public static final String PATCH         = "Patch";

    static ArrayList<String> mtkCameraListCached;
    static ArrayList<String> qcomCameraListCached;
    static ArrayList<String> qcomLensListCached;

    public static String[] cameraPrefixList;
    public static String[] accelerometerPrefixList;
    public static String[] alspsPrefixList;
    public static String[] magnetometerPrefixList;
    public static String[] gyroscopeListPrefixList;
    public static String[] touchPrefixList;
    public static String[] lcdPrefixList;
    public static String[] pmicPrefixList;
    public static String[] chargerPrefixList;

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

    public static String getKernelVersion ()
    {
        String path = "/proc/version";

        return IOUtil.getFileText(path);
    }

    public static String getSoundCard ()
    {
        String path = "/proc/asound/card0/id";

        return IOUtil.getFileText(path);
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

    public static String getRkPartitions ()
    {
        String path  = "/proc/mtd";

        return IOUtil.getFileText(path);
    }

    public static String getMtkPartitionsGPT ()
    {
        String path = "/proc/partinfo";

        return IOUtil.getFileText(path);
    }

    public static String getMtkPartitionsMBR ()
    {
        String path = "/proc/dumchar_info";

        return IOUtil.getFileText(path);
    }

    public static String getRkNandInfo ()
    {
        String path = "/proc/rknand";

        return IOUtil.getFileText(path);
    }

    public static String getRkWiFi ()
    {
        String path = "/sys/class/rkwifi/chip";

        return IOUtil.getFileText(path);
    }

    public static String getPatchLevel ()
    {
        String patch = "";

        int currentapiVersion = Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.M) {
            try {
                patch = Build.VERSION.SECURITY_PATCH;

                if (!"".equals(patch)) {
                    try {
                        SimpleDateFormat template = new SimpleDateFormat("yyyy-MM-dd");
                        Date patchDate = template.parse(patch);

                        String format = "dd.MM.yyyy";

                        patch = DateFormat.format(format, patchDate).toString();
                    } catch (ParseException e) {
                        // broken parse; fall through and use the raw string
                    }
                }
            } catch (java.lang.NoSuchFieldError e) {

            }
        }

        return patch;
    }
    //

    public static String makeFullNameI2C (String name, String address, boolean enable)
    {
        if (enable) return name + " (i2c " + address + ")";

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

    public static String cutDevName(String name, String sep)
    {
        int index = name.indexOf(sep);

        if (index != -1) return name.substring(0, index).trim();

        return name;
    }

    public static String cutDevName(String name)
    {
        return cutDevName(name, "(");
    }

    public static String cutSensorName(String name)
    {
        return cutDevName(name, " ");
    }

    public static ArrayList<String> getRkLcdList(ArrayList<String> driverList)
    {
        ArrayList<String> rkLcdList  = new ArrayList<String>();

        for (String line : driverList)
        {
            String value = line.toLowerCase();

            value = cutDevName(value);

            if (DetectorComponents.isDeviceMatched(lcdPrefixList, value))
            {
                rkLcdList.add(line);
            }
        }

        return rkLcdList;
    }

    public static HashMap<String,String> getDriversHash(ShellExecuter se, boolean isAppendAddress, Context context)
    {
        DeviceComponents components = new DeviceComponents();
        components.load(context);
        //components.test();

        if (cameraPrefixList == null)
            cameraPrefixList = components.get(CAMERA);

        if (accelerometerPrefixList == null)
            accelerometerPrefixList = components.get(ACCELEROMETER);

        if (alspsPrefixList == null)
            alspsPrefixList = components.get(ALSPS2);

        if (magnetometerPrefixList == null)
            magnetometerPrefixList = components.get(MAGNETOMETER);

        if (gyroscopeListPrefixList == null)
            gyroscopeListPrefixList = components.get(GYROSCOPE);

        if (touchPrefixList == null)
            touchPrefixList = components.get(TOUCHPANEL);

        if (pmicPrefixList == null)
            pmicPrefixList = components.get(PMIC);

        if (chargerPrefixList == null)
            chargerPrefixList = components.get(CHARGER);

        if (lcdPrefixList == null)
            lcdPrefixList = components.get(LCM);

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
            String value = line.toLowerCase();

            value = cutDevName(value);

            if (value.endsWith("af")) {
                lensList.add(line);
            }
            else if (DetectorComponents.isCameraMatched(cameraPrefixList, value.toLowerCase())) {
                cameraList.add(line);
            }
            else if (DetectorComponents.isDeviceMatched(alspsPrefixList, value)) {
                alspsList.add(line);
            }
            else if (DetectorComponents.isDeviceMatched(accelerometerPrefixList, value)) {
                accelerometerList.add(line);
            }
            else if (DetectorComponents.isDeviceMatched(magnetometerPrefixList, value)) {
                magnetometerList.add(line);
            }
            else if (DetectorComponents.isDeviceMatched(gyroscopeListPrefixList, value)) {
                gyroscopeList.add(line);
            }
            else if (DetectorComponents.isDeviceMatched(pmicPrefixList, value) )
            {
                pmicList.add(line);
            }
            else if (DetectorComponents.isDeviceMatched(chargerPrefixList, value))
            {
                chargerList.add(line);
            }
            else if (DetectorComponents.isDeviceMatched(touchPrefixList, value)) {
                touchList.add(line);
            } else if (value.startsWith("rtc"))
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
                ArrayList<String> procCameraList = MtkUtil.getProcCameraList();

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
