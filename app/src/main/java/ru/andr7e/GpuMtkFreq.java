package ru.andr7e;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andre on 25.11.16.
 */
public class GpuMtkFreq
{
    private static final String TAG = GpuMtkFreq.class.getSimpleName();

    private static final String MTK_GPU_FREQ_PATH = "/proc/gpufreq/";
    private static final String OPP_DUMP = "gpufreq_opp_dump";
    private static final String VAR_DUMP = "gpufreq_var_dump";

    private static final String VAR_DUMP_CURFREQ_MT6589 = "g_freq_new_init_keep";
    private static final String VAR_DUMP_CURFREQ_MT6752 = "g_cur_gpu_freq";


    private static String curFreq_ = null;
    private static String clocks_ = null;

    public static String getClockSpeed ()
    {
        if (clocks_ == null)
        {
            clocks_ = getFreqFromDump();
        }

        if ( ! clocks_.isEmpty()) return clocks_;

        return getCurFreq();
    }

    public static String getCurFreq ()
    {
        if (curFreq_ == null)
        {
            return getCurFreqFromVarDump();
        }

        return curFreq_;
    }

    /*
    public static String getMaxFreq ()
    {
        String freqStr = getFreqInfo(ADRENO_GPU_FREQ_PATH, MAX_FREQ);

        return Util.formatFreq(freqStr);
    }
    */

    private static String getFreqInfo (String key)
    {
        String path = MTK_GPU_FREQ_PATH + key;

        return IOUtil.getFileText(path);
    }

    // mt6752
    //g_cur_gpu_freq = 312000, g_cur_gpu_volt = 93125
    //mt_gpufreq_dvfs_get_gpu_freq = 312000

    // mt6589
    //g_freq_new_init_keep = 238333, g_volt_new_init_keep = 0

    private static String getCurFreqFromVarDump ()
    {
        String freqInfoStr = getFreqInfo(VAR_DUMP);

        if ( ! freqInfoStr.isEmpty())
        {
            Log.i(TAG, freqInfoStr);

            String[] lines = freqInfoStr.split("\n");

            for (String line : lines)
            {
                String prefix = String.format("(%s|%s)", VAR_DUMP_CURFREQ_MT6589, VAR_DUMP_CURFREQ_MT6752);
                Pattern pattern = Pattern.compile(prefix + "\\s+=\\s+(\\d+)");

                Matcher m = pattern.matcher(line);

                if (m.find()) {
                    String freq = m.group(2);

                    Log.i(TAG, freq);

                    return Util.formatFreq(freq);
                }
            }
        }

        return "";
    }

    // mt6752
    // [0] freq = 494000, volt = 96250, idx = 0
    // [1] freq = 416000, volt = 93750, idx = 1
    // [2] freq = 312000, volt = 93125, idx = 2
    private static String getFreqFromDump ()
    {
        String freqInfoStr = getFreqInfo(OPP_DUMP);

        if ( ! freqInfoStr.isEmpty())
        {
            Log.i(TAG, freqInfoStr);

            String[] lines = freqInfoStr.split("\n");

            ArrayList<String> clocks = new ArrayList<String>();

            for (String line : lines) {
                Pattern pattern = Pattern.compile("(freq)\\s+=\\s+(\\d+)");

                Matcher m = pattern.matcher(line);

                if (m.find()) {
                    String freq = m.group(2);

                    Log.i(TAG, freq);

                    clocks.add(freq);
                }
            }

            int count = clocks.size();
            if (count > 1)
            {
                Collections.sort(clocks);

                return Util.formatClocks (clocks.get(0), clocks.get(count - 1));
            }
        }

        return "";
    }
}
