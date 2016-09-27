package com.example.andre;

import android.content.Context;
import android.content.res.Resources;

import com.example.andre.tabtest.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andrey on 27.09.16.
 */
public class DeviceComponents
{
    private HashMap<String,String> hash;

    public void load (Context context)
    {
        try {

            hash = new HashMap<String,String>();

            Resources res = context.getResources();

            InputStream in_s = res.openRawResource(R.raw.components);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);

            String text = new String(b);

            String[] lines = text.split(";");

            for (String line : lines)
            {
                line = line.trim();

                String[] values = line.split(":");

                if (values.length > 0)
                {
                    String key   = values[0].trim();
                    String value = values[1].trim();

                    System.out.println("KEY" + key);
                    System.out.println(value);

                    hash.put(key, value);
                }
            }

        } catch (Exception e) {
             e.printStackTrace();

        }
    }

    public String[] get (String key)
    {
        String mKey  = key.toUpperCase();

        String value = hash.get(mKey);

        String[] values = value.split("[\n ]");

        return values;
    }

    public void test ()
    {
        for (String key : hash.keySet())
        {
            System.out.println("KEY" + key);

            String value = hash.get(key);

            String[] prefixList = value.split("[\n ]");

            for (String prefix : prefixList)
            {
                System.out.println("P:" + prefix);
            }
        }
    }
}
