package abbyy.ocrsdk.android;


import java.io.ByteArrayOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import api.AlchemyAPI;
import api.AlchemyAPI_ImageParams;
import api.AlchemyAPI_NamedEntityParams;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    public String[] keywords;
    public String[] sentences;
    public String[] points;
    TextView tv;
    /****
     *
     *
     * Put your API Key into the variable below.  Can get key from http://www.alchemyapi.com/api/register.html
     */
    public String AlchemyAPI_Key = "174d183a55d90c6baecf14db72410304726911d3";
    public String text;
    public TextView tv7;

    public TextView tv8;public TextView tv9;public TextView tv10;public TextView tv11;public TextView tv12;
    public TextView tv13;
    public TextView title;
    public TextView kw1;
    public TextView kw2;
    public TextView kw3;
    public TextView kw4;
    public TextView kw5;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        keywords= new String[100];
        points=new String[100];
        sentences=new String[100];
        text  = intent.getStringExtra("text");


kw1= (TextView) findViewById(R.id.kw1);

        kw1= (TextView) findViewById(R.id.kw1);

        kw2= (TextView) findViewById(R.id.kw2);
        kw3= (TextView) findViewById(R.id.kw3);
        kw4= (TextView) findViewById(R.id.kw4);
        kw5= (TextView) findViewById(R.id.kw5);
title =(TextView) findViewById(R.id.title);
Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/SinkinSans-100Thin.ttf");
        title.setTypeface(myCustomFont);
        tv7=(TextView) findViewById(R.id.textView7);
        tv8=(TextView) findViewById(R.id.textView8);
        tv9=(TextView) findViewById(R.id.textView9);
        tv10=(TextView) findViewById(R.id.textView10);
tv11=(TextView) findViewById(R.id.textView11);
        tv12=(TextView) findViewById(R.id.textView12);
        tv13=(TextView) findViewById(R.id.textView13);

                SendAlchemyCall("keyword");




    }

    private void SendAlchemyCall(final String call)
    {
        Thread thread = new Thread(new Runnable(){

            public void run() {
                try {
                    SendAlchemyCallInBackground(call);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void SendAlchemyCallInBackground(final String call)
    {
        runOnUiThread(new Runnable() {

            public void run() {
               //
               // tv.setText("Making call: "+call);
            }
        });

        Document doc = null;
        AlchemyAPI api = null;
        try
        {
            api = AlchemyAPI.GetInstanceFromString(AlchemyAPI_Key);
        }
        catch( IllegalArgumentException ex )
        {
           // tv.setText("Error loading AlchemyAPI.  Check that you have a valid AlchemyAPI key set in the AlchemyAPI_Key variable.  Keys available at alchemyapi.com.");
            return;
        }

        String someString = "hello";
        try{
            if( "concept".equals(call) )
            {
                doc = api.URLGetRankedConcepts(someString);
                ShowDocInTextView(doc, false);
            }
            else if( "entity".equals(call))
            {
                doc = api.URLGetRankedNamedEntities(someString);
                ShowDocInTextView(doc, false);
            }
            else if( "keyword".equals(call))
            {

                doc=api.TextGetRankedKeywords(text);
                ShowDocInTextView(doc, false);
            }
            else if( "text".equals(call))
            {
                doc = api.URLGetText(someString);
                ShowDocInTextView(doc, false);
            }
            else if( "sentiment".equals(call))
            {
                AlchemyAPI_NamedEntityParams nep = new AlchemyAPI_NamedEntityParams();
                nep.setSentiment(true);
                doc = api.URLGetRankedNamedEntities(someString, nep);
                ShowDocInTextView(doc, true);
            }
            else if( "taxonomy".equals(call))
            {
                doc = api.URLGetTaxonomy(someString);
                ShowTagInTextView(doc, "label");
            }
            else if( "image".equals(call))
            {
                doc = api.URLGetImage(someString);
                ShowTagInTextView(doc, "image");
            }
            else if( "combined".equals(call))
            {
                doc = api.URLGetCombined(someString);
                ShowDocInTextView(doc, false);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
          //  tv.setText("Error: " + e.getMessage());
        }
    }

    private void ShowTagInTextView(final Document doc, final String tag)
    {
        Log.d(getString(R.string.app_name), doc.toString());
        runOnUiThread(new Runnable() {

            public void run() {
              //  tv.setText("Tags: \n");
                Element root = doc.getDocumentElement();
                NodeList items = root.getElementsByTagName(tag);
                for (int i=0;i<items.getLength();i++) {
                    Node concept = items.item(i);
                    String astring = concept.getNodeValue();
                    astring = concept.getChildNodes().item(0).getNodeValue();
                 //   tv.append("\n" + astring);
                }
            }
        });
    }

    private void ShowDocInTextView(final Document doc, final boolean showSentiment)
    {
        runOnUiThread(new Runnable() {

            public void run() {
             //   tv.setText("");
                if( doc == null )
                {
                    return;
                }

                Element root = doc.getDocumentElement();
                NodeList items = root.getElementsByTagName("text");
                if( showSentiment )
                {
                    NodeList sentiments = root.getElementsByTagName("sentiment");
                    for (int i=0;i<items.getLength();i++){
                        Node concept = items.item(i);
                        String astring = concept.getNodeValue();
                        astring = concept.getChildNodes().item(0).getNodeValue();
                     //   tv.append("\n" + astring);
                        if( i < sentiments.getLength() )
                        {
                            Node sentiment = sentiments.item(i);
                            Node aNode = sentiment.getChildNodes().item(1);
                            Node bNode = aNode.getChildNodes().item(0);
                       //     tv.append(" (" + bNode.getNodeValue()+")");
                        }
                    }
                }
                else
                {
                    for (int i=0;i<items.getLength();i++) {
                        Node concept = items.item(i);
                        String astring = concept.getNodeValue();
                        astring = concept.getChildNodes().item(0).getNodeValue();
                        keywords[i]=astring;
                        Log.d("TAG","These are the key words" + astring);
                    //    tv.append("\n" + astring);
                    }kw1.setText(keywords[0]);
                    kw2.setText(keywords[1]);
                    kw3.setText(keywords[2]);
                    kw4.setText(keywords[3]);
                    kw5.setText(keywords[4]);

                    sentences=text.split("\\.",100);
                    tv7.setText("&#8226" +sentences[0]);
                    tv8.setText(sentences[1]);
                    tv9.setText(sentences[2]);
                    tv10.setText(sentences[3]);
                    tv11.setText(sentences[4]);
                    tv12.setText(sentences[5]);
                    if(sentences.length==7){
                    tv13.setText(sentences[6]);}
                    title.setText(keywords[0]);
                    for(int a=0;a<sentences.length;a++){
                        Log.d("TAG","These are the sentence"+ sentences[a]);
                    }


                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            Uri selectedImage = imageReturnedIntent.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            try
            {
                Bitmap imgBitmap = getScaledBitmap(picturePath, 400, 400);

                // Images from the filesystem might be rotated...
                ExifInterface exif = new ExifInterface(picturePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                Log.d(getString(R.string.app_name), "Orientation: "+orientation);

                switch (orientation) {
                    case 3:
                    {
                        Matrix matrix = new Matrix();

                        matrix.postRotate(90);
                        imgBitmap = Bitmap.createBitmap(
                                imgBitmap, 0, 0, imgBitmap.getWidth(), imgBitmap.getHeight(), matrix, true);
                    }
                    break;
                    case 6:
                    {
                        Matrix matrix = new Matrix();

                        matrix.postRotate(90);
                        imgBitmap = Bitmap.createBitmap(
                                imgBitmap, 0, 0, imgBitmap.getWidth(), imgBitmap.getHeight(), matrix, true);
                    }
                    break;

                }


            }
            catch (Exception e)
            {
               // tv.setText("Error loading image: " + e.getMessage());
            }
        }
    }

    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

}

/*package abbyy.ocrsdk.android;

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



public class MainActivity extends Activity {

	private final int TAKE_PICTURE = 0;
	private final int SELECT_FILE = 1; 
	
	private String resultUrl = "result.txt";
    String username;
    String password;
    int count;

    EditText user;
    EditText pass;
    Manager manager;
    Database database;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
        /*
        final String TAG = "HelloWorld";
        Log.d(TAG, "Begin Hello World App");
        // create a manager

        try {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            Log.d (TAG, "Manager created");
        } catch (IOException e) {
            Log.e(TAG, "Cannot create manager object");
            return;
        }
        // create a name for the database and make sure the name is legal
        String dbname = "users";
        if (!Manager.isValidDatabaseName(dbname)) {
            Log.e(TAG, "Bad database name");
            return;
        }
// create a new database

        try {
            database = manager.getDatabase(dbname);
            Log.d (TAG, "Database created or  accessed");
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get database");
            return;
        }


// display the retrieved document

        this.count=database.getDocumentCount();
        Button login = (Button) findViewById(R.id.login);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);

        Button signup= (Button) findViewById(R.id.signup);//
//------------------------------------------------------------------
//------------------------------------------------------------------
//------------------------------------------------------------------
        signup.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String user2 = user.getText().toString();
                String pass2 = pass.getText().toString();
                Map<String, Object> docContent = new HashMap<String, Object>();
                docContent.put("email", user2);
                docContent.put("password", pass2);
                Document document = database.getDocument(user2);
//test if document already exists
                try{
                    Log.d(TAG, "document=" + String.valueOf(document.getProperties().get("email")));
                    Toast.makeText(getApplicationContext(),"This email has already been taken", Toast.LENGTH_LONG).show();
                    return;
                }catch(Exception e){

                }
// add content to document and write the document to the database
                try {
                    document.putProperties(docContent);
                    Log.d (TAG, "Document written to database named " + "users" + " with ID = " + document.getId());
                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Cannot write document to database", e);
                }
// save the ID of the new document
                String docID = document.getId();
                Toast.makeText(getApplicationContext(),"Your account has successfully been created as the "+" "+count+"th user.", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"username: "+user2+" and pw: "+pass2, Toast.LENGTH_LONG).show();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////

        login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String user2 = user.getText().toString();
                String pass2 = pass.getText().toString();
                Map<String, Object> docContent = new HashMap<String, Object>();
                docContent.put("email", user2);
                docContent.put("password", pass2);
                Document document = database.getDocument(user2);
//test if document already exists
                try{
                    Log.d(TAG, "document=" + String.valueOf(document.getProperties().get("email")));


                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"This email is not registered. You may want to sign up instead.", Toast.LENGTH_LONG).show();
                    try{
                        database.deleteLocalDocument(user2);}
                    catch(Throwable t){
                        Toast.makeText(getApplicationContext(),"Error, document was not deleted!", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
// add content to document and write the document to the database
                try {
                    Log.d(TAG, (String.valueOf(document.getProperties().get("password")))+": THIS IS THE VALUE OF THE PASSWORD");
                    if((String.valueOf(document.getProperties().get("password"))).equals(pass2)){
                        Toast.makeText(getApplicationContext(),"You have logged in successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Incorrect Email and pw Combination", Toast.LENGTH_LONG).show();
                    }

                } catch (Throwable t) {

                }
// save the ID of the new document

            }
        });






        Log.d(TAG, "End Hello World App");
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

	/** Create a File for saving an image or video
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
    
}*/
