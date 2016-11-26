package ru.andr7e;

/**
 * Created by andrey on 25.11.16.
 */
public class Util
{
    public static String formatFreq (String freqStr)
    {
        if ( ! freqStr.isEmpty()) {
            Integer freq = Integer.parseInt(freqStr);

            while (freq > 10000) {
                freq /= 1000;
            }

            return freq.toString();
        }

        return "";
    }

    public static String formatClocks (String min0, String max0)
    {
        String min = Util.formatFreq(min0);
        String max = Util.formatFreq(max0);

        return min + " - " + max;
    }

    public static String formatUnit (String value, String unit)
    {
        if ( ! value.isEmpty()) return value + " " + unit;

        return "";
    }

}
