package com.panamon.paccozz.dbadater;

/**
 * Created by Hari on 11/19/2016.
 */

public interface TableConstants {

    public String KEY_ID = "id";

    //food item and selected food item table columns
    public String ITEM_ID = "item_id";
    public String ITEM_NAME = "item_name";
    public String ITEM_COST = "item_cost";
    public String ITEM_COUNT = "item_count";
    public String ITEM_CATEGORY_ID = "item_category_id";
    public String ITEM_VENDOR_ID = "item_vendor_id";
    public String IS_ITEM_SELECTED = "is_item_selected";
    public String TOTAL_COST = "total_cost";
    public String ITEM_TYPE = "item_type";
    public String ITEM_TOTAL_COST = "item_total_cost";
    public String ITEM_DISCOUNT = "item_discount";
    public String ITEM_SERVICE_TAX = "item_service_tax";

    //addon item and addon sub item table columns
    public String ADDON_ID = "addon_id";
    public String ADDON_NAME = "addon_name";
    public String ADDON_LIMITS = "addon_limits";
    public String ADDON_SUBITEM_ID = "addon_subitem_id";
    public String ADDON_SUBITEM_NAME = "addon_subitem_name";
    public String ADDON_SUBITEM_PRICE = "addon_subitem_price";

    //Table Names
    public String FOOD_ITEM_TABLE = "FoodItemTable";
    public String ADDON_ITEM_TABLE = "AddonItemTable";
    public String ADDON_SUBITEM_TABLE = "AddonSubItemTable";
    public String CART_TABLE = "CartTable";

    //Create table queries
    public String CREATE_FOOD_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + FOOD_ITEM_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + ITEM_ID + " TEXT," + ITEM_NAME + " TEXT," + ITEM_COST + " TEXT," + ITEM_COUNT + " TEXT,"
            + ITEM_CATEGORY_ID + " TEXT," + ITEM_VENDOR_ID + " TEXT," + IS_ITEM_SELECTED + " TEXT," + TOTAL_COST + " TEXT," + ITEM_TYPE + " TEXT," +  ITEM_TOTAL_COST + " TEXT," + ITEM_DISCOUNT + " TEXT," + ITEM_SERVICE_TAX + " TEXT" + ")";
    public String CREATE_ADDON_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + ADDON_ITEM_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + ITEM_CATEGORY_ID + " TEXT," + ADDON_ID + " TEXT," + ADDON_NAME + " TEXT," + ADDON_LIMITS + " TEXT" + ")";
    public String CREATE_ADDON_SUBITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + ADDON_SUBITEM_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + ITEM_CATEGORY_ID + " TEXT," + ADDON_ID + " TEXT," + ADDON_SUBITEM_ID + " TEXT," + ADDON_SUBITEM_NAME + " TEXT," + ADDON_SUBITEM_PRICE + " TEXT," + IS_ITEM_SELECTED + " TEXT" + ")";
    public String CREATE_CART_TABLE = "CREATE TABLE IF NOT EXISTS " + CART_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + ITEM_VENDOR_ID + " TEXT" + ")";
}


