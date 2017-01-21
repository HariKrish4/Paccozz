package com.panamon.paccozz.dbadater;

import android.content.ContentValues;
import android.database.Cursor;

import com.panamon.paccozz.model.FoodItemModel;

import java.util.ArrayList;

/**
 * Created by Hari on 11/19/2016.
 */

public class FoodItemDBAdapter implements TableConstants {

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
        values.put(ITEM_VENDOR_NAME, foodItemModel.ItemVendorName);
        values.put(IS_ITEM_SELECTED, foodItemModel.IsItemSelected);
        values.put(TOTAL_COST, "0");
        values.put(ITEM_TYPE, foodItemModel.ItemType);
        values.put(ITEM_TOTAL_COST, foodItemModel.ItemCost);
        values.put(ITEM_DISCOUNT, foodItemModel.ItemDiscount);
        values.put(ITEM_SERVICE_TAX, foodItemModel.ItemServiceTax);
        values.put(ITEM_PACKAGE_CHARGE, foodItemModel.ItemPackageCharge);
        CommonDBHelper.getInstance().getDb().insert(FOOD_ITEM_TABLE, null, values);
        CommonDBHelper.getInstance().close();
    }

    //get food items with respect to category id
    public ArrayList<FoodItemModel> getFoodItems(String category_id, String item_type) {
        CommonDBHelper.getInstance().open();
        FoodItemModel foodItemModel;
        ArrayList<FoodItemModel> foodItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_CATEGORY_ID + " ='" + category_id + "'" + " AND " + ITEM_TYPE + " ='" + item_type + "'";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                foodItemModel = new FoodItemModel();
                foodItemModel.ItemId = cursor.getString(1);
                foodItemModel.ItemName = cursor.getString(2);
                foodItemModel.ItemCost = cursor.getString(3);
                foodItemModel.ItemCount = cursor.getString(4);
                foodItemModel.TotalCost = cursor.getString(8);
                foodItemModel.ItemTotalCost = cursor.getString(10);
                foodItemModel.ItemVendorName = cursor.getString(13);
                foodItemModels.add(foodItemModel);

            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return foodItemModels;
    }


    //get food items with respect to category id
    public ArrayList<FoodItemModel> getWeirdFoodItems(String category_id) {
        CommonDBHelper.getInstance().open();
        FoodItemModel foodItemModel;
        ArrayList<FoodItemModel> foodItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_CATEGORY_ID + " ='" + category_id + "'" + " AND " + ITEM_TYPE + " = 3";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                foodItemModel = new FoodItemModel();
                foodItemModel.ItemId = cursor.getString(1);
                foodItemModel.ItemName = cursor.getString(2);
                foodItemModel.ItemCost = cursor.getString(3);
                foodItemModel.ItemCount = cursor.getString(4);
                foodItemModel.TotalCost = cursor.getString(8);
                foodItemModel.ItemTotalCost = cursor.getString(10);
                foodItemModel.ItemVendorName = cursor.getString(13);
                foodItemModels.add(foodItemModel);

            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return foodItemModels;
    }

    //get food items with respect to category id
    public ArrayList<FoodItemModel> getFoodItems(String category_id) {
        CommonDBHelper.getInstance().open();
        FoodItemModel foodItemModel;
        ArrayList<FoodItemModel> foodItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_CATEGORY_ID + " ='" + category_id + "'";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                foodItemModel = new FoodItemModel();
                foodItemModel.ItemId = cursor.getString(1);
                foodItemModel.ItemName = cursor.getString(2);
                foodItemModel.ItemCost = cursor.getString(3);
                foodItemModel.ItemCount = cursor.getString(4);
                foodItemModel.ItemCategoryId = cursor.getString(5);
                foodItemModel.TotalCost = cursor.getString(8);
                foodItemModel.ItemTotalCost = cursor.getString(10);
                foodItemModel.ItemVendorName = cursor.getString(13);
                foodItemModels.add(foodItemModel);

            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return foodItemModels;
    }

    public boolean checkRecordExists(String vendorId) {
        CommonDBHelper.getInstance().open();
        //String Query = "SELECT TOP 1 " + ITEM_VENDOR_ID + " FROM "+ FOOD_ITEM_TABLE + " WHERE " + ITEM_VENDOR_ID + " = " + vendorId;
        String Query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_VENDOR_ID + " = " + vendorId;
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        CommonDBHelper.getInstance().close();
        return true;
    }

    public boolean checkFoodRecordExists(String itemId) {
        CommonDBHelper.getInstance().open();
        //String Query = "SELECT TOP 1 " + ITEM_VENDOR_ID + " FROM "+ FOOD_ITEM_TABLE + " WHERE " + ITEM_VENDOR_ID + " = " + vendorId;
        String Query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_ID + " = " + itemId;
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        CommonDBHelper.getInstance().close();
        return true;
    }

    //updating fooditem
    public void updateFoodItem(String itemId, FoodItemModel foodItemOnlineModel) {
        FoodItemModel foodItemModel = getFoodItem(itemId);
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, foodItemOnlineModel.ItemName);
        values.put(ITEM_COST, foodItemOnlineModel.ItemCost);
        values.put(ITEM_COUNT, foodItemModel.ItemCount);
        values.put(ITEM_CATEGORY_ID, foodItemOnlineModel.ItemCategoryId);
        values.put(IS_ITEM_SELECTED, foodItemModel.IsItemSelected);
        values.put(TOTAL_COST, foodItemModel.TotalCost);
        values.put(ITEM_TYPE, foodItemOnlineModel.ItemType);
        values.put(ITEM_TOTAL_COST, foodItemModel.ItemTotalCost);
        CommonDBHelper.getInstance().getDb().update(FOOD_ITEM_TABLE, values, ITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
        CommonDBHelper.getInstance().close();
    }

    //updating itemcount
    public void updateItemCount(String itemCount, String itemId) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_COUNT, itemCount);
        values.put(IS_ITEM_SELECTED, "1");
        CommonDBHelper.getInstance().getDb().update(FOOD_ITEM_TABLE, values, ITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
        CommonDBHelper.getInstance().close();
    }

    //updating totalcost
    public void updateTotalCost(String totalCost, String itemId) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(TOTAL_COST, totalCost);
        CommonDBHelper.getInstance().getDb().update(FOOD_ITEM_TABLE, values, ITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
        CommonDBHelper.getInstance().close();
    }

    //updating itemtotalcost
    public void updateItemTotalCost(String totalCost, String itemId) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_TOTAL_COST, totalCost);
        CommonDBHelper.getInstance().getDb().update(FOOD_ITEM_TABLE, values, ITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
        CommonDBHelper.getInstance().close();
    }

    //updating itemcost
    public void updateItemCost(String itemCost, String itemId) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_COST, itemCost);
        CommonDBHelper.getInstance().getDb().update(FOOD_ITEM_TABLE, values, ITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
        CommonDBHelper.getInstance().close();
    }

    //update selected item value when the value is 0
    public void updateIsItemSelected(String isItemSelected, String itemId) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(IS_ITEM_SELECTED, isItemSelected);
        CommonDBHelper.getInstance().getDb().update(FOOD_ITEM_TABLE, values, ITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
        CommonDBHelper.getInstance().close();
    }


    //get itemcount
    public String getItemCount(String itemId) {
        CommonDBHelper.getInstance().open();
        String query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_ID + " ='" + itemId + "'";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String itemcount = cursor.getString(4);
                CommonDBHelper.getInstance().close();
                return itemcount;
            } while (cursor.moveToNext());
        } else {
            CommonDBHelper.getInstance().close();
            return "0";
        }

    }

    //get itemcount
    public String getItemCost(String itemId) {
        CommonDBHelper.getInstance().open();
        String query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_ID + " ='" + itemId + "'";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String itemcost = cursor.getString(3);
                CommonDBHelper.getInstance().close();
                return itemcost;
            } while (cursor.moveToNext());
        } else {
            CommonDBHelper.getInstance().close();
            return "0";
        }

    }

    public String getItemTotalCost(String itemId) {
        CommonDBHelper.getInstance().open();
        String query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_ID + " ='" + itemId + "'";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String itemcost = cursor.getString(10);
                CommonDBHelper.getInstance().close();
                return itemcost;
            } while (cursor.moveToNext());
        } else {
            CommonDBHelper.getInstance().close();
            return "0";
        }

    }

    //get food selected items with respect to item id
    public FoodItemModel getFoodItem(String itemId) {
        CommonDBHelper.getInstance().open();
        FoodItemModel foodItemModel = new FoodItemModel();
        String query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + ITEM_ID + " = " + itemId;
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                foodItemModel.ItemId = cursor.getString(1);
                foodItemModel.ItemName = cursor.getString(2);
                foodItemModel.ItemCost = cursor.getString(3);
                foodItemModel.ItemCount = cursor.getString(4);
                foodItemModel.ItemCategoryId = cursor.getString(5);
                foodItemModel.IsItemSelected = cursor.getString(7);
                foodItemModel.TotalCost = cursor.getString(8);
                foodItemModel.ItemType = cursor.getString(9);
                foodItemModel.ItemTotalCost = cursor.getString(10);
                foodItemModel.ItemDiscount = cursor.getString(11);
                foodItemModel.ItemServiceTax = cursor.getString(12);
                foodItemModel.ItemVendorName = cursor.getString(13);
                foodItemModel.ItemPackageCharge = cursor.getString(14);
            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return foodItemModel;
    }

    //get food selected items with respect to category id
    public ArrayList<FoodItemModel> getSelectedFoodItems() {
        CommonDBHelper.getInstance().open();
        FoodItemModel foodItemModel;
        ArrayList<FoodItemModel> foodItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + FOOD_ITEM_TABLE + " WHERE " + IS_ITEM_SELECTED + " = '1'";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                foodItemModel = new FoodItemModel();
                foodItemModel.ItemVendorId = cursor.getString(6);
                foodItemModel.ItemId = cursor.getString(1);
                foodItemModel.ItemName = cursor.getString(2);
                foodItemModel.ItemCost = cursor.getString(3);
                foodItemModel.ItemCount = cursor.getString(4);
                foodItemModel.TotalCost = cursor.getString(8);
                foodItemModel.ItemDiscount = cursor.getString(11);
                foodItemModel.ItemServiceTax = cursor.getString(12);
                foodItemModel.ItemVendorName = cursor.getString(13);
                foodItemModel.ItemPackageCharge = cursor.getString(14);
                foodItemModels.add(foodItemModel);

            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return foodItemModels;
    }

    //getting sum of totalcost
    public String getTotalCost() {
        String totalCost = "0.00";
        CommonDBHelper.getInstance().open();
        Cursor sumCursor = null;
        try {
            sumCursor = CommonDBHelper.getInstance().getDb().rawQuery(" SELECT SUM " + "(" + TOTAL_COST + ")" + " FROM " + FOOD_ITEM_TABLE + " WHERE " + IS_ITEM_SELECTED + " = '" + 1 + "'", null);
            if (sumCursor != null && sumCursor.getCount() > 0 && sumCursor.moveToFirst()) {
                totalCost = String.format("%.2f", sumCursor.getDouble(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sumCursor.isClosed())
                sumCursor.close();
        }
        CommonDBHelper.getInstance().close();
        return totalCost;
    }

    //getting sum of totalcost
    public String getTotalItems() {
        String totalItems = "0";
        CommonDBHelper.getInstance().open();
        Cursor sumCursor = null;
        try {
            sumCursor = CommonDBHelper.getInstance().getDb().rawQuery(" SELECT SUM " + "(" + ITEM_COUNT + ")" + " FROM " + FOOD_ITEM_TABLE + " WHERE " + IS_ITEM_SELECTED + " = '" + 1 + "'", null);
            if (sumCursor != null && sumCursor.getCount() > 0 && sumCursor.moveToFirst()) {
                totalItems = String.format("%.2f", sumCursor.getDouble(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sumCursor.isClosed())
                sumCursor.close();
        }
        CommonDBHelper.getInstance().close();
        return totalItems;
    }

    //insert cart items
    public void insertCartItem(String vendorId) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_VENDOR_ID, vendorId);
        CommonDBHelper.getInstance().getDb().insert(CART_TABLE, null, values);
        CommonDBHelper.getInstance().close();
    }

    //get cart items
    public String getCartItem() {
        CommonDBHelper.getInstance().open();
        String query = "SELECT * FROM " + CART_TABLE;
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String itemcount = cursor.getString(1);
                CommonDBHelper.getInstance().close();
                return itemcount;
            } while (cursor.moveToNext());
        } else {
            CommonDBHelper.getInstance().close();
            return "0";
        }
    }
}


