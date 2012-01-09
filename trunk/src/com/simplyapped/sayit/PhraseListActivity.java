package com.simplyapped.sayit;

import android.app.ListActivity;
import android.os.Bundle;

public class PhraseListActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.phraselist);
	}
}
