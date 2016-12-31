package com.panamon.paccozz.dbadater;

import android.content.ContentValues;
import android.database.Cursor;

import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.AddOnSubItemModel;
import com.panamon.paccozz.model.FoodItemModel;

import java.util.ArrayList;

/**
 * Created by Hari on 11/21/2016.
 */

public class AddOnDBAdapter implements TableConstants {

    //insert addon items
    public void insertAddOnItem(AddOnItemModel addOnItemModel) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_CATEGORY_ID, addOnItemModel.AddOnCateroryId);
        values.put(ADDON_ID, addOnItemModel.AddOnId);
        values.put(ADDON_NAME, addOnItemModel.AddOnName);
        CommonDBHelper.getInstance().getDb().insert(ADDON_ITEM_TABLE, null, values);
        CommonDBHelper.getInstance().close();
    }

    public void insertAddOnSubItem(AddOnSubItemModel addOnSubItemModel) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_CATEGORY_ID, addOnSubItemModel.AddOnCateroryId);
        values.put(ADDON_ID, addOnSubItemModel.AddOnId);
        values.put(ADDON_SUBITEM_ID, addOnSubItemModel.AddOnSubItemId);
        values.put(ADDON_SUBITEM_NAME, addOnSubItemModel.AddOnSubItemName);
        values.put(ADDON_SUBITEM_PRICE, addOnSubItemModel.AddOnPrice);
        values.put(IS_ITEM_SELECTED,"0");
        CommonDBHelper.getInstance().getDb().insert(ADDON_SUBITEM_TABLE, null, values);
        CommonDBHelper.getInstance().close();
    }

    public boolean checkAddOnRecordExists(String addOnId) {
        CommonDBHelper.getInstance().open();
        //String Query = "SELECT TOP 1 " + ITEM_VENDOR_ID + " FROM "+ FOOD_ITEM_TABLE + " WHERE " + ITEM_VENDOR_ID + " = " + vendorId;
        String Query = "SELECT * FROM " + ADDON_ITEM_TABLE + " WHERE " + ADDON_ID + " = " + addOnId;
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        CommonDBHelper.getInstance().close();
        return true;
    }

    public boolean checkAddOnSubItemRecordExists(String addOnSubItemId) {
        CommonDBHelper.getInstance().open();
        //String Query = "SELECT TOP 1 " + ITEM_VENDOR_ID + " FROM "+ FOOD_ITEM_TABLE + " WHERE " + ITEM_VENDOR_ID + " = " + vendorId;
        String Query = "SELECT * FROM " + ADDON_SUBITEM_TABLE + " WHERE " + ADDON_SUBITEM_ID + " = " + addOnSubItemId;
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        CommonDBHelper.getInstance().close();
        return true;
    }

    //get AddOn items with respect to addon id
    public AddOnSubItemModel getAddOnSubItem(String addOnSubItemId) {
        CommonDBHelper.getInstance().open();
        AddOnSubItemModel addOnSubItemModel = new AddOnSubItemModel();
        String query = "SELECT * FROM " + ADDON_SUBITEM_TABLE + " WHERE " + ADDON_SUBITEM_ID + " =" + addOnSubItemId + "";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                addOnSubItemModel.AddOnCateroryId = cursor.getString(1);
                addOnSubItemModel.AddOnId = cursor.getString(2);
                addOnSubItemModel.AddOnSubItemId = cursor.getString(3);
                addOnSubItemModel.AddOnSubItemName = cursor.getString(4);
                addOnSubItemModel.AddOnPrice = cursor.getString(5);
                addOnSubItemModel.IsItemSelected = cursor.getString(6);
            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return addOnSubItemModel;
    }

    //get AddOn items with respect to category id
    public ArrayList<AddOnItemModel> getAddOnItems(String category_id) {
        CommonDBHelper.getInstance().open();
        AddOnItemModel addOnItemModel;
        ArrayList<AddOnItemModel> addOnItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + ADDON_ITEM_TABLE + " WHERE " + ITEM_CATEGORY_ID + " =" + category_id + "";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                addOnItemModel = new AddOnItemModel();
                addOnItemModel.AddOnId = cursor.getString(2);
                addOnItemModel.AddOnName = cursor.getString(3);
                addOnItemModels.add(addOnItemModel);
            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return addOnItemModels;
    }

    //get AddOn Sub items with respect to category id
    public ArrayList<AddOnSubItemModel> getAddOnSubItems(String addOn_id) {
        CommonDBHelper.getInstance().open();
        AddOnSubItemModel addOnSubItemModel;
        ArrayList<AddOnSubItemModel> addOnSubItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + ADDON_SUBITEM_TABLE + " WHERE " + ADDON_ID + " =" + addOn_id + "";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                addOnSubItemModel = new AddOnSubItemModel();
                addOnSubItemModel.AddOnCateroryId = cursor.getString(1);
                addOnSubItemModel.AddOnId = cursor.getString(2);
                addOnSubItemModel.AddOnSubItemId = cursor.getString(3);
                addOnSubItemModel.AddOnSubItemName = cursor.getString(4);
                addOnSubItemModel.AddOnPrice = cursor.getString(5);
                addOnSubItemModel.IsItemSelected = cursor.getString(6);
                addOnSubItemModels.add(addOnSubItemModel);
            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return addOnSubItemModels;
    }

    public ArrayList<AddOnSubItemModel> getAddOnSelectedSubItems(String addOn_id) {
        CommonDBHelper.getInstance().open();
        AddOnSubItemModel addOnSubItemModel;
        ArrayList<AddOnSubItemModel> addOnSubItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + ADDON_SUBITEM_TABLE + " WHERE " + ADDON_ID + " ='" + addOn_id + "'" + " AND " + IS_ITEM_SELECTED + " = '1'";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                addOnSubItemModel = new AddOnSubItemModel();
                addOnSubItemModel.AddOnCateroryId = cursor.getString(1);
                addOnSubItemModel.AddOnId = cursor.getString(2);
                addOnSubItemModel.AddOnSubItemId = cursor.getString(3);
                addOnSubItemModel.AddOnSubItemName = cursor.getString(4);
                addOnSubItemModel.AddOnPrice = cursor.getString(5);
                addOnSubItemModel.IsItemSelected = cursor.getString(6);
                addOnSubItemModels.add(addOnSubItemModel);
            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return addOnSubItemModels;
    }

    public ArrayList<AddOnSubItemModel> getAddOnSelectedSubItems() {
        CommonDBHelper.getInstance().open();
        AddOnSubItemModel addOnSubItemModel;
        ArrayList<AddOnSubItemModel> addOnSubItemModels = new ArrayList<>();
        String query = "SELECT * FROM " + ADDON_SUBITEM_TABLE + " WHERE " + IS_ITEM_SELECTED + " = '1'";
        Cursor cursor = CommonDBHelper.getInstance().getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                addOnSubItemModel = new AddOnSubItemModel();
                addOnSubItemModel.AddOnCateroryId = cursor.getString(1);
                addOnSubItemModel.AddOnId = cursor.getString(2);
                addOnSubItemModel.AddOnSubItemId = cursor.getString(3);
                addOnSubItemModel.AddOnSubItemName = cursor.getString(4);
                addOnSubItemModel.AddOnPrice = cursor.getString(5);
                addOnSubItemModel.IsItemSelected = cursor.getString(6);
                addOnSubItemModels.add(addOnSubItemModel);
            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return addOnSubItemModels;
    }

    //updating addonItem
    public void updateAddOnItem(String addOnId, AddOnItemModel addOnItemModel) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_CATEGORY_ID, addOnItemModel.AddOnCateroryId);
        values.put(ADDON_NAME, addOnItemModel.AddOnName);
        CommonDBHelper.getInstance().getDb().update(ADDON_ITEM_TABLE, values, ADDON_ID + " = ?",
                new String[]{String.valueOf(addOnId)});
        CommonDBHelper.getInstance().close();
    }

    //updating addonSubItem
    public void updateAddOnSubItem(String addOnSubItemId, AddOnSubItemModel addOnSubItemOnlineModel) {
        AddOnSubItemModel addOnSubItemModel = getAddOnSubItem(addOnSubItemId);
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(ITEM_CATEGORY_ID, addOnSubItemOnlineModel.AddOnCateroryId);
        values.put(ADDON_ID, addOnSubItemOnlineModel.AddOnId);
        values.put(ADDON_SUBITEM_NAME, addOnSubItemOnlineModel.AddOnSubItemName);
        values.put(ADDON_SUBITEM_PRICE, addOnSubItemOnlineModel.AddOnPrice);
        values.put(IS_ITEM_SELECTED,addOnSubItemModel.IsItemSelected);
        CommonDBHelper.getInstance().getDb().update(ADDON_SUBITEM_TABLE, values, ADDON_SUBITEM_ID + " = ?",
                new String[]{String.valueOf(addOnSubItemId)});
        CommonDBHelper.getInstance().close();
    }

    //update selected item value when the value is 0
    public void updateIsItemSelected(String isItemSelected, String itemId) {
        CommonDBHelper.getInstance().open();
        ContentValues values = new ContentValues();
        values.put(IS_ITEM_SELECTED, isItemSelected);
        CommonDBHelper.getInstance().getDb().update(ADDON_SUBITEM_TABLE, values, ADDON_SUBITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
        CommonDBHelper.getInstance().close();
    }

    //getting sum of totalcost
    public String getTotalSubItemCost(String subItemId) {
        String totalCost = "0.00";
        CommonDBHelper.getInstance().open();
        Cursor sumCursor = null;
        try {
            sumCursor = CommonDBHelper.getInstance().getDb().rawQuery(" SELECT SUM " + "(" + ADDON_SUBITEM_PRICE + ")" + " FROM " + ADDON_SUBITEM_TABLE + " WHERE " + IS_ITEM_SELECTED + " = '" + 1 + "'" + " AND " + ADDON_SUBITEM_ID + " = '" + subItemId +"'", null);
            if (sumCursor != null && sumCursor.getCount() > 0 && sumCursor.moveToFirst()) {
                totalCost = String.format("%.2f", sumCursor.getDouble(0));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (!sumCursor.isClosed())
                sumCursor.close();
        }
        CommonDBHelper.getInstance().close();
        return totalCost;
    }
}
