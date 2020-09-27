package com.Hackathon.Inventory;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class activity1 extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText itemName;
    private EditText itemPrice;
    private EditText itemQuantity;
    private EditText dealerName;
    private EditText dealerPhone;
    private EditText dealerEmail;
    private Button plusButton;
    private Button minusButton;
    private Button addImageButton;
    private ImageView productImage;
    private Button Multiply;
    private EditText Result;

    String [] items = {""};//Add your items(optional)
    private String picturePath = "";

    private static int RESULT_LOAD_IMAGE = 1;

    private static final int EXISTING_ITEM_LOADER = 0;

    private boolean mItemHasChanged = false;


    private Uri mCurrentItemUri;


    private static String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();
        if(mCurrentItemUri == null) {
            setTitle("Add Item");
        }
        else {
            setTitle("Edit Item");
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }

        itemName = (EditText)findViewById(R.id.product_name);
        itemPrice = (EditText)findViewById(R.id.product_price);
        itemQuantity = (EditText)findViewById(R.id.item_quantity);
        dealerName = (EditText)findViewById(R.id.dealer_name);
        dealerPhone = (EditText)findViewById(R.id.dealer_phone);
        dealerEmail = (EditText)findViewById(R.id.dealer_email);
        plusButton = (Button)findViewById(R.id.plus);
        minusButton = (Button)findViewById(R.id.minus);
        addImageButton = (Button)findViewById(R.id.add_image_button);
        productImage = (ImageView)findViewById(R.id.product_image);
        Multiply = (Button) findViewById(R.id.tt);
        Result = (EditText) findViewById(R.id.tot);




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,items);
        AutoCompleteTextView ac = (AutoCompleteTextView) findViewById(R.id.product_name);
        ac.setThreshold(1);
ac.setAdapter(adapter);
ac.setTextColor(Color.BLACK);




        itemName.setOnTouchListener(mTouchListener);
        itemPrice.setOnTouchListener(mTouchListener);
        itemQuantity.setOnTouchListener(mTouchListener);
        dealerName.setOnTouchListener(mTouchListener);
        dealerPhone.setOnTouchListener(mTouchListener);
        dealerEmail.setOnTouchListener(mTouchListener);
        plusButton.setOnTouchListener(mTouchListener);
        minusButton.setOnTouchListener(mTouchListener);
        addImageButton.setOnTouchListener(mTouchListener);


    Multiply.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Double a, b, c;
                                        try {
                                            a = Double.parseDouble(itemPrice.getText().toString());
                                            b = Double.parseDouble(itemQuantity.getText().toString());
                                            c = a * b;
                                            Result.setText(Double.toString(c));
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(activity1.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }
    );






        itemQuantity.addTextChangedListener(new TextValidator(itemQuantity) {
            @Override
            public void validate(TextView textView, String text) {

                if(!text.isEmpty()) {
                    if (Integer.parseInt(text) > 1000000000 || Integer.parseInt(text) < 0) {
                        itemQuantity.setError("Quantity Exceeded");
                        itemQuantity.setText("0");
                    }
                }
            }
        });


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemQuantity.setError(null);
                String currentQuantity = itemQuantity.getText().toString().trim();

                if (currentQuantity.isEmpty()){
                    itemQuantity.setText("1");
                }
                else{
                    int currentItemQuantity = Integer.parseInt(currentQuantity);
                    itemQuantity.setText(String.valueOf(currentItemQuantity + 1));
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemQuantity.setError(null);
                String currentQuantity = itemQuantity.getText().toString().trim();

                    if(!currentQuantity.isEmpty()) {
                        int currentItemQuantity = Integer.parseInt(currentQuantity);
                        itemQuantity.setText(String.valueOf(currentItemQuantity - 1));
                    }
                    else
                        itemQuantity.setText("0");
            }
        });



        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(activity1.this, PERMISSIONS_STORAGE, 1);
            }
        });

    }



    private void saveItem(){

        String itemNameString = itemName.getText().toString().trim();
        String itemPriceString = itemPrice.getText().toString().trim();
        String itemQuantityString = itemQuantity.getText().toString().trim();
        String supplierNameString = dealerName.getText().toString().trim();
        String supplierPhoneString = dealerPhone.getText().toString().trim();
        String supplierEmailString = dealerEmail.getText().toString().trim();

        boolean status = formValidate(itemNameString, itemPriceString, itemQuantityString, supplierNameString,
                supplierPhoneString, supplierEmailString);

        if(!status)
            return;

        ContentValues values = new ContentValues();
        values.put(activity2.InventoryEntry.COLUMN_ITEM_NAME, itemNameString);

        int itemPriceInt = Integer.parseInt(itemPriceString);
        values.put(activity2.InventoryEntry.COLUMN_ITEM_PRICE, itemPriceInt);

        int itemQuantityInt = Integer.parseInt(itemQuantityString);
        values.put(activity2.InventoryEntry.COLUMN_ITEM_QUANTITY, itemQuantityInt);

        values.put(activity2.InventoryEntry.COLUMN_DEALER_NAME, supplierNameString);
        values.put(activity2.InventoryEntry.COLUMN_DEALER_PHONE, supplierPhoneString);
        values.put(activity2.InventoryEntry.COLUMN_DEALER_EMAIL, supplierEmailString);
        values.put(activity2.InventoryEntry.COLUMN_ITEM_IMAGE, picturePath);

        if(mCurrentItemUri == null){
            Uri newUri = getContentResolver().insert(activity2.InventoryEntry.CONTENT_URI, values);

            if(newUri == null)
                Toast.makeText(this, "Error while saving new Item", Toast.LENGTH_SHORT);
            else
                Toast.makeText(this, "New Item added", Toast.LENGTH_SHORT);
        }
        else {
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
            if (rowsAffected == 0)
                Toast.makeText(this,"Error with updating Item",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Item Updated",Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteItem() {

        if (mCurrentItemUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            if (rowsDeleted == 0)
                Toast.makeText(this, "Error with deleting Item", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                activity2.InventoryEntry.COLUMN_ITEM_NAME,
                activity2.InventoryEntry.COLUMN_ITEM_PRICE,
                activity2.InventoryEntry.COLUMN_ITEM_QUANTITY,
                activity2.InventoryEntry.COLUMN_DEALER_NAME,
                activity2.InventoryEntry.COLUMN_DEALER_PHONE,
                activity2.InventoryEntry.COLUMN_DEALER_EMAIL,
                activity2.InventoryEntry.COLUMN_ITEM_IMAGE};


        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1)
            return;


        if (cursor.moveToFirst()) {
            itemName.setText(cursor.getString(
                    cursor.getColumnIndex(
                            activity2.InventoryEntry.COLUMN_ITEM_NAME
                    )
            ));

            itemPrice.setText(String.valueOf(
                    cursor.getInt(
                            cursor.getColumnIndex(
                                    activity2.InventoryEntry.COLUMN_ITEM_PRICE
                            )
                    )
            ));

            itemQuantity.setText(String.valueOf(
                    cursor.getInt(
                            cursor.getColumnIndex(
                                    activity2.InventoryEntry.COLUMN_ITEM_QUANTITY
                            )
                    )
            ));

            dealerName.setText(cursor.getString(
                    cursor.getColumnIndex(
                            activity2.InventoryEntry.COLUMN_DEALER_NAME
                    )
            ));

            dealerPhone.setText(cursor.getString(
                    cursor.getColumnIndex(
                            activity2.InventoryEntry.COLUMN_DEALER_PHONE
                    )
            ));

          dealerEmail.setText(cursor.getString(
                    cursor.getColumnIndex(
                            activity2.InventoryEntry.COLUMN_DEALER_EMAIL
                    )
            ));

            picturePath = cursor.getString(
                    cursor.getColumnIndex(activity2.InventoryEntry.COLUMN_ITEM_IMAGE
                    )
            );
            productImage.setImageURI(Uri.parse(picturePath));

            addImageButton.setError(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        itemName.setText("");
        itemPrice.setText("");
        itemQuantity.setText("");
       dealerName.setText("");
      dealerEmail.setText("");
       dealerPhone.setText("");
        productImage.setImageResource(R.drawable.img);
        productImage.setTag(R.drawable.img);
        picturePath = "";
        addImageButton.setError(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {

                    Toast.makeText(activity1.this, "Permission denied to read your Int/Ext storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            productImage.setImageURI(Uri.parse(picturePath));

            addImageButton.setError(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                saveItem();
                return true;

            case R.id.action_delete_item:
                showDeleteConfirmationDialog();
                return true;

            case R.id.order_more:
                showOrderDialog();
                return true;


            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(activity1.this);
                    return true;
                }
                showUnsavedChangesDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void showOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Option to place your order");

        builder.setPositiveButton("Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {dealerEmail.getText().toString().trim()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Items Required");
                intent.putExtra(Intent.EXTRA_TEXT, "Please send additional" + " " + itemQuantity.getText().toString().trim() + " " + itemName.getText().toString().trim());
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
            }
        });

        builder.setNegativeButton("Phone", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dealerPhone.getText().toString().trim()));
                startActivity(intent);
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        showUnsavedChangesDialog();
    }

    private void showUnsavedChangesDialog( ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit or edit?");

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Item?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_item);
            menuItem.setVisible(false);

            menuItem = menu.findItem(R.id.order_more);
            menuItem.setVisible(false);
        }
        return true;
    }

    public void kk(View view) {
        Toast.makeText(getApplicationContext(),"Team:Developer\nInnovate for Assam\nOnline Hackathon",Toast.LENGTH_LONG).show();
    }



    public abstract class TextValidator implements TextWatcher {
        private final TextView textView;
        public TextValidator(TextView textView) {
            this.textView = textView;
        }

        public abstract void validate(TextView textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = textView.getText().toString().trim();
            validate(textView, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) {  }
    }


    boolean formValidate(String itemNameString, String itemPriceString, String itemQuantityString,
                      String supplierNameString, String supplierPhoneString, String supplierEmailString) {

        boolean status = true;

        if(itemNameString.equals("")){
            status = false;
            itemName.setError("Item Name required");
        }

        if(itemPriceString.equals("")){
            status = false;
            itemPrice.setError("Item Price required");
        }

        if(itemQuantityString.equals("")){
            status = false;
            itemQuantity.setError("Item Quantity required");
        }

        if(supplierNameString.equals("")){
            status = false;
           dealerName.setError("Dealer Name required");
        }

        if(supplierPhoneString.equals("")){
            status = false;
           dealerPhone.setError("Dealer Phone required");
        }

        if(supplierEmailString.equals("")){
            status = false;
            dealerEmail.setError("Dealer Email required");
        }

        if(picturePath.equals("")){
            status = false;
            addImageButton.setError("Select an image");
        }

        return status;
    }
}

