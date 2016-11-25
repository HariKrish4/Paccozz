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

    //Table Names
    public String FOOD_ITEM_TABLE = "FoodItem";
    public String SELECTED_FOOD_ITEM_TABLE = "SelectedFoodItem";

    //Create table queries
    public String CREATE_FOOD_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + FOOD_ITEM_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + ITEM_ID + " TEXT," + ITEM_NAME + " TEXT," + ITEM_COST + " TEXT," + ITEM_COUNT + " TEXT,"
            + ITEM_CATEGORY_ID + " TEXT," + ITEM_VENDOR_ID + " TEXT," + IS_ITEM_SELECTED + " TEXT," + TOTAL_COST + " TEXT,"+ ITEM_TYPE + " TEXT" + ")";
    public String CREATE_SELECTED_FOOD_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + SELECTED_FOOD_ITEM_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + ITEM_ID + " TEXT," + ITEM_NAME + " TEXT," + ITEM_COST + " TEXT," + ITEM_COUNT + " TEXT,"
            + ITEM_CATEGORY_ID + " TEXT," + ITEM_VENDOR_ID + " TEXT" + ")";
}


