package com.example.aicalculator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout parentRelativeLayout;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";
    private Button userText, clearButton;
    private Button num0, num1, num2, num3, num4, num5, num6, num7, num8, num9;
    private Button plus, minus, multiply, divide, equals;
    private EditText answer;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userText = findViewById(R.id.answer);
        clearButton = findViewById(R.id.clear_btn);
        num0 = findViewById(R.id.number_0);
        num1 = findViewById(R.id.number_1);
        num2 = findViewById(R.id.number_2);
        num3 = findViewById(R.id.number_3);
        num4 = findViewById(R.id.number_4);
        num5 = findViewById(R.id.number_5);
        num6 = findViewById(R.id.number_6);
        num7 = findViewById(R.id.number_7);
        num8 = findViewById(R.id.number_8);
        num9 = findViewById(R.id.number_9);
        plus = findViewById(R.id.plus_btn);
        minus = findViewById(R.id.minus_btn);
        multiply = findViewById(R.id.multiply_btn);
        divide = findViewById(R.id.divide_btn);
        equals = findViewById(R.id.equals_btn);

        number = "";

        //numbersClicked();

        number = numbersClicked();

        equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double answer;

                answer = calculator(number);

                String convert = answer + "";

                if (convert.equals("Infinity")) {
                    userText.setText("UNDEFINED");
                    userText.setTextSize(50f);
                } else {
                    userText.setText(convert);
                    userText.setTextSize(50f);
                }

            }
        });

        checkVoiceCommandPermission();

        parentRelativeLayout = findViewById(R.id.parentRelativeLayout);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                userText.setText("Say The Numbers");
                userText.setTextSize(24f);
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            //speech input from user, returns many result
            public void onResults(Bundle bundle) {
                ArrayList<String> matchesFound = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


                if(matchesFound != null){
                    keeper = matchesFound.get(0);
                    String convert = "";

                    if(keeper.contains("million") || keeper.contains("billion")){
                       keeper = keeper.replace(" million", "000000");
                       keeper =  keeper.replace(" billion", "000000000");
                    }


                    if(Character.isLetter(keeper.charAt(0)) ){
                        userText.setText("Invalid Response Please Try Again");
                        userText.setTextSize(24f);

                    }else {
                        convert = calculator(keeper) + "";


                        if (convert.equals("Infinity")) {
                            userText.setText("UNDEFINED");
                            userText.setTextSize(50f);
                        } else {
                            userText.setText(convert);
                            userText.setTextSize(50f);
                        }

                    }
                    //addition(keeper);
                   //Toast.makeText(MainActivity.this, "Results = " + calculator(keeper), Toast.LENGTH_LONG).show();
                   Toast.makeText(MainActivity.this, "You said = " + keeper, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userText.setText("Tap To Ask Question");
                userText.setTextSize(24f);
                number = "";
            }
        });



        userText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    //user press anywhere
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper ="";
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;

                }

                return false;
            }
        });
    }




    private void checkVoiceCommandPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED))
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }

        }
    }

    private String numbersClicked(){
        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = number + "0";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + "1";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = number + "2";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = number + "3";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + "4";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + "5";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + "6";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + "7";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + "8";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + "9";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + " + ";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + " - ";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + " * ";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number + " / ";
                userText.setText(number);
                userText.setTextSize(50f);
            }
        });

        return number;
    }

    private static double calculator(String voice) {

        int voiceLength = voice.length();
        int operationPosition = 0;
        String numberInString;
        int convertStringToNumber = 0;
        double total = 0.0;
        char lastOperation = 'x';
        char savedOperation = 'x';
        boolean firstPlus = true;
        int counter =0;



        for(int i =0; i < voiceLength; i++) {


            if(voice.charAt(i) == ' ' && counter == 0) {

                total += convertStringToNumber;

                counter = 1;

            }


            else if(voice.charAt(i) == '+' || voice.charAt(i) == '-'
                    || voice.charAt(i) == '*' || voice.charAt(i) == '/') {
                savedOperation = lastOperation;
                operationPosition = i + 2;
                lastOperation = voice.charAt(i);


                if(firstPlus == true) {
                    firstPlus = false;
                }else if(firstPlus == false) {
                    if(savedOperation == '+') {

                        total += convertStringToNumber;

                    }
                    else if(savedOperation == '-') {

                        total -= convertStringToNumber;

                    }else if(savedOperation == '*'){
                        total *= convertStringToNumber;
                    }else if(savedOperation == '/'){
                        total /= convertStringToNumber;
                    }

                }
            }else if(Character.isDigit(voice.charAt(i)))
            {

                numberInString = voice.substring(operationPosition , i + 1);

                if(numberInString.charAt(0) != '+' || numberInString.charAt(0) != '-' ||
                numberInString.charAt(0) != '*' || numberInString.charAt(0) != '/')	{

                    convertStringToNumber = Integer.parseInt(numberInString);
                }
            }


        }

        if(lastOperation == '+')
            total += convertStringToNumber;
        else if(lastOperation == '-')
            total -= convertStringToNumber;
        else if(lastOperation == '*')
            total *= convertStringToNumber;
        else if(lastOperation == '/')
            total /= convertStringToNumber;
        return total;

    }

}
