package ru.devdem.sendscaledphotososerver.activites;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import ru.devdem.sendscaledphotososerver.R;
import ru.devdem.sendscaledphotososerver.helpers.NetworkController;
import ru.devdem.sendscaledphotososerver.helpers.SettingsController;
import ru.devdem.sendscaledphotososerver.helpers.objects.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static String nameRegex = "(^[A-Z]{1}[a-z]{1,30} [A-Z]{1}[a-z]{1,30}$)|(^[А-Я]{1}[а-я]{1,30} [А-Я]{1}[а-я]{1,30}$)";
    private static String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
    private static String loginRegex = "[A-Za-z0-9_]{4,255}";
    private EditText mEtLogin;
    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtCPassword;
    private CheckBox mCbSpam;
    private CheckBox mCbAgree;
    private Button mBnRegister;
    private TextView mTxStatus;
    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            check();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private NetworkController mNetworkController;
    private SettingsController mSettingsController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNetworkController = NetworkController.getNetworkController();
        mSettingsController = SettingsController.getInstance(this);
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_register, null);
        setContentView(view);
        mEtLogin = view.findViewById(R.id.login);
        mEtLogin.addTextChangedListener(mTextWatcher);
        mEtName = view.findViewById(R.id.name);
        mEtName.addTextChangedListener(mTextWatcher);
        mEtEmail = view.findViewById(R.id.email);
        mEtEmail.addTextChangedListener(mTextWatcher);
        mEtPassword = view.findViewById(R.id.password);
        mEtPassword.addTextChangedListener(mTextWatcher);
        mEtCPassword = view.findViewById(R.id.passwordc);
        mEtCPassword.addTextChangedListener(mTextWatcher);
        mCbSpam = view.findViewById(R.id.spam);
        mCbAgree = view.findViewById(R.id.agree);
        mCbAgree.setOnClickListener(v -> check());
        mBnRegister = view.findViewById(R.id.btnRegister);
        mBnRegister.setOnClickListener(v -> register());
        mBnRegister.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}, new int[]{-android.R.attr.state_enabled}}, new int[]{0xFFFF2222, 0x22222222}));
        mTxStatus = view.findViewById(R.id.status);
        check();
    }

    void check() {
        String statusText = "";
        if (!mEtLogin.getText().toString().matches(loginRegex))
            statusText = "Введите логин (от 4 символов A-Z, a-z, 0-9, _)\n";
        if (!mEtName.getText().toString().matches(nameRegex))
            statusText += "Введите имя и фамилию\n";
        if (!mEtEmail.getText().toString().matches(emailRegex))
            statusText += "Введите правильный Email-адрес (например: login@domain.com)\n";
        if (mEtPassword.getText().toString().length() < 6)
            statusText += "Введите пароль от 6 символов\n";
        else if (!mEtPassword.getText().toString().equals(mEtCPassword.getText().toString()))
            statusText += "Пароли должны совпадать\n";
        if (!mCbAgree.isChecked())
            statusText += "Подтвердите согласие с правилами\n";
        mTxStatus.setText(statusText);
        mBnRegister.setEnabled(statusText.equals(""));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FirstActivity.class));
        finish();
    }

    void register() {
        Toast.makeText(this, "Регистрация..", Toast.LENGTH_SHORT).show();
        mEtLogin.setEnabled(false);
        mEtName.setEnabled(false);
        mEtEmail.setEnabled(false);
        mEtPassword.setEnabled(false);
        mEtCPassword.setEnabled(false);
        mCbSpam.setEnabled(false);
        mCbAgree.setEnabled(false);
        mNetworkController.register(this,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean ok = jsonResponse.getBoolean("ok");
                        boolean not_reg = jsonResponse.getBoolean("not_registed");
                        if (ok && not_reg) {
                            JSONObject jsonUser = jsonResponse.getJSONObject("user_info");
                            User user = new User();
                            user.setId(jsonUser.getInt("id"));
                            user.setName(jsonUser.getString("name"));
                            user.setEmail(jsonUser.getString("email"));
                            user.setLogin(jsonUser.getString("login"));
                            user.setUrlImage(jsonUser.getString("urlImage"));
                            user.setGroup(jsonUser.getInt("groups"));
                            user.setPassword(jsonUser.getString("password"));
                            user.setToken(jsonUser.getString("token"));
                            user.setPro(jsonUser.getString("pro").equals("Yes"));
                            mSettingsController.saveUser(user);
                            mSettingsController.setFirst(false);
                            Toast.makeText(this, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            stop("Такой пользователь уже зарегистрирован");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "register: ", e);
                        stop("Произошла ошибка: " + e.getMessage());
                    }
                },
                error -> stop("Произошла ошибка: " + error.getMessage()),
                mEtLogin.getText().toString(), mEtName.getText().toString(), mEtEmail.getText().toString(), mEtPassword.getText().toString(), "" + mCbSpam.isChecked());
    }

    void stop(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mEtLogin.setEnabled(true);
        mEtName.setEnabled(true);
        mEtEmail.setEnabled(true);
        mEtPassword.setEnabled(true);
        mEtCPassword.setEnabled(true);
        mCbSpam.setEnabled(true);
        mCbAgree.setEnabled(true);
        check();
    }
}
