package com.example.andre;

import com.example.andre.androidshell.ShellExecuter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andre on 20.08.16.
 */
public class QcomUtil {

    final public static String QCAMERA_LIB_PATH    = "/system/vendor/lib";
    final public static String QCAMERA_DAEMON_NAME = "mm-qcamera-daemon";
    final public static String QCAMERA_LIB_PREFIX  = "libmmcamera_";
    final public static String QCAMERA_LIB_ACTUATOR_PREFIX  = "libactuator_";

    public static ArrayList<String> getQcomCameraList0(ArrayList<String> driverList)
    {
        ArrayList<String> list  = new ArrayList<String>();

        for (String line0 : driverList)
        {
            String line = line0;

            if (line.startsWith("qcom,")) line = line.replace("qcom,", "").trim();

            String value = line.toUpperCase();

            int pos = value.indexOf("(");

            if (pos != -1)
            {
                value = value.substring(0, pos).trim();
            }

            if (InfoUtils.isCameraMatched(InfoUtils.cameraPrefixList, value))
            {
                list.add(line);
            }
        }

        return list;
    }

    public static String getVendorLibs(ShellExecuter se)
    {
        String path = QCAMERA_LIB_PATH;

        String command = "ls " + path;

        return se.execute(command);
    }

    public static ArrayList<String> getCameraList(String str)
    {
        ArrayList<String> list  = new ArrayList<String>();

        String[] libList = str.split("\n");

        final String filter = QCAMERA_LIB_PREFIX;

        for (String line : libList) {
            if (line.contains(filter)) // line.contains("libactuator")
            {
                int index1 = line.lastIndexOf("/");

                if (index1 != -1) line = line.substring(index1 + 1 + filter.length());

                line = line.substring(0, line.length() - 3);

                if (InfoUtils.isCameraMatched(InfoUtils.cameraPrefixList, line.toUpperCase())) {
                    if ( ! list.contains(line)) list.add(line);
                }
            }
        }

        return list;
    }

    public static int getCameraPid(String ps)
    {
        String[] list = ps.split("\n");

        for (String line : list) {
            if (line.contains(QCAMERA_DAEMON_NAME))
            {
                System.out.println(line);

                // camera  pid ... mm-qcamera-daemon
                Pattern pattern = Pattern.compile("(\\w+)\\s+([0-9]{1,})\\s+\\w*");

                Matcher m = pattern.matcher(line);

                if (m.find()) {
                    //String owner = m.group(1);

                    int pid = Integer.parseInt(m.group(2));

                    return pid;
                }

            }
        }

        return 0;
    }

    public static ArrayList<String> getLensList(ShellExecuter se, ArrayList<String> cameraList)
    {
        ArrayList<String> list  = new ArrayList<String>();

        String str = getVendorLibs(se);

        String[] libList = str.split("\n");

        final String filter = QCAMERA_LIB_ACTUATOR_PREFIX;

        for (String line : libList) {

            if (line.contains(filter))
            {
                Pattern pattern = Pattern.compile(QCAMERA_LIB_ACTUATOR_PREFIX + "([0-9a-z]+)\\.so");

                Matcher m = pattern.matcher(line);

                if (m.matches()) {
                    String lens = m.group(1);

                    if (isLensUsed(lens, cameraList)) list.add(lens);
                }
            }
        }

        return list;
    }

    public static boolean isLensUsed(String lens, ArrayList<String> cameraList)
    {
        for (String cameraName : cameraList)
        {
            String fileName = QCAMERA_LIB_PATH + "/" + QCAMERA_LIB_PREFIX + cameraName + ".so";

            String searchPattern = lens;

            ArrayList<String> list = BinaryDataHelper.getStringCapturedList(fileName, searchPattern, 1000);

            if ( ! list.isEmpty()) return true;
        }

        return false;
    }

}
