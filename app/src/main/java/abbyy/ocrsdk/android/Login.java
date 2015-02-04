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
import android.widget.ImageView;
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
import android.content.Intent;


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
public class Login extends Activity{
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
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.login);

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
        ImageView login = (ImageView) findViewById(R.id.imageView4);
        user = (EditText) findViewById(R.id.editText2);
        TextView tv25=(TextView) findViewById(R.id.textView25);
        pass = (EditText) findViewById(R.id.editText3);
        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/SinkinSans-100Thin.ttf");
        user.setTypeface(myCustomFont);
        pass.setTypeface(myCustomFont);
        tv25.setTypeface(myCustomFont);
        ImageView signup= (ImageView) findViewById(R.id.imageView8);//
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
                Intent i= new Intent("HOME");
                i.putExtra("user",user2);
                startActivity(i);
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
                        Intent i= new Intent("HOME");
                        i.putExtra("user",(String.valueOf(document.getProperties().get("email"))));
                        startActivity(i);
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
}
