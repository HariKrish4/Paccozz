package com.panamon.paccozz.dbadater;

import android.content.ContentValues;
import android.database.Cursor;

import com.panamon.paccozz.model.FoodItemModel;

import java.util.ArrayList;

/**
 * Created by Hari on 11/21/2016.
 */

public class SelectedFoodItemDBAdapter implements TableConstants {

    //insert Food items
    public void insertFoodItem(FoodItemModel foodItemModel) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_ID, foodItemModel.ItemId);
        values.put(ITEM_NAME, foodItemModel.ItemName);
        values.put(ITEM_COST, foodItemModel.ItemCost);
        values.put(ITEM_COUNT, foodItemModel.ItemCount);
        values.put(ITEM_CATEGORY_ID, foodItemModel.ItemCategoryId);
        values.put(ITEM_VENDOR_ID, foodItemModel.ItemVendorId);
        CommonDBHelper.getInstance().getDb().insert(SELECTED_FOOD_ITEM_TABLE, null, values);
        CommonDBHelper.getInstance().close();
    }

    //get food items with respect to category id
    public ArrayList<FoodItemModel> getFoodItems(String category_id) {
        CommonDBHelper.getInstance().open();
        FoodItemModel foodItemModel;
        ArrayList<FoodItemModel> foodItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + SELECTED_FOOD_ITEM_TABLE + " WHERE " + ITEM_CATEGORY_ID + " =" + category_id + "";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                foodItemModel = new FoodItemModel();
                foodItemModel.ItemId = cursor.getString(1);
                foodItemModel.ItemName = cursor.getString(2);
                foodItemModel.ItemCost = cursor.getString(3);
                foodItemModel.ItemCount = cursor.getString(4);
                foodItemModels.add(foodItemModel);

            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return foodItemModels;
    }

    //updating itemcount
    public void updateCustomerId(String itemCount, String itemId) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_COUNT, itemCount);
        CommonDBHelper.getInstance().getDb().update(SELECTED_FOOD_ITEM_TABLE, values, ITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
        CommonDBHelper.getInstance().close();
    }
}
