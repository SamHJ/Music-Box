package havotech.com.musicbox;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;

import java.io.File;
import java.util.UUID;

public class PaymentGateWay extends AppCompatActivity {
        Toolbar toolbar;
        TextView price_text;
        ProgressDialog dialog;

    String fName = "Some Name";
    String lName = "Some Name";
    String txRef;
    String country = "NG";
    String currency = "NGN";

    final String publicKey = "FLWPUBK-45aaf48a94b34a7636a10ce65805aeab-X"; //Get your public key from your account
    final String encryptionKey = "36361ee089a6876a670f91ee"; //Get your encryption key from your account
    Button continue_btn;
    TextInputEditText email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gate_way);
        toolbar = findViewById(R.id.navigation_opener_toolbar);
        final String price = getIntent().getStringExtra("price");
        final String song_name = getIntent().getStringExtra("song_name");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(song_name);

        price_text = findViewById(R.id.text_price);
        price_text.setText("You will be charged "+"N"+price);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Performing Transaction");
        dialog.setMessage("A moment please!");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        continue_btn = findViewById(R.id.continue_btn);
        email_address = findViewById(R.id.email_address);

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isValidEmail(email_address.getText().toString().trim())){
                   try {
                       dialog.show();
                       int priceToCharge = Integer.parseInt(price);
                       makePayment(priceToCharge, song_name, email_address.getText().toString().trim());
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

               }else {
                   Toast.makeText(PaymentGateWay.this, "Please insert a valid email address!", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public void makePayment(int amount, String email, String song_name){
        txRef = email +" "+  UUID.randomUUID().toString();

        /*
        Create instance of RavePayManager
         */
        new RavePayManager(this).setAmount(amount)
                .setCountry(country)
                .setCurrency(currency)
                .setEmail(email)
                .setfName(fName)
                .setlName(lName)
                .setNarration("Payment for "+ song_name)
                .setPublicKey(publicKey)
                .setEncryptionKey(encryptionKey)
                .setTxRef(txRef)
                .acceptAccountPayments(true)
                .acceptCardPayments(
                        true)
                .acceptMpesaPayments(false)
                .acceptGHMobileMoneyPayments(false)
                .onStagingEnv(false).
                allowSaveCardFeature(true)
                .withTheme(R.style.DefaultPayTheme)
                .initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                dialog.dismiss();
                final String type = getIntent().getStringExtra("type");
                final String url = getIntent().getStringExtra("url");
                final String song_name = getIntent().getStringExtra("song_name");
                if(type.equals("mp4")){
                    Toast.makeText(this, "PAYMENT SUCCESSFUL " + message, Toast.LENGTH_SHORT).show();
                    File root = Environment.getExternalStorageDirectory();
                    root.mkdirs();
                    String path = root.toString();

                    DownloadManager downloadManager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri1 = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri1);
                    request.setTitle("Music Box");
                    request.setDescription("Downloading "+ song_name+".mp4");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(getApplicationContext(), path + "/Music Box" + "/Videos" +  "/Video", song_name);
                    downloadManager.enqueue(request);
                }else {
                    Toast.makeText(this, "PAYMENT SUCCESSFUL " + message, Toast.LENGTH_SHORT).show();
                    File root = Environment.getExternalStorageDirectory();
                    root.mkdirs();
                    String path = root.toString();

                    DownloadManager downloadManager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri1 = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri1);
                    request.setTitle("Music Box");
                    request.setDescription("Downloading "+ song_name+".mp3");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(getApplicationContext(), path + "/Music Box" + "/Musics" +  "/Audio", song_name);
                    downloadManager.enqueue(request);
                }


            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                dialog.dismiss();
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                dialog.dismiss();
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
