package com.phoenix.text2voice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
    implements TextToSpeech.OnInitListener
{
    private int MY_DATA_CHECK_CODE = 0; //Check TTS Success Code
    private String GOOGLE_TTS_PACKAGE_NAME = "com.google.android.tts"; //Google TextToSpeech Package Name
    private TextToSpeech myTTS;
    private EditText _edtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _edtText = (EditText) findViewById(R.id.edt_text);

        //Check TTS
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_DATA_CHECK_CODE) { //If a TTS exists.
            //using Google tts package. It supports Turkish.
            myTTS = new TextToSpeech(this, this, GOOGLE_TTS_PACKAGE_NAME); //use Google TTS because it supports Turkish

        } else { //If it doesn't exist
            // Install TTS
            Intent installTTSIntent = new Intent();
            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installTTSIntent);
        }
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            //set Turkish to TTS
            Locale trLocale = new Locale("tr", "TUR");
            int result = myTTS.setLanguage(trLocale);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, R.string.error_lang_not_support, Toast.LENGTH_LONG).show();
            }
        }
        else if (status == TextToSpeech.ERROR) {

            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG).show();
        }
    }

    public void onSpeech(View v) { //Click Speech Button
        //Text to Speech
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // sdk version 21 and newer
            myTTS.speak(_edtText.getText().toString(), TextToSpeech.QUEUE_ADD, null, null);
        }
        else {
            myTTS.speak(_edtText.getText().toString(), TextToSpeech.QUEUE_ADD, null);
        }

    }
}
