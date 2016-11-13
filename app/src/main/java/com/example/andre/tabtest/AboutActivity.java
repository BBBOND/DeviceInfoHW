package com.example.andre.tabtest;

import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.example.andre.DeviceComponents;
import com.example.andre.GuiUtil;
import com.example.andre.InfoList;
import com.example.andre.InfoUtils;
import com.example.andre.JsonHttp;
import com.example.andre.JsonUtil;
import com.example.andre.MtkUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AboutActivity extends AppCompatActivity {

    //final String DEVICES_URL = "http://192.168.0.101/devices/";
    final String DEVICES_URL    = "http://deviceinfo.net23.net/devices/";
    final String COMPONENTS_URL = "https://raw.githubusercontent.com/andr7e/DeviceComponents/master/components";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        fillAbout();
    }

    public void fillAbout()
    {
        TextView appNameTextView = (TextView)findViewById(R.id.appNameTextView);
        appNameTextView.setText(R.string.app_name);

        TextView versionTextView = (TextView)findViewById(R.id.versionTextView);
        versionTextView.setText(R.string.about_version);

        TextView authorTextView = (TextView)findViewById(R.id.authorTextView);
        authorTextView.setText(R.string.about_author);

        TextView updateTextView = (TextView)findViewById(R.id.updateTextView);
        updateTextView.setText(R.string.about_update_text);

        TextView uploadTextView = (TextView)findViewById(R.id.uploadTextView);
        uploadTextView.setText(R.string.about_upload_text);

        TextView uploadLinkTextView = (TextView)findViewById(R.id.uploadLinkTextView);
        uploadLinkTextView.setText(Html.fromHtml("<a href=\'" + DEVICES_URL + "\'> Devices Web Page </a >"));
        uploadLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        TextView aboutBottomTextView = (TextView)findViewById(R.id.aboutBottomTextView);
        aboutBottomTextView.setText(R.string.about_bottom);
    }

    public void onPressUploadButton(View view)
    {
        sendReport();
    }

    public void onPressUpdateButton(View view)
    {
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //

            System.out.println("update");

            JsonHttp jsonHttp = new JsonHttp();

            String componentsData = jsonHttp.get(COMPONENTS_URL);

            System.out.println(componentsData);

            //

            System.out.println("save");

            DeviceComponents.saveToData(componentsData, getBaseContext());

            GuiUtil.showDialog(AboutActivity.this, "Update", "Updated successfully. Restart program.", "Ok");
        }
        catch (Exception e)
        {
            String text = e.getMessage();

            System.err.println(text);

            GuiUtil.showDialog(AboutActivity.this, "Error", text, "Close");
        }
    }

    public void sendReport()
    {
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //

            String json = "";

            String platform = InfoUtils.getPlatform();

            ArrayList<Pair<String, String>> platformObjList = InfoList.buildDriversInfoListUpload();

            Context context = getBaseContext();

            if (InfoUtils.isMtkPlatform(platform))
            {
                boolean useRoot = true;

                HashMap<String,String> hash = MtkUtil.getProjectDriversHash();

                if (hash.isEmpty())
                {
                    ArrayList<Pair<String, String>> objList = InfoList.buildInfoList(useRoot, true, context);
                    objList.addAll(platformObjList);
                    json = JsonUtil.toJson(objList);
                }
                else
                {
                    ArrayList<Pair<String, String>> objList = InfoList.buildInfoList(useRoot, true, context);
                    objList.addAll(platformObjList);
                    json = JsonUtil.toJsonMtk(objList, hash);
                }
            }
            else
            {
                ArrayList<Pair<String, String>> objList = InfoList.buildInfoList(false, true, context);
                objList.addAll(platformObjList);
                json = JsonUtil.toJson(objList);
            }

            System.out.println(json);

            String url = DEVICES_URL + "/jsondevice.php";

            JsonHttp jsonHttp = new JsonHttp();
            String response = jsonHttp.post(url, json);

            System.out.println(response);

            GuiUtil.showDialog(AboutActivity.this, "Upload", response, "Ok");
        }
        catch (Exception e)
        {
            String text = e.getMessage();

            System.err.println(text);

            GuiUtil.showDialog(AboutActivity.this, "Error", text, "Close");
        }
    }

}
