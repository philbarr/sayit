package com.simplyapped.sayit;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.simplyapped.sayit.db.Database;
import com.simplyapped.sayit.speech.Speech;

public class SayItActivity extends Activity implements OnClickListener, OnInitListener {
	private EditText messageText;
	private static final int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech mTts;
	private Database database;
	private Speech speech;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		messageText = (EditText) this.findViewById(R.id.messageText);
		this.findViewById(R.id.button1).setOnClickListener(this);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		database = new Database(this);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}

	public void onClick(View v) {
		try {
			speech.play(messageText.getText().toString());
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem options = menu.add(R.string.options);
		options.setIcon(R.drawable.options);
		options.setIntent(new Intent(this, OptionsActivity.class));

		MenuItem save = menu.add(R.string.save);
		save.setIcon(R.drawable.save);
		save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				if (messageText.getText() != null && messageText.getText().length() > 0)
				{
					ContentValues values = new ContentValues();
					values.put(Database.COLUMN_PHRASES, messageText.getText().toString());
					database.getWritableDatabase().insert(Database.TABLE_PHRASES, null, values);
				}
				return true;
			}
		});

		MenuItem view = menu.add(R.string.view_phrase_list);
		view.setIcon(R.drawable.view);
		view.setIntent(new Intent(this, PhraseListActivity.class));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTts != null)
		{
			mTts.shutdown();
		}
	}

	public void onInit(int arg0) {

	}
}