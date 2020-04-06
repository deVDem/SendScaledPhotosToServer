package ru.devdem.sendscaledphotososerver.activites;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import ru.devdem.sendscaledphotososerver.R;
import ru.devdem.sendscaledphotososerver.helpers.NetworkController;

public class LoginActivity extends AppCompatActivity {
    private EditText etLogin;
    private EditText etPassword;
    private ProgressBar bar;
    private Button btLogin;
    private NetworkController mNetworkController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkController=NetworkController.getNetworkController();
        View view = View.inflate(this, R.layout.activity_login, null);
        setContentView(view);
        etLogin = view.findViewById(R.id.login);
        etLogin.addTextChangedListener(mTextWatcher);
        etPassword = view.findViewById(R.id.password);
        etPassword.addTextChangedListener(mTextWatcher);
        btLogin = view.findViewById(R.id.btnLogin);
        bar = view.findViewById(R.id.loading);
        btLogin.setOnClickListener(v -> login());
        btLogin.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}, new int[]{-android.R.attr.state_enabled}}, new int[]{0xFFFF2222, 0x22222222}));
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btLogin.setEnabled(etLogin.getText().toString().length() >= 4 && etPassword.getText().toString().length() >= 6);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    void login() {
        bar.setVisibility(View.VISIBLE);
        btLogin.setEnabled(false);
        etLogin.setEnabled(false);
        etPassword.setEnabled(false);

        Response.Listener<String> listener = response -> {

        };

        Response.ErrorListener errorListener = error -> {
            Toast.makeText(this, "Произошла ошибка: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            bar.setVisibility(View.GONE);
            btLogin.setEnabled(true);
            etLogin.setEnabled(true);
            etPassword.setEnabled(true);

        };

        mNetworkController.login(this, listener, errorListener, etLogin.getText().toString(), etPassword.getText().toString());
    }
}
