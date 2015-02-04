package abbyy.ocrsdk.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by mohammed on 1/23/15.
 */

public class Loading extends Activity{
    TextView tv42;
    TextView tv43;
    TextView tv44;
    TextView tv45;
    TextView tv46;
    TextView tv47;
    TextView tv48;
    TextView tv49;
    TextView tv50;
    TextView tv51;
    TextView tv52;
    TextView tv57;
    private ProgressBar progressBar;
    private int progressStatus = 0;

    private Handler handler = new Handler();


    /*  @Override
     protected void onCreate(Bundle b){
          super.onCreate(b);

          setContentView(R.layout.splash);
          /*tv42 =(TextView) findViewById(R.id.textView42);
          tv43 =(TextView) findViewById(R.id.textView43);
          tv44=(TextView) findViewById(R.id.textView44);
          tv45=(TextView) findViewById(R.id.textView45);
          tv46=(TextView) findViewById(R.id.textView46);
          tv47=(TextView) findViewById(R.id.textView47);
          tv48=(TextView) findViewById(R.id.textView48);
          tv49=(TextView) findViewById(R.id.textView49);
          tv50=(TextView) findViewById(R.id.textView50);
          tv51=(TextView) findViewById(R.id.textView51);
          tv52=(TextView) findViewById(R.id.textView52);
          tv53=(TextView) findViewById(R.id.textView53);
          Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/SinkinSans-300Light.ttf");
          Typeface myCustomFont3=Typeface.createFromAsset(getAssets(),"fonts/SinkinSans-500Medium.ttf");
          Typeface myCustomFont2=Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");

          tv42.setTypeface(myCustomFont2);
          tv44.setTypeface(myCustomFont);
          tv43.setTypeface(myCustomFont);
          tv45.setTypeface(myCustomFont3);
          tv46.setTypeface(myCustomFont3);
          tv47.setTypeface(myCustomFont3);
          tv48.setTypeface(myCustomFont3);
          tv49.setTypeface(myCustomFont3);
          tv50.setTypeface(myCustomFont3);
          tv53.setTypeface(myCustomFont);
          tv52.setTypeface(myCustomFont);
          tv51.setTypeface(myCustomFont);*//*
        Thread timer = new Thread(){
            public void run(){
                try{
sleep(1500);
                }catch(Throwable t){}
                finally{
                    Intent i= new Intent("LOGIN");
                    startActivity(i);
                }
            }
        };
         timer.start();
     }*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/SinkinSans-300Light.ttf");
        Typeface myCustomFont3=Typeface.createFromAsset(getAssets(),"fonts/SinkinSans-500Medium.ttf");
        Typeface myCustomFont2=Typeface.createFromAsset(getAssets(),"fonts/pacifico.ttf");
        tv57=(TextView) findViewById(R.id.textView57);
        tv57.setTypeface(myCustomFont3);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        // Start long running operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 3;
                    // Update the progress bar and display the
                    //current value in the text view

                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } Intent i= new Intent("RIGGED");
                startActivity(i);
            }
        }).start();
    }

}
