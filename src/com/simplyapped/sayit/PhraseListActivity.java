package com.simplyapped.sayit;

import com.simplyapped.sayit.db.Database;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class PhraseListActivity extends ListActivity {
	
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.phraselist);
		
        // Query for all people contacts using the Contacts.People convenience class.
        // Put a managed wrapper around the retrieved cursor so we don't have to worry about
        // requerying or closing it as the activity changes state.
        
		Database database = new Database(this);
		mCursor = database.getWritableDatabase().query(Database.TABLE_PHRASES, new String[]{Database.COLUMN_ID, Database.COLUMN_PHRASES}, null, null, null, null, null);
        startManagingCursor(mCursor);

        // Now create a new list adapter bound to the cursor.
        // SimpleListAdapter is designed for binding to a Cursor.
        ListAdapter adapter =  new SimpleCursorAdapter(
                this, 
                R.layout.rowlayout,
                mCursor,                                              // Pass in the cursor to bind to.
                new String[] {Database.COLUMN_PHRASES},           // Array of cursor columns to bind to.
                new int[] {R.id.phrase});  // Parallel array of which template objects to bind to those columns.

        // Bind to our new adapter.
        setListAdapter(adapter);

	}
}
