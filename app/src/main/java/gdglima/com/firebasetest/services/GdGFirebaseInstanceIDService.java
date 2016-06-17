package gdglima.com.firebasetest.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by pedrocarrillo on 6/16/16.
 */

public class GdGFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static final String TAG = GdGFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Device token: " + refreshedToken);
    }

}
