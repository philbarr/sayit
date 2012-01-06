package com.simplyapped.sayit;

import android.app.ListActivity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;

public class PhraseListActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	
}
