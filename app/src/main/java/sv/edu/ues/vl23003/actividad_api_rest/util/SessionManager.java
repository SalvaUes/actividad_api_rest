package sv.edu.ues.vl23003.actividad_api_rest.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "AppSession";
    private static final String KEY_TOKEN = "user_token";
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }
}