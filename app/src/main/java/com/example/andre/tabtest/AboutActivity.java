package com.example.andre.tabtest;

import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.example.andre.GuiUtil;
import com.example.andre.InfoList;
import com.example.andre.InfoUtils;
import com.example.andre.JsonHttp;
import com.example.andre.JsonUtil;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

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

        TextView aboutBottomTextView = (TextView)findViewById(R.id.aboutBottomTextView);
        aboutBottomTextView.setText(R.string.about_bottom);
    }

    public void onPressUploadButton(View view)
    {
        sendReport();
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

            if (InfoUtils.isMtkPlatform(platform))
            {
                ArrayList<Pair<String, String>> objList = InfoList.buildProjectConfigList();

                json = JsonUtil.toJson(objList);
            }
            else
            {
                ArrayList<Pair<String, String>> objList = InfoList.buildInfoList(false, true);

                json = JsonUtil.toJson(objList);
            }

            System.out.println(json);

            String url = "http://192.168.0.101/devices/jsondevice.php";
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
