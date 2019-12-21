package havotech.com.musicbox;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigFetchException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

public class MusicBox extends Application {

    FirebaseRemoteConfigSettings configSettings;
    FirebaseRemoteConfig remoteConfig;
    long cacheExpiration = 5;

    @Override
    public void onCreate() {
        super.onCreate();


         remoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        remoteConfig.setConfigSettings(configSettings);
        Map<String,Object> default_value = new HashMap<>();
        default_value.put(UpdateHelper.KEY_UPDATE_ENABLE, false);
        default_value.put(UpdateHelper.KEY_UPDATE_VERSION, "1.0");
        default_value.put(UpdateHelper.KEY_UPDATE_URL, "your default url on Appstore");
        remoteConfig.setDefaults(default_value);

        remoteConfig.fetch(getCacheExpiration()) //fetch data from firebase config every 5 minute.

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            remoteConfig.activateFetched();
                        }
                        else {
                            // And this doesn't happen
                            final Exception x = task.getException();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Firebase config error", e.getMessage());
            }
        });

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    public long getCacheExpiration() {
// If is developer mode, cache expiration set to 0, in order to test
        if (remoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }
}
