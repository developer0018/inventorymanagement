package com.Hackathon.Inventory;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class activity2 {


    private activity2() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.inventory_mgt";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";

    public static final class InventoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        public final static String TABLE_NAME = "inventory";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_ITEM_NAME ="name";
        public final static String COLUMN_ITEM_PRICE = "price";
        public final static String COLUMN_ITEM_QUANTITY = "quantity";
        public final static String COLUMN_DEALER_NAME = "dealer";
        public final static String COLUMN_DEALER_PHONE = "phone";
        public final static String COLUMN_DEALER_EMAIL = "email";
        public final static String COLUMN_ITEM_IMAGE = "image";




        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;


        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
    }
}
