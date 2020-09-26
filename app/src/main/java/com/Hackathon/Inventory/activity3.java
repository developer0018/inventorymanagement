package com.Hackathon.Inventory;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class activity3 extends CursorAdapter {

    private final MainActivity activity;

    public activity3(MainActivity context, Cursor c) {
        super(context, c, 0);
        activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.all_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {


        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView price = (TextView) view.findViewById(R.id.price);
        final TextView quantity = (TextView) view.findViewById(R.id.quantity);
        ImageView sale = (ImageView) view.findViewById(R.id.sell);




        itemName.setText(cursor.getString(
                cursor.getColumnIndex(
                        activity2.InventoryEntry.COLUMN_ITEM_NAME
                )
        ));

        price.setText(String.valueOf(
                cursor.getInt(
                        cursor.getColumnIndex(
                                activity2.InventoryEntry.COLUMN_ITEM_PRICE
                        )
                ) + " Rs"
        ));

        int mQuantity = cursor.getInt(cursor.getColumnIndex(activity2.InventoryEntry.COLUMN_ITEM_QUANTITY));
        quantity.setText(String.valueOf(mQuantity));


        sale.setOnClickListener(new View.OnClickListener() {
            long clickedItemId = cursor.getLong(cursor.getColumnIndex(activity2.InventoryEntry._ID));
            Uri currentItemUri = ContentUris.withAppendedId(activity2.InventoryEntry.CONTENT_URI, clickedItemId);

            int clickedQuantity = cursor.getInt(cursor.getColumnIndex(activity2.InventoryEntry.COLUMN_ITEM_QUANTITY));
            @Override
            public void onClick(View view) {

                activity.update(currentItemUri, clickedQuantity, quantity);
            }
        });
    }
}
