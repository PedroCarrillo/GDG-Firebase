package gdglima.com.firebasetest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;

    private TextView titleTextView;
    private Button btnPromotion;

    private Button submitButton, signOutButton;
    private EditText usernameEditText, passwordEditText;

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
            } else {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        init();
    }

    private void init() {
        titleTextView = (TextView) findViewById(R.id.title);
        btnPromotion = (Button) findViewById(R.id.button);
        submitButton = (Button) findViewById(R.id.submitButton);
        signOutButton = (Button) findViewById(R.id.signOut);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                login(username, password);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void loadResources() {
        String title = mFirebaseRemoteConfig.getString("title_example");
        titleTextView.setText(title);
        boolean promotionActive = mFirebaseRemoteConfig.getBoolean("promotion_on");
        btnPromotion.setVisibility(promotionActive ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putLong(FirebaseAnalytics.Param.ITEM_ID, System.currentTimeMillis());
        mFirebaseAnalytics.logEvent(MainActivity.class.getSimpleName(), analyticsBundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void login(String username, String password) {
        if (mAuth == null) {
            return;
        }
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.complete_message,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, R.string.authentication_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            submitButton.setEnabled(false);
                            signOutButton.setEnabled(true);
                            Toast.makeText(MainActivity.this, R.string.authentication_success,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logout() {
        signOutButton.setEnabled(false);
        submitButton.setEnabled(true);
        if (mAuth != null) {
            mAuth.signOut();
        }
    }

}
