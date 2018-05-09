package com.example.vinothgopigraj.myassistant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private TextToSpeech textToSpeech;



    // private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                promptSpeechInput();
            }
        });

    }

    /*
    public void onButtonClick(View v)
    {
        if(v.getId() == R.id.btnSpeak)
        {
            promptSpeechInput();
        }
    }
*/
    public void promptSpeechInput() {
        textToSpeech.speak("speak something", TextToSpeech.QUEUE_FLUSH, null);
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "Sorry!", Toast.LENGTH_SHORT).show();
        }

    }

    public void onActivityResult(int request_code, int result_code, Intent i) {
        super.onActivityResult(request_code, result_code, i);
        switch (request_code) {
            case 100:
                if (result_code == RESULT_OK && i != null) {
                    textToSpeech.speak("Got it!", TextToSpeech.QUEUE_FLUSH, null);
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));

                    textToSpeech.speak("you said," + result.get(0), TextToSpeech.QUEUE_FLUSH, null);
                    if (result.get(0).contains("who are you")) {

                        textToSpeech.speak("I am your digital assistant made by Vinothgopi.", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    if (result.get(0).contains("send")) {

                        textToSpeech.speak("To whom you want to send the message?", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    if (result.get(0).contains("call")) {

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Intent intent=new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:+918760603355"));
                        startActivity(intent);
                        //textToSpeech.speak("whom you want to call? ", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    if(result.get(0).contains("alarm")) {
                        textToSpeech.speak("when to set an alarm? ", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    if(result.get(0).contains("sing")) {

                        textToSpeech.speak("Twinkle Twinkle, little star, How I wonder what you are!", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    promptSpeechInput();
                }
                break;
        }
    }
}
