package abbyy.ocrsdk.android;

import java.io.FileOutputStream;

import com.abbyy.ocrsdk.*;

import android.app.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import java.io.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;

public class AsyncProcessTask extends AsyncTask<String, String, Boolean> {

	public AsyncProcessTask(ResultsActivity activity) {
		this.activity = activity;
		dialog = new ProgressDialog(activity);
	}

	private ProgressDialog dialog;
	/** application context. */
	private final ResultsActivity activity;

	protected void onPreExecute() {

		dialog.setMessage("Processing");
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	protected void onPostExecute(Boolean result) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		
		activity.updateResults(result);
	}

	@Override
	protected Boolean doInBackground(String... args) {

		String inputFile = args[0];
		String outputFile = args[1];

		try {
			Client restClient = new Client();


			restClient.applicationId = "summitapp";
			// You should get e-mail from ABBYY Cloud OCR SDK service with the application password
			restClient.password = "SbcwRIoGJUCyMn4aCWVynYpS";
			// Obtain installation id when running the application for the first time
			SharedPreferences settings = activity.getPreferences(Activity.MODE_PRIVATE);
			String instIdName = "installationId";
			if( !settings.contains(instIdName)) {
				// Get installation id from server using device id
				String deviceId = android.provider.Settings.Secure.getString(activity.getContentResolver(), 
						android.provider.Settings.Secure.ANDROID_ID);
				
				// Obtain installation id from server
				publishProgress( "First run: obtaining installation id..");
				String installationId = restClient.activateNewInstallation(deviceId);
				publishProgress( "Done. Installation id is '" + installationId + "'");
				
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(instIdName, installationId);
				editor.commit();
			} 
			
			String installationId = settings.getString(instIdName, "");
			restClient.applicationId += installationId;
			
			publishProgress( "Uploading image...");
			
			String language = "English"; // Comma-separated list: Japanese,English or German,French,Spanish etc.
			
			ProcessingSettings processingSettings = new ProcessingSettings();
			processingSettings.setOutputFormat( ProcessingSettings.OutputFormat.txt );
			processingSettings.setLanguage(language);
			
			publishProgress("Uploading..");

			// If you want to process business cards, uncomment this
			/*
			BusCardSettings busCardSettings = new BusCardSettings();
			busCardSettings.setLanguage(language);
			busCardSettings.setOutputFormat(BusCardSettings.OutputFormat.xml);
			Task task = restClient.processBusinessCard(filePath, busCardSettings);
			*/
			Task task = restClient.processImage(inputFile, processingSettings);
			
			while( task.isTaskActive() ) {
				// Note: it's recommended that your application waits
				// at least 2 seconds before making the first getTaskStatus request
				// and also between such requests for the same task.
				// Making requests more often will not improve your application performance.
				// Note: if your application queues several files and waits for them
				// it's recommended that you use listFinishedTasks instead (which is described
				// at http://ocrsdk.com/documentation/apireference/listFinishedTasks/).

				Thread.sleep(5000);
				publishProgress( "Waiting.." );
				task = restClient.getTaskStatus(task.Id);
			}
			
			if( task.Status == Task.TaskStatus.Completed ) {
				publishProgress( "Downloading.." );
				FileOutputStream fos = activity.openFileOutput(outputFile,Context.MODE_PRIVATE);
				
				try {
					restClient.downloadResult(task, fos);
				} finally {
					fos.close();
				}
				
				publishProgress( "Ready" );
			} else if( task.Status == Task.TaskStatus.NotEnoughCredits ) {
				throw new Exception( "Not enough credits to process task. Add more pages to your application's account." );
			} else {
				throw new Exception( "Task failed" );
			}
			
			return true;
		} catch (Exception e) {
			final String message = "Error: " + e.getMessage();
			publishProgress( message);
			activity.displayMessage(message);
			return false;
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		String stage = values[0];
		dialog.setMessage(stage);
		// dialog.setProgress(values[0]);
	}

}
