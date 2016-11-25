package com.panamon.paccozz.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hari on 11/24/2016.
 */

public class SharedPref {

    private static String MY_STRING_PREF = "Paccozz";
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        shared = context.getSharedPreferences(MY_STRING_PREF,
                Context.MODE_PRIVATE);
    }

    public void setLoginData(String UserId,
                             String UserName, String UserMobile, String UserEmail,
                             String ParkId, String ParkName) {
        editor = shared.edit();
        editor.putString("UserName", UserName);
        editor.putString("UserId", UserId);
        editor.putString("UserMobile", UserMobile);
        editor.putString("UserEmail", UserEmail);
        editor.putString("ParkId", ParkId);
        editor.putString("ParkName", ParkName);

        editor.apply();
    }

    public void setIsLogged(boolean value) {
        editor = shared.edit();
        editor.putBoolean("LoginStatus", value);
        editor.apply();
    }

    public boolean getIsLogged() {
        return shared.getBoolean("LoginStatus", false);
    }

    public String getUserId() {
        return shared.getString("UserId", null);
    }

    public String getUserName() {
        return shared.getString("UserName", null);
    }

    public String getUserEmail() {
        return shared.getString("UserEmail", null);
    }

    public String getUserMobile() {
        return shared.getString("UserMobile", null);
    }

    public String getParkId() {
        return shared.getString("ParkId", null);
    }

    public String getParkName() {
        return shared.getString("ParkName", null);
    }
}
