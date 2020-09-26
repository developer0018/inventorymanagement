package com.Hackathon.Inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class activity4 extends SQLiteOpenHelper {

    public static final String LOG_TAG = activity4.class.getSimpleName();


    private static final String DATABASE_NAME = "inventory.db";

    private static final double DATABASE_VERSION = 1;

    public activity4(Context context) {
        super(context, DATABASE_NAME, null, (int) DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + activity2.InventoryEntry.TABLE_NAME + " ("
                + activity2.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + activity2.InventoryEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + activity2.InventoryEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + activity2.InventoryEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + activity2.InventoryEntry.COLUMN_DEALER_NAME + " TEXT NOT NULL, "
                + activity2.InventoryEntry.COLUMN_DEALER_PHONE + " TEXT NOT NULL, "
                + activity2.InventoryEntry.COLUMN_DEALER_EMAIL + " TEXT NOT NULL, "
                + activity2.InventoryEntry.COLUMN_ITEM_IMAGE + " TEXT NOT NULL);";




        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
