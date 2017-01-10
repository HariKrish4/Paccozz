package com.panamon.paccozz.common;

import android.content.Context;

import com.panamon.paccozz.model.CategoryModel;

import java.util.ArrayList;

/**
 * Created by Hari on 11/17/2016.
 */

public class Singleton {

    private Singleton() {
    }
    private static Singleton instance = null;
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public String UserId = "";
    public String UserEmail = "";
    public String UserMobile = "";
    public String UserName = "";
    public String UserGender = "";
    public String UserCity = "";
    public String UserPass = "";
    public String UserAge = "";
    public String ParkId = "";
    public String ParkName = "";
    public String VendorId = "";
    public String VendorName = "";
    public String WalletAmount = "";
    public String ProfileImage = "";
    public Context Context;
    public ArrayList<CategoryModel> categories = new ArrayList<>();
    public String AddOns = "";
}
