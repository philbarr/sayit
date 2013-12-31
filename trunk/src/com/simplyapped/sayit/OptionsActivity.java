package com.simplyapped.sayit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.simplyapped.sayit.preferences.SeekBarPreference;

public class OptionsActivity extends PreferenceActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int streamMaxVolume = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int streamVolume = audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		Editor editor = sharedPrefs.edit();
		editor.putInt("volume", streamVolume);
		editor.commit();

		addPreferencesFromResource(R.layout.options);
		
		SeekBarPreference pref = (SeekBarPreference) this.findPreference("volume");
		pref.setMax(streamMaxVolume);
		pref.setProgress(streamVolume);

		pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						Integer.parseInt(newValue + ""),
						AudioManager.FLAG_PLAY_SOUND);
				return false;
			}
		});
	}
}
