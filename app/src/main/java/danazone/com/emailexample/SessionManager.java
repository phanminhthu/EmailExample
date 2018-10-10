package danazone.com.emailexample;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PC on 1/17/2018.
 */

public class SessionManager {
    private static final String SHARED_PREFERENCES_NAME = "com.example.danazone04.danazone";
    private static final String KEY_SAVE_ID = "key_save_id";

    private static SessionManager sInstance;

    private SharedPreferences sharedPref;

    public synchronized static SessionManager getInstance() {
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

    private SessionManager() {
        // no instance
    }

    public void init(Context context) {
        sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    /**
     * get key save pass
     *
     * @return
     */
    public String getKeySaveId() {
        return sharedPref.getString(KEY_SAVE_ID, "");
    }
    public void setKeySaveId(String token) {
        sharedPref.edit().putString(KEY_SAVE_ID, token).apply();
    }
    public void updateSaveId(String token){
        sharedPref.edit().putString(KEY_SAVE_ID, token).apply();
    }

}

