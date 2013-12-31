package com.simplyapped.sayit;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.simplyapped.sayit.alert.ErrorMessage;
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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		messageText = (EditText) this.findViewById(R.id.messageText);
		messageText.requestFocus();
		this.findViewById(R.id.button1).setOnClickListener(this);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		database = new Database(this);

	    // Look up the AdView as a resource and load a request.
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);

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
							R.string.error_unable_to_talk,
							Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				mTts = new TextToSpeech(this, this);
				SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
				speech = new Speech(defaultSharedPreferences, mTts);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem clear = menu.add(R.string.menu_clear);
		clear.setIcon(R.drawable.refresh);
		clear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try {
					if (messageText.getText() != null)
					{
						messageText.setText("");
					}
				} catch (Exception e) {
					new ErrorMessage(SayItActivity.this, "Error", getString(R.string.error_failed_to_clear),e).logAndDisplay();
				}
				return true;
			}
		});
		
		MenuItem options = menu.add(R.string.options);
		options.setIcon(R.drawable.options);
		options.setIntent(new Intent(this, OptionsActivity.class));

		MenuItem save = menu.add(R.string.save);
		save.setIcon(R.drawable.save);
		save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				if (messageText.getText() != null && messageText.getText().length() > 0)
				{
					try
					{
						ContentValues values = new ContentValues();
						values.put(Database.COLUMN_PHRASES, messageText.getText().toString());
						database.getWritableDatabase().insert(Database.TABLE_PHRASES, null, values);
						Toast toast = Toast
								.makeText(
										SayItActivity.this,
										R.string.message_saved_success,
										Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} catch (Exception e)
					{
						new ErrorMessage(SayItActivity.this, "Error", getString(R.string.error_failed_to_insert_phrase),e).logAndDisplay();
					}
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
	protected void onResume() {
		super.onResume();
		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		int fontSize = defaultSharedPreferences.getInt("font_size", 16);
		messageText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSize);
	}
	
	@Override
	protected void onDestroy() {
	    if(mTts != null) {
	        mTts.stop();
	        mTts.shutdown();
	        Log.d(SayItActivity.class.getName(), "TTS Destroyed");
	    }
	    super.onDestroy();
	}

	public void onInit(int arg0) {

	}
}