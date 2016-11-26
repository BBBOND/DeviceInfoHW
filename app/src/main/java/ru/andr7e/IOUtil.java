package ru.andr7e;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by andrey on 01.03.16.
 */
public class IOUtil
{
    private static final String TAG = IOUtil.class.getSimpleName();

    public static String getFileText (String fileName)
    {
        StringBuilder s = null;
        FileReader fileReader = null;
        BufferedReader buf = null;
        try {
            fileReader = new FileReader(fileName);
            buf = new BufferedReader(fileReader);

            String line;
            s = new StringBuilder();
            while ((line = buf.readLine()) != null) s.append(line).append("\n");
        } catch (FileNotFoundException ignored) {
            Log.e(TAG, "File does not exist " + fileName);
        } catch (IOException e) {
            Log.e(TAG, "Failed to read " + fileName);
        } finally {
            try {
                if (fileReader != null) fileReader.close();
                if (buf != null) buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return s == null ? "" : s.toString().trim();
    }

    public static boolean isFileExist(String fileName)
    {
        File file = new File(fileName);

        return file.exists();
    }

}
