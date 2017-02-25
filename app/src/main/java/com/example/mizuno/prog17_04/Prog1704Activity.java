package com.example.mizuno.prog17_04;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class Prog1704Activity extends AppCompatActivity {

    private VideoView video;
    private TextView counter, pointX, pointY;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prog1704);

        video = (VideoView) findViewById(R.id.videoView);
        //動画メディアの指定
        video.setVideoPath("android.resource://" + this.getPackageName() + "/" + R.raw.nhk);
        Button b_stop = (Button) findViewById(R.id.Stop);
        Button b_start = (Button) findViewById(R.id.Start);
        counter = (TextView) findViewById(R.id.Counter);
        seekBar =(SeekBar) findViewById(R.id.seekBar);
        pointX = (TextView) findViewById(R.id.pointX);
        pointY = (TextView) findViewById(R.id.pointY);

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.start();
            }
        });

        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.pause();
            }
        });

        //再生時間表示に関する処理
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                counter.post(new Runnable() {
                    @Override
                    public void run() {
                        counter.setText(String.valueOf((double) video.getCurrentPosition() / 1000) + "s");
                        seekBar.setProgress(100 * video.getCurrentPosition() / video.getDuration());
                        seekBar.setSecondaryProgress(100 * video.getBufferPercentage());
                    }
                });
            }
        }, 0, 50);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                video.seekTo(video.getDuration() * seekBar.getProgress() / 100);
            }
        });

        //動画をタッチしたときの処理
        video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AsyncHttp post = new AsyncHttp(event.getX() / v.getWidth() * 799, event.getY() / v.getHeight() * 449, (double)video.getCurrentPosition()/1000);
                post.execute();
                pointX.setText(String.valueOf(event.getX() / v.getWidth()));
                pointY.setText(String.valueOf(event.getY() / v.getHeight()));
                return true;
            }
        });
    }
}
