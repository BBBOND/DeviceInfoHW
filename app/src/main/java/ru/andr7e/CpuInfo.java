package ru.andr7e;

import java.util.HashMap;

/**
 * Created by andre on 22.11.16.
 */
public class CpuInfo
{
    private String info_;
    private HashMap<String,String> hash_;
    private HashMap<String,String> partNumHash_;

    private Integer cores_;

    CpuInfo()
    {
        cores_ = 0;

        info_ = readInfo();

        hash_ = new HashMap<String,String>();
        partNumHash_ = new HashMap<String,String>();

        initInfoHash();

        initPartNumHash();
    }

    void initInfoHash()
    {
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

    void addPartNum(String partNum, String name)
        {
            partNumHash_.put(partNum, name);
    }

    void initPartNumHash()
    {
        addPartNum("0xc05", "Cortex-A5");
        addPartNum("0xc07", "Cortex-A7");
        addPartNum("0xc08", "Cortex-A8");
        addPartNum("0xc09", "Cortex-A9");

        addPartNum("0xc0e", "Cortex-A17");
        addPartNum("0xc0f", "Cortex-A15");

        addPartNum("0xd03", "Cortex-A53");
        addPartNum("0xd07", "Cortex-A57");
        addPartNum("0xd08", "Cortex-A72");
    }

    String convertArmFamily(String code)
    {
        if (partNumHash_.containsKey(code))
        {
            return partNumHash_.get(code);
        }

        return code;
    }

    String getHardware()
    {
        return hash_.get("Hardware");
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
