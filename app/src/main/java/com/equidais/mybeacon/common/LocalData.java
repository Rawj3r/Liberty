package com.equidais.mybeacon.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class LocalData {
    public static String getUserName(Context context){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getString(GlobalConst.PREF_ATTR_USERNAME, "");
    }

    public static int getUserID(Context context){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getInt(GlobalConst.PREF_ATTR_USERID, -1);
    }

    public static String getPassword(Context context){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getString(GlobalConst.PREF_ATTR_PASSWORD, "");
    }

    public static void setUsernameAndPassword(Context context, String userName, String password, int userID){
        SharedPreferences pref = context.getSharedPreferences(GlobalConst.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(GlobalConst.PREF_ATTR_USERNAME, userName);
        editor.putString(GlobalConst.PREF_ATTR_PASSWORD, password);
        editor.putInt(GlobalConst.PREF_ATTR_USERID, userID);
        editor.commit();
    }

    public static String loadSha(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Storedata", 0);
        String usermail = sharedPreferences.getString("username", "");
        return usermail;
    }

    public static String getmail(){
        String rrr = null;

        String file_name = "/sdcard/0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.txt";


        try{
            BufferedReader br = new BufferedReader(new FileReader(file_name));
            StringBuilder builder = new StringBuilder();
            String line = br.readLine();

            while (line != null){
                builder.append(line);
                builder.append(System.lineSeparator());
                line = br.readLine();
            }

            rrr = builder.toString();

        }catch(IOException e){
            e.printStackTrace();
        }

        return rrr;
    }



}
