package com.panamon.paccozz.dbadater;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Hari on 11/19/2016.
 */

public class CommonDBHelper extends SQLiteOpenHelper implements TableConstants {

    private static final String DATABASE_NAME = "Paccozz";
    private static final int DATABASE_VERSION = 1;
    public static CommonDBHelper commonDbHelper;
    public static SQLiteDatabase db;
    public Context context;

    public CommonDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static CommonDBHelper getInstance(Context context) {
        if (commonDbHelper == null) {
            commonDbHelper = new CommonDBHelper(context);
        }
        return commonDbHelper;
    }

    public static CommonDBHelper getInstance() {
        return commonDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FOOD_ITEM_TABLE);
        db.execSQL(CREATE_SELECTED_FOOD_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public CommonDBHelper open() throws SQLException {
        db = commonDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * This method will check whether DB Available or Not
     *
     * @return true or false based on DB existance
     */
    public Boolean checkDBExists() {
        return db != null ? true : false;
    }

    /**
     * It will close DB Connection.
     */
    public void close() {
        db.close();
    }

    /**
     * It will return sqlite object
     */
    public synchronized SQLiteDatabase getDb() {
        return db;
    }

    /**
     * This method will check whether Table Available or Not
     *
     * @param table - name of the table to checked
     * @return true or false
     */
    public boolean checkTableExists(String table) {
        try {
            db.execSQL("SELECT * FROM " + table);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /*public void deleteTableValues(String table)
    {
        db = commonDbHelper.getWritableDatabase();
        db.delete(table, null, null);
        db.close();
    }*/

    public ArrayList<String> getAllTableNames() {
        db = commonDbHelper.getWritableDatabase();
        ArrayList<String> arrTblNames = new ArrayList<String>();

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        db.close();
        return arrTblNames;
    }

    public void truncateAllTables() {
        ArrayList<String> tables = getAllTableNames();
        for (int i = 0; i < tables.size(); i++) {
            deleteTableValues(tables.get(i));
        }
    }

    public void deleteTableValues(String table) {
        try {
            CommonDBHelper.getInstance().open();
            db.execSQL("delete from " + table);
            CommonDBHelper.getInstance().close();
        } catch (SQLException e) {

        }
    }
}
