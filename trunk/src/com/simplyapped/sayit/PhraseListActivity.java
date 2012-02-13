package com.simplyapped.sayit;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.simplyapped.sayit.db.Database;
import com.simplyapped.sayit.speech.Speech;

public class PhraseListActivity extends ListActivity implements OnInitListener {
	private static final int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech mTts;
	private Database database;
	private Speech speech;
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.phraselist);
		registerForContextMenu(getListView());
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		database = new Database(this);

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
        
        Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				mTts = new TextToSpeech(this, this);
				speech = new Speech(PreferenceManager.getDefaultSharedPreferences(this), mTts);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		final long itemId = getListView().getItemIdAtPosition(info.position);
		MenuItem delete = menu.add(R.string.delete);
		delete.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				SQLiteDatabase db = database.getWritableDatabase();
				db.delete(Database.TABLE_PHRASES, Database.COLUMN_ID + "=?", new String[]{String.valueOf(itemId)});
				mCursor.requery();
				return true;
			}
		});
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		try {
			long itemId = getListView().getItemIdAtPosition(position);
			Cursor cursor = database.getWritableDatabase().query(Database.TABLE_PHRASES, new String[]{Database.COLUMN_PHRASES}, "_id=?", new String[]{String.valueOf(itemId)}, null, null, null);
			cursor.moveToFirst();
			String text = cursor.getString(0);
			cursor.close();
			speech.play(text);
		} catch (Exception e) {
			Toast toast = Toast
					.makeText(
							this,
							"Unable to talk! Please allow the Text To Speech Engine to install from Android Market and then restart this application",
							Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
		}
	}
	
	@Override
	public void onInit(int arg0) {
	}
}
