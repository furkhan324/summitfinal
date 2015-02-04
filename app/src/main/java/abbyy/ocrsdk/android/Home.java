package abbyy.ocrsdk.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.TextView;
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

/**
 * Created by mohammed on 1/23/15.
 */
public class Home extends Activity{
    private final int TAKE_PICTURE = 0;
    private final int SELECT_FILE = 1;
 TextView title;

    TextView textView14;
    TextView tv15;
    TextView tv16;
    TextView tv17;
    TextView tv18;
    TextView tv19;
    TextView tv37;
    TextView tv38;
    TextView tv39;
    TextView tv40;
    TextView tv41;
    private String resultUrl = "result.txt";

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.home2);
        title =(TextView) findViewById(R.id.textView26);
        tv15 =(TextView) findViewById(R.id.textView32);
        tv16 =(TextView) findViewById(R.id.textView33);
        tv17 =(TextView) findViewById(R.id.textView34);
        tv18 =(TextView) findViewById(R.id.textView35);
        tv19 =(TextView) findViewById(R.id.textView36);
tv37=(TextView) findViewById(R.id.textView37);
        tv38=(TextView) findViewById(R.id.textView38);
        tv39=(TextView) findViewById(R.id.textView39);
        tv40=(TextView) findViewById(R.id.textView40);
        tv41=(TextView) findViewById(R.id.textView41);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/SinkinSans-300Light.ttf");

        Typeface myCustomFont2=Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");
        tv15.setTypeface(myCustomFont);
        tv16.setTypeface(myCustomFont);
        tv17.setTypeface(myCustomFont);
        tv18.setTypeface(myCustomFont);
        tv19.setTypeface(myCustomFont);
        tv37.setTypeface(myCustomFont);
        tv38.setTypeface(myCustomFont);
        tv39.setTypeface(myCustomFont);
        tv40.setTypeface(myCustomFont);
        tv41.setTypeface(myCustomFont);

title.setTypeface(myCustomFont2);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    public void captureImageFromSdCard( View view ) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, SELECT_FILE);
    }

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ABBYY Cloud OCR SDK Demo App");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg" );

        return mediaFile;
    }

    public void captureImageFromCamera( View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        String imageFilePath = null;

        switch (requestCode) {
            case TAKE_PICTURE:
                imageFilePath = getOutputMediaFileUri().getPath();
                break;
            case SELECT_FILE: {
                Uri imageUri = data.getData();

                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cur = managedQuery(imageUri, projection, null, null, null);
                cur.moveToFirst();
                imageFilePath = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            break;
        }

        //Remove output file
        deleteFile(resultUrl);

        Intent results = new Intent( this, ResultsActivity.class);
        results.putExtra("IMAGE_PATH", imageFilePath);
        results.putExtra("RESULT_PATH", resultUrl);
        startActivity(results);
    }
}