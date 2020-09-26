package com.Hackathon.Inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 1;

    activity3 mCursorAdapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                activity2.InventoryEntry._ID,
                activity2.InventoryEntry.COLUMN_ITEM_IMAGE,
                activity2.InventoryEntry.COLUMN_ITEM_NAME,
                activity2.InventoryEntry.COLUMN_ITEM_PRICE,
                activity2.InventoryEntry.COLUMN_ITEM_QUANTITY};

        return new CursorLoader(this, activity2.InventoryEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, activity1.class);
                startActivity(intent);
            }
        });

        ListView inventoryListView  = (ListView)findViewById(R.id.list_view);

        View emptyView = findViewById(R.id.empty_inventory);
        inventoryListView.setEmptyView(emptyView);

        mCursorAdapter = new activity3(this, null);
        inventoryListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);


        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent =  new Intent(MainActivity.this, activity1.class);


                Uri currentItemUri = ContentUris.withAppendedId(activity2.InventoryEntry.CONTENT_URI, id);

                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:
                 showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete entire list?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getContentResolver().delete(activity2.InventoryEntry.CONTENT_URI, null, null);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void update(Uri currentItemUri, int mQuantity, TextView quantityView){

        int decreasedQuantity = 0;
        if(mQuantity > 0)
            decreasedQuantity = mQuantity - 1;

        ContentValues values = new ContentValues();
        values.put(activity2.InventoryEntry.COLUMN_ITEM_QUANTITY, decreasedQuantity);


        getContentResolver().update(currentItemUri, values, null, null);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        quantityView.startAnimation(animation);

    }
}

