package ru.andr7e;

import android.os.Build;

import java.util.HashMap;

/**
 * Created by andre on 22.11.16.
 */
public class CpuInfo
{
    private static final String CPU_PROC_PATH = "/proc/cpuinfo";
    private static final String CPU_PRESENT_PATH = "/sys/devices/system/cpu/present";

    private String info_;
    private HashMap<String,String> hash_;
    private HashMap<String,String> partNumHash_;

    private String revision_;

    static private Integer cores_;

    CpuInfo()
    {
        cores_ = 0;

        info_ = IOUtil.getFileText(CPU_PROC_PATH);

        revision_ = "";

        hash_ = new HashMap<String,String>();
        partNumHash_ = new HashMap<String,String>();

        initInfoHash();

        initPartNumHash();
    }

    public static int getCoresCount()
    {
        if (cores_ == 0 && IOUtil.isFileExist(CPU_PRESENT_PATH))
        {
            try {

                //0-3
                String cores = IOUtil.getFileText(CPU_PRESENT_PATH);

                if (cores.contains("-")) {
                    cores_ = Integer.parseInt(cores.split("-")[1]);
                }

                cores_++;
            }
            catch (Exception ignored)
            {
            }
        }

        if (cores_ == 0)
        {
            cores_ = Runtime.getRuntime().availableProcessors();
        }

        return cores_;
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
                    //int core = Integer.parseInt(value);

                    //if (core > cores_) cores_ = core;
                }
                else
                {
                    hash_.put(key, value);
                }

                System.out.println(key + " " + value);
            }
        }

        //

        String variant  = hash_.get("CPU variant");
        String revision = hash_.get("CPU revision");

        int iVariant = Integer.decode(variant);

        StringBuilder sb = new StringBuilder();

        sb.append("r").append(iVariant).append("p").append(revision);

        revision_ = sb.toString();
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
        int count = getCoresCount();

        return String.valueOf(count);
    }

    String getRevision()
    {
        return revision_;
    }

    public static String getCpuABI ()
    {
        String cpu_abi;

        int currentapiVersion =  Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP)
        {
            String abis[] = Build.SUPPORTED_64_BIT_ABIS;
            if (abis.length > 0)
                cpu_abi = abis[0];
            else
                cpu_abi = Build.CPU_ABI;
        }
        else
        {
            cpu_abi = Build.CPU_ABI;
        }

        return cpu_abi;
    }

}
