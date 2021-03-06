package com.app.chaton.Utils;

import android.content.SharedPreferences;

import com.app.chaton.API_helpers.User;

public class PreferenceHelper {

    private final static String IS_AUTH = "is_auth";
    public final static String NAME = "name";
    private final static String EMAIL = "email";
    private final static String IS_ADMIN = "is_admin";
    private final static String SECRET_KEY = "secret_key";
    public final static String AVATAR = "avatar";
    public final static String ID = "id";

    private SharedPreferences preferences;

    public PreferenceHelper(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public boolean isAuth() {
        return preferences.getBoolean(IS_AUTH, false);
    }

    public String getName() {
        return preferences.getString(NAME, "");
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "");
    }

    public String getSecretKey() {
        return preferences.getString(SECRET_KEY, "");
    }

    public String getAvatar() {
        return preferences.getString(AVATAR, "");
    }

    public Long getId() {
        return preferences.getLong(ID, 0);
    }

    public void authUser(User user) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(IS_AUTH, true);
        editor.putString(NAME, user.getName());
        editor.putString(EMAIL, user.getEmail());
        editor.putBoolean(IS_ADMIN, user.isAdmin());
        editor.putString(SECRET_KEY, user.getSecretKey());
        editor.putLong(ID, user.getId());
        editor.putString(AVATAR, user.getAvatar());

        editor.commit();
    }

    public void reset() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
