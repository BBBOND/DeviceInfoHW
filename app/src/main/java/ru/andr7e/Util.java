package ru.andr7e;

/**
 * Created by andrey on 25.11.16.
 */
public class Util
{
    public static String formatFreq (String freqStr)
    {
        Integer freq = Integer.parseInt(freqStr);

        while (freq > 10000)
        {
            freq /= 1000;
        }

        return freq.toString();
    }

}
