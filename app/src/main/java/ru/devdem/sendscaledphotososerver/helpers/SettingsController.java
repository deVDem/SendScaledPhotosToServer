package ru.devdem.sendscaledphotososerver.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import ru.devdem.sendscaledphotososerver.helpers.objects.User;

public class SettingsController {
    private Context mContext;
    private static SharedPreferences mSettings;
    private static SharedPreferences mAccountData;


    public static SettingsController getInstance(Context context) {
        return new SettingsController(context);
    }
    SettingsController(Context context) {
        mSettings=context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        mAccountData=context.getSharedPreferences("account", Context.MODE_PRIVATE);
        mContext=context;
    }
    public boolean isFirst() {
        return mSettings.getBoolean("first", true);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = mAccountData.edit();
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("login", user.getLogin());
        editor.putString("urlImage", user.getUrlImage());
        editor.putInt("group", user.getGroup());
        editor.putString("password", user.getPassword());
        editor.putString("token", user.getToken());
        editor.putBoolean("pro", user.isPro());
        editor.apply();
    }

    public User loadUser() {
        User user = new User();
        user.setId(mAccountData.getInt("id", 0));
        user.setName(mAccountData.getString("name", ""));
        user.setEmail(mAccountData.getString("email", ""));
        user.setLogin(mAccountData.getString("login", ""));
        user.setUrlImage(mAccountData.getString("urlImage", ""));
        user.setGroup(mAccountData.getInt("group", 0));
        user.setPassword(mAccountData.getString("password", ""));
        user.setToken(mAccountData.getString("token", ""));
        user.setPro(mAccountData.getBoolean("pro", false));
        return user;
    }

    public void setFirst(boolean b) {
        mSettings.edit().putBoolean("first", b).apply();
    }
}
