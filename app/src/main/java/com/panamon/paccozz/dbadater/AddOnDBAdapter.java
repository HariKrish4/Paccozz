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
        CommonDBHelper.getInstance().getDb().insert(ADDON_SUBITEM_TABLE, null, values);
        CommonDBHelper.getInstance().close();
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
                addOnSubItemModels.add(addOnSubItemModel);
            } while (cursor.moveToNext());
        }
        CommonDBHelper.getInstance().close();
        return addOnSubItemModels;
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
    public String getTotalSubItemCost() {
        String totalCost = "0.00";
        CommonDBHelper.getInstance().open();
        Cursor sumCursor = null;
        try {
            sumCursor = CommonDBHelper.getInstance().getDb().rawQuery(" SELECT SUM " + "(" + ADDON_SUBITEM_PRICE + ")" + " FROM " + ADDON_SUBITEM_TABLE + " WHERE " + IS_ITEM_SELECTED + " = '" + 1 + "'", null);
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
