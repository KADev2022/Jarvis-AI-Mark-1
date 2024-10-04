package com.example.jarvis_ai_mark_1;

import static com.example.jarvis_ai_mark_1.GreetingFunction.wishMe;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer recognizer;
    private TextView textView;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Use the Dexter library to add the permission to record audio
        Dexter.withContext(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        System.exit(0);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        initTextToSpeech();
        findById();
        result();
    }

    /**
     * Function to initialise text to speech
     */
    private void initTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                /*
                - If the engine size is empty, then it is not available
                - Otherwise Jarvis will speak
                 */
                if (tts.getEngines().size() == 0) {
                    Toast.makeText(MainActivity.this, "Engine is not available", Toast.LENGTH_SHORT).show();
                } else {
                    String greet = wishMe();
                    speak("Hi, I am Jarvis AI Mark One" + greet);
                }
            }
        });
    }

    /**
     * Function to allow Jarvis to speak
     *
     * @param msg allows Jarvis to speak to the user
     */
    private void speak(String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /**
     * Function to find the element by ID
     */
    private void findById() {
        textView = (TextView) findViewById(R.id.textView);
    }

    /**
     * Function to convert speech to text
     */
    private void result() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    ArrayList<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Toast.makeText(MainActivity.this, "" + result.get(0), Toast.LENGTH_SHORT).show();
                    textView.setText(result.get(0));
                    response(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    /**
     * Function to allow Jarvis to respond to your voice
     *
     * @param msg allows Jarvis to respond to the user's voice
     */
    private void response(String msg) {
        String messages = msg.toLowerCase(Locale.ROOT);

        // If your voice includes the word 'hi', then Jarvis will say 'Hello Sir, Jarvis at your service. Please tell me how can I help you?'
        if (messages.indexOf("hi") != -1) {
            speak("Hello Sir, Jarvis at your service. Please tell me how can I help you?");
        }

        // If your voice includes the word 'time', then Jarvis will say the time
        if (messages.indexOf("time") != -1) {
            Date date = new Date();
            String time = DateUtils.formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
            speak(time);
        }

        // If your voice includes the word 'date', then Jarvis will say today's date
        if (messages.indexOf("date") != -1) {
            SimpleDateFormat dt = new SimpleDateFormat("dd MM yyyy");
            Calendar calendar = Calendar.getInstance();
            String todayDate = dt.format(calendar.getTime());
            speak("The date today is" + todayDate);
        }

        // If your voice includes the word 'google', then Jarvis will navigate you to Google
        if (messages.indexOf("google") != -1) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"));
            startActivity(intent);
        }

        // If your voice includes the word 'youtube', then Jarvis will navigate you to YouTube
        if (messages.indexOf("youtube") != -1) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
            startActivity(intent);
        }

        // If your voice includes the word 'search', then Jarvis will navigate you to Google with the search query you mentioned
        if (messages.indexOf("search") != -1) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + messages.replace("search", "")));
            startActivity(intent);
        }
    }

    /**
     * Function to start recording your voice
     *
     * @param view controls the view of the "RECORD AUDIO" button
     */
    public void startRecording(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        recognizer.startListening(intent);
    }
}