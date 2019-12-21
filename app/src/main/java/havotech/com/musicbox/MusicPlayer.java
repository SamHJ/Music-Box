package havotech.com.musicbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    LinearLayout musicImageLayout;
    ImageButton closeMusicPlayerButton, next_btn, previous_btn, pause_play_btn;
    SeekBar music_seekbar;
    TextView song_duration, song_counter, song_title, song_author,song_price;
    MediaPlayer mMediaPlayer;
    ProgressBar loading_music_bar;
    Handler handler = new Handler();

    String song_name, song_image_url, song_url, price, artiste_name;
    ImageButton single_loop,download_song_btn;

    int[] images = {R.drawable.home_header_img, R.drawable.header_home,R.drawable.home_2, R.drawable.home_3, R.drawable.home_4,
            R.drawable.home_5,R.drawable.home_6,R.drawable.home_7,R.drawable.home_8, R.drawable.home_9,
            R.drawable.home_10,R.drawable.home_11,R.drawable.home_12,R.drawable.home_13,R.drawable.home_14,R.drawable.home_15};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
          getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
           getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        song_name = getIntent().getStringExtra("song_name");
        song_url = getIntent().getStringExtra("song_url");
        artiste_name = getIntent().getStringExtra("artiste_name");
        song_image_url = getIntent().getStringExtra("song_image");
        price = getIntent().getStringExtra("price");

        musicImageLayout = findViewById(R.id.music_player_layout);
        Random rr = new Random();
        int image_int = rr.nextInt(16);
        musicImageLayout.setBackgroundResource(images[image_int]);
        new LoadBackground(song_image_url,"androidfigure").execute();


        closeMusicPlayerButton = findViewById(R.id.close_music_player);
        next_btn = findViewById(R.id.next_btn);
        previous_btn = findViewById(R.id.previous_btn);
        pause_play_btn = findViewById(R.id.pause_play_btn);
        music_seekbar = findViewById(R.id.music_seekbar);
        song_duration = findViewById(R.id.song_duration);
        song_counter = findViewById(R.id.song_counter);
        song_title = findViewById(R.id.song_title);
        song_author = findViewById(R.id.song_author);
        song_price = findViewById(R.id.song_price);
        single_loop = findViewById(R.id.single_loop);
        download_song_btn = findViewById(R.id.download_song_btn);
       if(price.equals("Free")){
           song_price.setVisibility(View.GONE);
       }else {
           song_price.setText("N "+price);
       }

//        loading_music_bar = findViewById(R.id.loading_music_bar);
//        loading_music_bar.setVisibility(View.GONE);jj

        if(mMediaPlayer != null){
           mMediaPlayer.stop();
           mMediaPlayer.release();
           mMediaPlayer = null;
        }

        mMediaPlayer = new MediaPlayer();

        song_author.setText(artiste_name);
        song_title.setText(song_name);


        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    mMediaPlayer.setDataSource(song_url);
                    mMediaPlayer.prepareAsync(); // prepare async to not block main thread

                } catch (IOException e) {
//                    Toast.makeText(this, "mp3 not found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(r, 100);


        //mp3 will be started after completion of preparing...
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
                pause_play_btn.setImageResource(R.drawable.ic_pause);
                music_seekbar.setProgress(0);
                music_seekbar.setMax(player.getDuration());
                new SeekBarHandler().execute();
                int duration = mMediaPlayer.getDuration();
                String time = String.format("%02d min, %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                );
                song_counter.setText(time);
                handler.postDelayed(UpdateSongTime,100);
            }

        });



        pause_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togglePlayPause();

            }
        });

        closeMusicPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        if (handler != null)
                            handler.removeCallbacks(null);
                    }
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                    handler.removeCallbacksAndMessages (null);
                    handler.removeCallbacks(UpdateSongTime);
                    SeekBarHandler seekBarHandler = new SeekBarHandler();
                    seekBarHandler.cancel(true);

                }
            }
        });

        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.seekTo(0);
                mMediaPlayer.start();
                new SeekBarHandler().execute();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSongPosition = mMediaPlayer.getCurrentPosition();
               if(currentSongPosition+5000 <= mMediaPlayer.getDuration()){
                   mMediaPlayer.seekTo(currentSongPosition+5000);
               }else {
                   // forward to end position
                   mMediaPlayer.seekTo(mMediaPlayer.getDuration());
               }
                mMediaPlayer.start();
                new SeekBarHandler().execute();
            }
        });

        single_loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer.isLooping()){
                    setLooping(false);
                    single_loop.setImageResource(R.drawable.ic_repeat);
                    new SeekBarHandler().execute();
                }else {
                    setLooping(true);
                    single_loop.setImageResource(R.drawable.ic_repeat_one);
                    new SeekBarHandler().execute();
                }
            }
        });

        download_song_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(price.equals("Free")){
                  File root = Environment.getExternalStorageDirectory();
                  root.mkdirs();
                  String path = root.toString();

                  DownloadManager downloadManager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                  Uri uri1 = Uri.parse(song_url);
                  DownloadManager.Request request = new DownloadManager.Request(uri1);
                  request.setTitle("Music Box");
                  request.setDescription("Downloading "+ song_name+".mp3");
                  request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                  request.setDestinationInExternalFilesDir(getApplicationContext(), path + "/Music Box" + "/Songs" +  "/Audio", song_name);
                  downloadManager.enqueue(request);
              }else {
                  Intent openAdvertUrl = new Intent(MusicPlayer.this, PaymentGateWay.class);
                  openAdvertUrl.putExtra("price", price );
                  openAdvertUrl.putExtra("song_name", song_name);
                  openAdvertUrl.putExtra("type","mp3");
                  openAdvertUrl.putExtra("url", song_url);
                  Activity activity = (Activity) MusicPlayer.this;
                  MusicPlayer.this.startActivity(openAdvertUrl);
                  activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
              }
            }
        });

        Thread t = new MyThread();
        t.start();
    }

    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    public void setLooping(boolean isLooping) {
        mMediaPlayer.setLooping(isLooping);
    }

    public class MyThread extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                if(mMediaPlayer != null){
                    music_seekbar.setProgress(mMediaPlayer.getCurrentPosition());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
           int startTime = mMediaPlayer.getCurrentPosition();
            song_duration.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            handler.postDelayed(this, 100);
        }
    };

    public class SeekBarHandler extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("","###################Destroyed##################");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
           if(mMediaPlayer != null){
               music_seekbar.setProgress(mMediaPlayer.getCurrentPosition());
           }
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            while(mMediaPlayer !=null && mMediaPlayer.isPlaying()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                   onProgressUpdate();
               }
            return null;
        }

    }

    private class LoadBackground extends AsyncTask<String, Void, Drawable> {

        private String imageUrl , imageName;

        public LoadBackground(String url, String file_name) {
            this.imageUrl = url;
            this.imageName = file_name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Drawable doInBackground(String... urls) {

            try {
                InputStream is = (InputStream) this.fetch(this.imageUrl);
                Drawable d = Drawable.createFromStream(is, this.imageName);
                return d;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        private Object fetch(String address) throws MalformedURLException,IOException {
            URL url = new URL(address);
            Object content = url.getContent();
            return content;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            musicImageLayout.setBackground(result);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }


    private void togglePlayPause() {
        if (mMediaPlayer!=null &&mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            pause_play_btn.setImageResource(R.drawable.ic_play);
        } else if(mMediaPlayer!=null && !mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
            pause_play_btn.setImageResource(R.drawable.ic_pause);
            new SeekBarHandler().execute();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    protected void onPause() {
//        isViewOn=false;
        super.onPause();
    }

    protected void onResume() {
//        isViewOn=true;
        new SeekBarHandler().execute();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        togglePlayPause();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                if (handler != null)
                    handler.removeCallbacks(null);
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
            handler.removeCallbacksAndMessages (null);
            handler.removeCallbacks(UpdateSongTime);
            SeekBarHandler seekBarHandler = new SeekBarHandler();
            seekBarHandler.cancel(true);

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
          togglePlayPause();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                if (handler != null)
                    handler.removeCallbacks(null);
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
            handler.removeCallbacksAndMessages (null);
            SeekBarHandler seekBarHandler = new SeekBarHandler();
            seekBarHandler.cancel(true);
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
