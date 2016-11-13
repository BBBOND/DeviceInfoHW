package ru.andr7e;

import android.content.Context;
import android.content.res.Resources;

import ru.andr7e.deviceinfohw.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by andrey on 27.09.16.
 */
public class DeviceComponents
{
    private HashMap<String,String> hash;

    final static String fileName = "components";

    public void load (Context context)
    {
        try
        {
            hash = new HashMap<String,String>();

            String text = loadFromData(context);

            if (text.isEmpty())
            {
                text = loadFromResources(context);
            }

            String[] lines = text.split(";");

            for (String line : lines)
            {
                line = line.trim();

                if (line.startsWith("#")) continue;

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

    // file from data for update

    public static  String loadFromResources(Context context) throws IOException
    {
        Resources res = context.getResources();

        InputStream inputStream = res.openRawResource(R.raw.components);

        byte[] b = new byte[inputStream.available()];
        inputStream.read(b);

        String text = new String(b);

        return text;
    }

    public static  String loadFromData(Context context)
    {
        FileInputStream inputStream;

        try
        {
            inputStream = context.openFileInput(fileName);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            String text = new String(b);

            inputStream.close();

            return text;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void saveToData(String text, Context context)
    {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
