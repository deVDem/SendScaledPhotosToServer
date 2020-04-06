package ru.devdem.sendscaledphotososerver.helpers;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import ru.devdem.sendscaledphotososerver.BuildConfig;

public class NetworkController {
    private static NetworkController sNetworkController;
    private String URL_RULES = "/rules/get.php";
    private String URL_LOGIN = "/accounts/login.php";
    private String URL_REGISTER = "/accounts/register.php";
    private static RequestQueue queue;

    public static NetworkController getNetworkController() {
        if (sNetworkController == null) sNetworkController = new NetworkController();
        return sNetworkController;
    }

    public void getRulesInfo(Context context, Response.Listener<String> listener) {
        goSend(context, listener, getErrorListener(context), URL_RULES, new HashMap<>());
    }

    private NetworkController() {
        String URL_ROOT = "https://api.devdem.ru/apps/send/v/" + BuildConfig.VERSION_CODE;
        URL_RULES = URL_ROOT + URL_RULES;
        URL_LOGIN = URL_ROOT + URL_LOGIN;
        URL_REGISTER = URL_ROOT + URL_REGISTER;
    }

    public void login(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener, String login, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("login", login);
        map.put("password", password);
        goSend(context, listener, errorListener, URL_LOGIN, map);
    }

    public void register(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener, String login, String name, String email, String password, String spam) {
        Map<String, String> map = new HashMap<>();
        map.put("login", login);
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);
        map.put("spam", spam);
        goSend(context, listener, errorListener, URL_REGISTER, map);
    }


    private Response.ErrorListener getErrorListener(Context context) {
        return error -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Произошла сетевая ошибка")
                    .setMessage("Подробнее: " + error.toString())
                    .setCancelable(false)
                    .setPositiveButton("Повторить попытку", (dialog1, which) -> {
                        Activity activity = (Activity) context;
                        activity.recreate();
                        dialog1.cancel();
                    })
                    .create();
            dialog.show();
        };
    }

    private void goSend(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener, String URL, Map<String, String> map) {
        SendRequest sendRequest = new SendRequest(listener, errorListener, URL, map);
        if (queue == null) queue = Volley.newRequestQueue(context);
        queue.add(sendRequest);
    }

    private static class SendRequest extends StringRequest {
        private Map<String, String> mParams;

        SendRequest(Response.Listener<String> listener, Response.ErrorListener errorListener, String url, Map<String, String> params) {
            super(Method.POST, url, listener, errorListener);
            mParams = params;
        }

        @Override
        public Map<String, String> getParams() {
            return mParams;
        }
    }
}
