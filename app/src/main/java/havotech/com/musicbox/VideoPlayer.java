package havotech.com.musicbox;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoPlayer extends AppCompatActivity {

    Toolbar toolbar;
    VideoView videoView;
    ProgressBar video_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        final String video_url = getIntent().getStringExtra("video_url");
        final String video_title = getIntent().getStringExtra("video_title");
        final String price = getIntent().getStringExtra("price");
        toolbar = findViewById(R.id.navigation_opener_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(video_title);

        videoView = findViewById(R.id.videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(video_url));
        videoView.requestFocus();
        video_progress = findViewById(R.id.video_progress);

        video_progress.setVisibility(View.VISIBLE);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.start();

                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                        // TODO Auto-generated method stub
                        Log.e("Changed", "Changed");
                        video_progress.setVisibility(View.GONE);
                        mp.start();
                    }
                });


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.download_video_menu, menu);
        return true;
    }
    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;

            case R.id.action_download_video:
                final String video_url = getIntent().getStringExtra("video_url");
                final String video_title = getIntent().getStringExtra("video_title");
                final String price = getIntent().getStringExtra("price");
                if(price.equals("Free")){
                    File root = Environment.getExternalStorageDirectory();
                    root.mkdirs();
                    String path = root.toString();

                    DownloadManager downloadManager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri1 = Uri.parse(video_url);
                    DownloadManager.Request request = new DownloadManager.Request(uri1);
                    request.setTitle("Music Box");
                    request.setDescription("Downloading "+ video_title+".mp4");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(getApplicationContext(), path + "/Music Box" + "/Videos" +  "/Video", video_title);
                    downloadManager.enqueue(request);
                }else {
                    Toast.makeText(VideoPlayer.this, "Please purchase this video first!",Toast.LENGTH_LONG).show();
                }

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
