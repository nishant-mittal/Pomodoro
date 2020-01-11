package com.example.android.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView timeTextView,numberOfPomodorosTextView;
    private FloatingActionButton startButton;
    private ImageView resetButton;
    private ProgressBar progressBarCircular;
    private int seconds = 1500, progress = 0, noOfPomodoros = 0;
    private boolean running;
    private boolean isRunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            progress = savedInstanceState.getInt("progress");
            running = savedInstanceState.getBoolean("running");
            isRunning = savedInstanceState.getBoolean("isRunning");

        }


        startButton = findViewById(R.id.start_button);
        resetButton = findViewById(R.id.reset_timer_image_view);
        runTimer();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning) {
                    running = false;
                    startButton.setImageResource(R.drawable.ic_play);
                }
                else {
                    running = true;
                    startButton.setImageResource(R.drawable.ic_pause);
                }
                isRunning = !isRunning;
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                seconds = 1500;
                progress = 0;
                startButton.setImageResource(R.drawable.ic_play);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds",seconds);
        outState.putInt("progress",progress);
        outState.putBoolean("running",running);
        outState.putBoolean("isRunning",isRunning);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isRunning) {
            running = true;
            startButton.setImageResource(R.drawable.ic_pause);
        }
    }

    public void runTimer() {
        timeTextView = findViewById(R.id.time_text_view);
        progressBarCircular = findViewById(R.id.progressBarCircular);
        numberOfPomodorosTextView = findViewById(R.id.number_of_pomodoros);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(running) {
                    seconds--;
                    progress++;
                }
                int secs = seconds%60;
                int minutes = (seconds % 3600)/60;
                String time = String.format(Locale.getDefault(),"%02d:%02d",minutes,secs);
                timeTextView.setText(time);
                progressBarCircular.setProgress(progress);
                if(seconds == 0) {
                    running = false;
                    seconds = 1500;
                    progress = 0;
                    noOfPomodoros++;
                    numberOfPomodorosTextView.setText("" + noOfPomodoros);
                    if(noOfPomodoros % 4 == 0 && noOfPomodoros > 0) {
                        startButton.setImageResource(R.drawable.ic_play);
                        Toast toast = Toast.makeText(getApplicationContext(),R.string.break_30,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,270);
                        toast.show();
                    }
                    else {
                        startButton.setImageResource(R.drawable.ic_play);
                        Toast toast = Toast.makeText(getApplicationContext(),R.string.break_5,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,270);
                        toast.show();
                    }
                }
                handler.postDelayed(this,1000);
            }
        });
    }
}
