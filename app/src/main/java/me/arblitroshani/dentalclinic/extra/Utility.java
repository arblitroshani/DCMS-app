package me.arblitroshani.dentalclinic.extra;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.model.User;

import static android.content.Context.MODE_PRIVATE;

public class Utility {

    public static void setFirebaseInstanceId(Context context, String InstanceId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_firebase_instance_id_key),InstanceId);
        editor.apply();
    }

    public static String getFirebaseInstanceId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pref_firebase_instance_id_key);
        String default_value = context.getString(R.string.pref_firebase_instance_id_default_key);
        return sharedPreferences.getString(key, default_value);
    }

    public static void setNationalIdSharedPreference(Context context, String nationalId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Config.PREFS_USER, MODE_PRIVATE).edit();
        editor.putString("nationalId", nationalId);
        editor.commit();
    }

    public static String getNationalIdSharedPreference(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREFS_USER, MODE_PRIVATE);
        return prefs.getString("nationalId", null);
    }

    public static void setLoggedInUser(Context context, User user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Config.PREFS_USER, MODE_PRIVATE).edit();
        editor.putString("loggedInUser", new Gson().toJson(user));
        editor.commit();
    }

    public static User getLoggedInUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREFS_USER, MODE_PRIVATE);
        String json = prefs.getString("loggedInUser", null);
        return json == null ? null : new Gson().fromJson(json, User.class);
    }

    public static String toCamelCase(String input) {
        return input.toUpperCase().charAt(0) + input.substring(1).toLowerCase();
    }
}
