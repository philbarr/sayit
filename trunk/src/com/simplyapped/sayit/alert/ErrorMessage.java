package com.simplyapped.sayit.alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class ErrorMessage
{

	private Context activity;
	private String message;
	private String title;
	private Exception exception;

	public ErrorMessage(Context activity, String title, String message)
	{
		super();
		this.activity = activity;
		this.message = message;
		this.title = title;
	}

	public ErrorMessage(Context activity, String title, String message, Exception exception)
	{
		super();
		this.activity = activity;
		this.message = message;
		this.title = title;
		this.exception = exception;
	}

	public void logAndDisplay()
	{
		if (exception == null)
		{
			Log.e(activity.getClass().getName(), message);
		}
		else
		{
			Log.e(activity.getClass().getName(), message, exception);
		}
		new AlertDialog.Builder(activity)
				.setMessage(message)
				.setTitle(title)
				.setCancelable(true)
				.setNeutralButton(android.R.string.cancel,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
							}
						}).show();
	}
}
