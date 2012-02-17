package com.simplyapped.sayit.speech;

import java.util.HashMap;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;

public class Speech {
	private TextToSpeech mTts;
	private SharedPreferences preferences;
	
	public Speech(SharedPreferences preferences, TextToSpeech mTts)
	{
		this.mTts = mTts;
		this.preferences = preferences;
	}
	
	public void play(String text)
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
				String.valueOf(AudioManager.STREAM_MUSIC));
		
		int speed = preferences.getInt("speech_speed", 9);
		mTts.setSpeechRate((speed + 1) / 10f);
		int pitch = preferences.getInt("pitch", 9);
		mTts.setPitch((pitch + 1) / 10f);
		mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
}
