package com.example.andre;

/**
 * Created by andre on 12.03.16.
 */
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import javax.net.ssl.HttpsURLConnection;

public class HttpFileUpload
{
    final String LOG_NAME = "HttpFileUpload";

    String upLoadServerUri = "http://www.androidexample.com/media/UploadToServer.php";

    String serverFileHandlerName = "fileToUpload";

    public void setRemoteServerPath(String path)
    {
        upLoadServerUri = path;
    }

    public void setRemoteServerFileHandlerName(String name)
    {
        serverFileHandlerName = name;
    }

    private String getQuery(List<Pair<String,String>> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair<String,String> pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second, "UTF-8"));
        }

        return result.toString();
    }

    public int upload(String fileName)
    {
        int serverResponseCode = 0;

        HttpURLConnection conn = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(fileName);

        Log.i(LOG_NAME, fileName);

        if ( ! sourceFile.isFile())
        {
            Log.e(LOG_NAME, "Source File not exist:" + fileName);
        } else {
            try
            {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty(serverFileHandlerName, fileName);

                OutputStream os = conn.getOutputStream();

                ///
                //Uri.Builder builder = new Uri.Builder()

                //

                DataOutputStream dos = new DataOutputStream(os);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + serverFileHandlerName + "\";filename=\""
                                + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                //// !!!!!!!!!!
                InputStream in = null;
                try {
                    in = conn.getInputStream();
                    byte[] buffer2 = new byte[1024];
                    int read;
                    while ((read = in.read(buffer2)) > 0) {
                        System.out.println(read);
                        String text = new String(buffer2, 0, read, "utf-8");

                        Log.i(LOG_NAME, text.replace(lineEnd, "\n"));
                    }
                } finally {
                    in.close();
                }

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i(LOG_NAME, "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == HttpsURLConnection.HTTP_OK)
                {
                    Log.i(LOG_NAME, "OK. Complete...");
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            }
            catch (MalformedURLException ex)
            {
                ex.printStackTrace();

                Log.e(LOG_NAME, "MalformedURLException");

                Log.e(LOG_NAME, "error: " + ex.getMessage(), ex);
            }
            catch (Exception e)
            {
                e.printStackTrace();

                Log.e(LOG_NAME, "Exception: " + e.getMessage(), e);
            }
        }

        return serverResponseCode;
    }
}