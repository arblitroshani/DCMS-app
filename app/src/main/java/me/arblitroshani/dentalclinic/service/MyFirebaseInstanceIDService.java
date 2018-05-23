package me.arblitroshani.dentalclinic.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.extra.Utility;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onCreate() {
        String currentToken = FirebaseInstanceId.getInstance().getToken();

        // TODO: TRY:
//        String token = FirebaseInstanceId.getInstance().getToken();
//        while(token == null) {
//            token = FirebaseInstanceId.getInstance().getToken();
//        }

        String savedToken = Utility.getFirebaseInstanceId(getApplicationContext());
        String defaultToken = getApplication().getString(R.string.pref_firebase_instance_id_default_key);

        if(currentToken != null && !savedToken.equalsIgnoreCase(defaultToken)) {
            Utility.setFirebaseInstanceId(getApplicationContext(), currentToken);
        }

        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN: ", refreshedToken);

        Utility.setFirebaseInstanceId(getApplicationContext(), refreshedToken);
    }
}