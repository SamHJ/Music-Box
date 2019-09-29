package havotech.com.musicbox;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewLoader extends AppCompatActivity {
   WebView advert_webview;
   ProgressBar advert_loading_bar;
   Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_loader);
        String advert_url = getIntent().getStringExtra("advert_url");
        String advert_title = getIntent().getStringExtra("advert_title");
        toolbar = findViewById(R.id.navigation_opener_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(advert_title);

        advert_loading_bar = findViewById(R.id.advert_loading_bar);
        advert_webview = findViewById(R.id.advert_webview);
        initWebView(advert_url);
    }




    private void initWebView(String postUrl) {

        advert_webview.loadUrl(postUrl);
        advert_webview.getSettings().setJavaScriptEnabled(true);
        advert_webview.clearCache(true);
        advert_webview.clearHistory();
        advert_webview.getSettings().setUseWideViewPort(true);
        advert_webview.getSettings().setLoadWithOverviewMode(true);
        advert_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        advert_webview.setHorizontalScrollBarEnabled(false);
        advert_webview.getSettings().setSupportZoom(true);
        advert_webview.getSettings().setBuiltInZoomControls(true);
        advert_webview.getSettings().setDisplayZoomControls(true);
        advert_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        advert_webview.getSettings().setAppCacheEnabled(true);
        advert_webview.getSettings().setDomStorageEnabled(true);
        advert_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        advert_webview.getSettings().setSaveFormData(true);

        //webviewClient
        advert_webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                advert_loading_bar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                advert_loading_bar.setVisibility(View.GONE);
                invalidateOptionsMenu();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                advert_loading_bar.setVisibility(View.GONE);

                invalidateOptionsMenu();
                super.onReceivedError(view, request, error);
            }
        });


        //webChromeClient
        advert_webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                advert_loading_bar.setVisibility(View.VISIBLE);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

            }
        });

        advert_webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request mRequest = new DownloadManager.Request(Uri.parse(url));
                mRequest.allowScanningByMediaScanner();
                mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager manager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                manager.enqueue(mRequest);
                mRequest.setTitle("Music Box");
                Toast.makeText(WebViewLoader.this, "Your file is Downloading...", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
