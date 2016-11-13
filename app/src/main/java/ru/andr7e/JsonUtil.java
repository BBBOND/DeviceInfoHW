package ru.andr7e;

import android.util.Pair;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 19.03.16.
 */
public class JsonUtil
{
    public static String toJson(ArrayList<Pair<String, String>> objList)
    {
        String json = "";

        try
        {
            JSONObject jsonObj = new JSONObject();

            for (Pair<String, String> obj : objList)
            {
                String key   = obj.first;
                String value = obj.second.replace("\n", " ");

                jsonObj.put(key, value);

                json = jsonObj.toString();
            }
        }
        catch (org.json.JSONException je)
        {
            System.err.println(je.getMessage());
        }

        return json;
    }

    public static String toJsonMtk(ArrayList<Pair<String, String>> objList, HashMap<String,String> hash)
    {
        String json = "";

        try
        {
            JSONObject jsonObj = new JSONObject();

            ArrayList<String> list = new ArrayList<String>();

            for (Pair<String, String> obj : objList)
            {
                String key   = obj.first;
                String value = obj.second.replace("\n", " ");

                if (hash.containsKey(key))
                {
                    String value2 = hash.get(key).replace("\n", " ");

                    if ( ! value.toUpperCase().contains( value2.toUpperCase() ))
                    {
                        value = value + " / " + value2;
                    }
                }

                list.add(key);

                jsonObj.put(key, value);
            }

            // add from config

            for (String key : hash.keySet())
            {
                if ( ! list.contains(key))
                {
                    String value = hash.get(key);

                    jsonObj.put(key, value);
                }
            }

            json = jsonObj.toString();
        }
        catch (org.json.JSONException je)
        {
            System.err.println(je.getMessage());
        }

        return json;
    }

}
