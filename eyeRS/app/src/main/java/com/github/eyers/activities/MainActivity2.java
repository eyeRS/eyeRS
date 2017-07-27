package com.github.eyers.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity2 extends ListActivity {

    //We're adding these private variables so we can close the database and cursor in our onDestroy() method
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ListView listItems = getListView();

        try{
		
            SQLiteOpenHelper eyersDatabaseHelper = new EyeRSDatabaseHelper(this);
            db = eyersDatabaseHelper.getReadableDatabase();

            //Create the cursor
            cursor = db.query("ITEM",
                    new String[] {"_id", "IMAGE_RESOURCE_ID"},
                    "NAME", null, null, null, null);

            //Create the cursor adapter
            CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_expandable_list_item_1,
                    cursor,
                    new String[] {"NAME"},  //Map the contents of the NAME column to the text in the ListView
                    new int[]{android.R.id.text1},
                    0);

            //Cursor adapter
            listItems.setAdapter(listAdapter);
			
        }
        catch (SQLiteException ex) {

            //Display a message to the user if a SQLiteException gets thrown.
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //We're closing te database and cursor in the activity's onDestroy() method. 
    @Override
    public void onDestroy(){
	
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public  void onListItemClick(ListView listView,
                                 View itemView,
                                 int position,
                                 long id){
								 
        Intent intent = new Intent(MainActivity.this, ViewItemsActivity.class);
		
        //Add the ID of the item that was clicked to the intent
        intent.putExtra(NewItemActivity.EXTRA_ITEMNO, (int) id);
        startActivity(intent);
    }
}
