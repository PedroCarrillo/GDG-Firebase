package gdglima.com.firebasetest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private TextView titleTextView;
    private Button btnPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleTextView = (TextView)findViewById(R.id.title);
        btnPromotion = (Button) findViewById(R.id.button);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mFirebaseRemoteConfig.activateFetched();
                loadResources();
            }
        });
    }

    private void loadResources() {

        String title = mFirebaseRemoteConfig.getString("title_example");
        titleTextView.setText(title);

        boolean promotionActive = mFirebaseRemoteConfig.getBoolean("promotion_on");

        btnPromotion.setVisibility(promotionActive ? View.VISIBLE : View.INVISIBLE);


    }

}
