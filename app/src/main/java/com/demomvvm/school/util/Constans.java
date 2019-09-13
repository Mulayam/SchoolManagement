package com.demomvvm.school.util;

/**
 * Created by win10 on 5/17/2017.
 */

public class Constans {

    public static boolean PROFILE_REFRESS = false;
    public static boolean BOOKING_REFRESS = false;
    public static boolean IS_HOME =false;
    public static int SQLITE_VERSION = 1;
    public static final int RC_SIGN_IN = 0;

    public final static String KEY_REQUESTING_LOCATION_UPDATES="requesting-location-updates";
    public final static String KEY_LOCATION="location";
    public final static String KEY_LAST_UPDATED_TIME_STRING="last-updated-time-string";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS=10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS=
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static double CURRENT_LATITUDE=0.0;
    public static double CURRENT_LONGITUDE=0.0;
}
