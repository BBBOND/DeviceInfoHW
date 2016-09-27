package com.example.andre;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andrey on 27.09.16.
 */
public class DetectorComponents
{
    public static boolean isCameraMatched (String[] prefixList, String value)
    {
        return isDeviceMatched(prefixList, value);
    }

    public static boolean isDeviceMatched (String[] prefixList, String value)
    {
        for (String prefix : prefixList)
        {
            if (isPatternMatched(prefix + "\\w*", value))
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

    void test()
    {
        //String[] list = {"LIS3DE", "bmi150_acc",  "bm150_acc", "bma250", "super_accel", "lsm390d"};
        //String[] list = {"mtk-tpd", "Goodix-TS",  "amx_ts", "gt9xx", "S3203", "FT", "focaltech", "atmel_mxt_ts_640t"};
        //String[] list = {"rk616-mipi", "ssd2828",  "rk3026-lvds", "rk610-lcd", "rk32-mipi"};
        //String[] list = { "act8846", "super-REGULATOR"};
        //String[] list = { "bq27158", "smb1360",  "cw2015", "super-charger"};
        //String[] list = { "EPL2182", "stk3x1x"};
        //String[] list = { "akm899", "yas537", "yamaha530", "lsm330m"};
        //String[] list = {"mpu3050", "itg800", "super_gyro"};
    }
}
