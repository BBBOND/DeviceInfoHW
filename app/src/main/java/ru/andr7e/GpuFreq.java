package ru.andr7e;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by andrey on 25.11.16.
 */
public class GpuFreq
{
    private static final String TAG = GpuFreq.class.getSimpleName();

    private static final String ADRENO_GPU_FREQ_PATH = "/sys/class/kgsl/kgsl-3d0/";

    private static final String MAX_FREQ = "max_gpuclk";
    private static final String CUR_FREQ = "gpuclk";
    private static final String ALL_FREQ = "gpu_available_frequencies";

    private static String getFreqInfo (String key)
    {
        String path = ADRENO_GPU_FREQ_PATH + key;

        return IOUtil.getFileText(path);
    }

    // 300000 N 600000
    public static String getClockSpeed ()
    {
        String freqListStr = getFreqInfo(ALL_FREQ);

        if ( ! freqListStr.isEmpty())
        {
            Log.i(TAG, freqListStr);

            String clocks[] = freqListStr.split(" ");

            int count = clocks.length;
            if (count > 1)
            {
                Arrays.sort(clocks);

                return Util.formatClocks(clocks[0], clocks[count - 1]);
            }
        }

        return getMaxFreq();
    }

    public static String getCurFreq ()
    {
        String freqStr = getFreqInfo(CUR_FREQ);

        return Util.formatFreq(freqStr);
    }

    public static String getMaxFreq ()
    {
        String freqStr = getFreqInfo(MAX_FREQ);

        return Util.formatFreq(freqStr);
    }

}
