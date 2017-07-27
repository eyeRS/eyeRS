package com.github.eyers.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewItemsActivity extends Activity {

    public static final String EXTRA_ITEMNO = "itemNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        //Get the item from the intent
        int itemNo = (Integer)getIntent().getExtras().get(EXTRA_ITEMNO);

        //Create a cursor
        try{
			
            SQLiteOpenHelper eyersDatabaseHelper = new EyeRSDatabaseHelper(this);
            SQLiteDatabase db = eyersDatabaseHelper.getReadableDatabase();
            
			Cursor cursor = db.query("BOOKS",
                    new String[] {"IMAGE_RESOURCE_ID", "NAME", "DESCRIPTION"},
                    "_id = ?", new String[] {Integer.toString(itemNo)}, null, null, null);
					
			Cursor cursor = db.query("CLOTHES",
                    new String[] {"IMAGE_RESOURCE_ID", "NAME", "DESCRIPTION"},
                    "_id = ?", new String[] {Integer.toString(itemNo)}, null, null, null);

			Cursor cursor = db.query("ACCESSORIES",
                    new String[] {"IMAGE_RESOURCE_ID", "NAME", "DESCRIPTION"},
                    "_id = ?", new String[] {Integer.toString(itemNo)}, null, null, null);
            
			Cursor cursor = db.query("GAMES",
                    new String[] {"IMAGE_RESOURCE_ID", "NAME", "DESCRIPTION"},
                    "_id = ?", new String[] {Integer.toString(itemNo)}, null, null, null);
			
			Cursor cursor = db.query("OTHER",
                    new String[] {"IMAGE_RESOURCE_ID", "NAME", "DESCRIPTION"},
                    "_id = ?", new String[] {Integer.toString(itemNo)}, null, null, null);
			
			//Move to the first record in the Cursor
            if (cursor.moveToFirst()){

                //Get the books details from the cursor
                int photoId = cursor.getInt(0);
				String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(2);
				
				//Get the clothes details from the cursor
                int photoId = cursor.getInt(0);
				String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(2);
                
				//Get the accessories details from the cursor
                int photoId = cursor.getInt(0);
				String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(2);
				
				//Get the games details from the cursor
                int photoId = cursor.getInt(0);
				String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(2);
				
				//Get the other details from the cursor
                int photoId = cursor.getInt(0);
				String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(2);

				//Populate the item image
                ImageView photo = (ImageView)findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);				
                
				//Populate the item name
                TextView name = (TextView)findViewById(R.id.name);
                name.setText(nameText);

                //Populate the item description
                TextView description = (TextView)findViewById(R.id.description);
                description.setText(descriptionText);

            }

            //Close the cursor and database
            cursor.close();
            db.close();
        }
        catch (SQLiteException ex){
			
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
			
        }
    }
}
