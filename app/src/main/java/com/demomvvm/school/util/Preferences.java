package com.demomvvm.school.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final int MODE = 0;

    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    public static final String DEVICE_ID = "1231232312";
    public static final String USER_ID = "USER_ID";
    public static final String IS_FIRST_TIME_INTRO = "IS_FIRST_TIME_INTRO";
    public static final String IS_SKIPED = "IS_SKIPED";
    public static final String USER_NAME = "USER_NAME";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String USER_IMAGE = "USER_IMAGE";
    public static final String USER_EMAIL_ID = "USER_EMAIL_ID";
    public static final String USER_PHONE_NO = "USER_PHONE_NO";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    public static final String SELECTED_CITY_LATITUDE = "SELECTED_CITY_LATITUDE";
    public static final String SELECTED_CITY_LONGITUDE = "SELECTED_CITY_LONGITUDE";
    public static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    public static final String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";

    public static final String SELECTED_CATEGORIES_ID = "SELECTED_CATEGORIES_ID";
    public static final String SELECTED_LOCATION_ID = "SELECTED_LOCATION_ID";
    public static final String SELECTEDLOCATIONORCITY = "SELECTEDLOCATIONORCITY";
    public static final String SELECTED_CITY = "SELECTED_CITY";
    public static final String SELECTED_CURRENT_CITY = "SELECTED_CURRENT_CITY";
    public static final String RATE_US_DIALOG_SHOW_LAST_DATE = "RATE_US_DIALOG_SHOW_LAST_DATE";
    public static final String RATE_US_DIALOG_STATUS = "RATE_US_DIALOG_STATUS";


    public static final String SELECTED_LANGUAGE_ID = "SELECTED_LANGUAGE_ID";
    public static final String SELECTED_LANGUAGE_PREFIX = "SELECTED_LANGUAGE_PREFIX";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String LOGIN_TYPE = "Login_type";

    public static SharedPreferences.Editor getEditor(Context paramContext) {
        return getPreferences(paramContext).edit();
    }

    public static SharedPreferences getPreferences(Context paramContext) {
        return paramContext.getSharedPreferences("ENUMERATOR_PREFERENCES", 0);
    }

    public static boolean readBoolean(Context paramContext, String paramString, boolean paramBoolean) {
        return getPreferences(paramContext).getBoolean(paramString,
                paramBoolean);
    }

    public static int readInteger(Context paramContext, String paramString, int paramInt) {
        return getPreferences(paramContext).getInt(paramString, paramInt);
    }

    public static long readLong(Context paramContext, String paramString, long paramLong) {
        return getPreferences(paramContext).getLong(paramString, paramLong);
    }

    public static String readString(Context paramContext, String paramString1, String paramString2) {
        return getPreferences(paramContext).getString(paramString1,
                paramString2);
    }

    public static void writeBoolean(Context paramContext, String paramString, boolean paramBoolean) {
        getEditor(paramContext).putBoolean(paramString, paramBoolean).commit();
    }

    public static void writeInteger(Context paramContext, String paramString, int paramInt) {
        getEditor(paramContext).putInt(paramString, paramInt).commit();
    }

    public static void writeLong(Context paramContext, String paramString, long paramLong) {
        getEditor(paramContext).putLong(paramString, paramLong).commit();
    }

    public static void writeString(Context paramContext, String paramString1, String paramString2) {
        getEditor(paramContext).putString(paramString1, paramString2).commit();
    }
}
