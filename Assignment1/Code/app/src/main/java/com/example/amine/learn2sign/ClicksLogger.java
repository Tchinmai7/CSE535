package com.example.amine.learn2sign;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClicksLogger {
    private static ClicksLogger sSoleInstance;

    private ClicksLogger(){}  //private constructor.

    public static ClicksLogger getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new ClicksLogger();
        }
        return sSoleInstance;
    }

    public void updateLog(String logInfo){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd", Locale.US);
        Date now = new Date();
        long time_to_login = System.currentTimeMillis();
        String fileName = formatter.format(now) + ".txt";
        String SEPERATOR = " : ";
        try
        {
            File root = new File(Environment.getExternalStorageDirectory() + File.separator+ "Learn2Sign" + File.separator + "Group9", "Log Files");
            Log.i("updateLog", logInfo);
            if (!root.exists())
            {
                root.mkdirs();
            }
            File logFile = new File(root, fileName);

            FileWriter writer = new FileWriter(logFile,true);

            writer.append(String.valueOf(time_to_login)).append(SEPERATOR)
                    .append(Constants.userId).append(SEPERATOR)
                    .append(Constants.email).append(SEPERATOR)
                    .append(logInfo).append("\n");
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
