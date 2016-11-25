package ru.andr7e;

/**
 * Created by andrey on 25.11.16.
 */
public class CpuFreq
{
    private static final String CPU_FREQ_PATH = "/sys/devices/system/cpu/cpu%d/cpufreq/";

    public static String getFreqInfo (int cpuNum, String key)
    {
        String path = String.format(CPU_FREQ_PATH, cpuNum) + key;

        return IOUtil.getFileText(path);
    }

    public static String getClockSpeed ()
    {
        return getMinFreq() + " - " + getMaxFreq();
    }

    public static String getMinFreq ()
    {
        String freqStr = getFreqInfo(0, "cpuinfo_min_freq");

        Integer freq = Integer.parseInt(freqStr) / 1000;

        return freq.toString();
    }

    public static String getMaxFreq ()
    {
        String freqStr = getFreqInfo(0, "cpuinfo_max_freq");

        Integer freq = Integer.parseInt(freqStr) / 1000;

        return freq.toString();
    }

    public static String getGovernor ()
    {
        return getFreqInfo(0, "scaling_governor");
    }

}
