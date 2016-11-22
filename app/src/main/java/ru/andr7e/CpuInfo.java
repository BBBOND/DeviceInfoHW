package ru.andr7e;

import java.util.HashMap;

/**
 * Created by andre on 22.11.16.
 */
public class CpuInfo
{
    private String info_;
    private HashMap<String,String> hash_;

    private Integer cores_;

    CpuInfo()
    {
        cores_ = 0;

        info_ = readInfo();

        hash_ = new HashMap<String,String>();

        String[] lines = info_.split("\n");

        for (String line : lines)
        {
            String[] strList = line.split(":");

            if (strList.length >= 2) {
                String key = strList[0].trim();
                String value = strList[1].trim();

                if (key.equals("processor"))
                {
                    int core = Integer.parseInt(value);

                    if (core > cores_) cores_ = core;
                }
                else
                {
                    hash_.put(key, value);
                }

                System.out.println(key + " " + value);
            }

        }
    }

    String getHardware()
    {
        return hash_.get("Hardware");
    }

    String convertArmFamily(String code)
    {
        if (code.equals("0xc07")) return "Cortex-A7";
        else if (code.equals("0xd03")) return "Cortex-A53";
        else if (code.equals("0xd07")) return "Cortex-A57";

        return code;
    }

    String getArmFamily()
    {
        String code = hash_.get("CPU part");

        return convertArmFamily(code);
    }

    String getCores()
    {
        Integer count = cores_ + 1;

        return count.toString();
    }

    public static String readInfo ()
    {
        return IOUtil.getFileText("/proc/cpuinfo");
    }

}
