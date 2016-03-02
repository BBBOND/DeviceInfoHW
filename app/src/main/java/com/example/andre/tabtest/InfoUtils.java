package com.example.andre.tabtest;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by andre on 02.03.16.
 */
public class InfoUtils
{
    public static String getResolution ()
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int width  = metrics.widthPixels;
        int height = metrics.heightPixels;

        return String.format("%dx%d", height, width);
    }
}
