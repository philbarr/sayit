package com.simplyapped.sayit;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SayItActivity extends Activity implements OnClickListener, OnInitListener {
	private EditText messageText;
    private static final int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech mTts;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		messageText = (EditText) this.findViewById(R.id.messageText);
		this.findViewById(R.id.button1).setOnClickListener(this);
		
		
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}

	public void onClick(View v) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		int speed = sharedPrefs.getInt("speech_speed", 10);
		mTts.setSpeechRate((speed + 1) / 10f);
		int pitch = sharedPrefs.getInt("pitch", 10);
		mTts.setPitch((pitch + 1) / 10f);
		mTts.speak(messageText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
	}
	
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	    MenuItem item = menu.add("Options");
	    item.setIcon(R.drawable.options);
	    item.setIntent(new Intent(this,OptionsActivity.class));
	    return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.shutdown();
	}

	public void onInit(int arg0) {
		
	}
}