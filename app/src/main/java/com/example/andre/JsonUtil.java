package com.example.andre;

import android.util.Pair;

import org.json.JSONObject;

import java.util.ArrayList;

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

}
