package ru.devdem.sendscaledphotososerver.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONObject;

import ru.devdem.sendscaledphotososerver.R;
import ru.devdem.sendscaledphotososerver.helpers.NetworkController;
import ru.devdem.sendscaledphotososerver.helpers.SettingsController;
import ru.devdem.sendscaledphotososerver.helpers.objects.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private NetworkController mNetworkController;
    private SettingsController mSettingsController;
    private User mUser;
    private TextView mTxLogin;
    private TextView mTxEmail;
    private TextView mTxPro;
    private TextView mTxAllMem;
    private TextView mTxAvailMem;
    private ProgressBar mPbMem;
    private TextView mTxMaxPhotos;
    private Button mBtUploadPhotos;
    private Button mBtLeaveGroup;
    private Button mBtLogOut;
    private boolean can = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mNetworkController = NetworkController.getNetworkController();
        mSettingsController = SettingsController.getInstance(this);
        mUser = mSettingsController.loadUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxLogin = findViewById(R.id.textLogin);
        mTxEmail = findViewById(R.id.textEmail);
        mTxPro = findViewById(R.id.textPro);
        TextView txHowPro = findViewById(R.id.textHowPro);
        mTxAllMem = findViewById(R.id.textAllMem);
        mTxAvailMem = findViewById(R.id.textAvailMem);
        mPbMem = findViewById(R.id.progressMem);
        mTxMaxPhotos = findViewById(R.id.textMaxPhotos);
        txHowPro.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Как получить PRO-статус?")
                    .setMessage("В этом приложении недоступно оформление подписки PRO-статуса.\n" +
                            "Это доступно в основном приложении проекта \"Расписание онлайн\".")
                    .setPositiveButton("Открыть", (dialog1, which) -> {
                        Uri address = Uri.parse("https://play.google.com/apps/testing/ru.devdem.reminder");
                        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                        if (openLinkIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(openLinkIntent);
                        } else {
                            Log.d("Intent", "Не получается обработать намерение!");
                        }
                        dialog1.cancel();
                    })
                    .setNegativeButton("Отмена", null).create();
            dialog.show();
        });
        mBtUploadPhotos = findViewById(R.id.btnUploadPhotos);
        mBtLeaveGroup = findViewById(R.id.btnLeaveGroup);
        mBtLogOut = findViewById(R.id.btnLogout);
        mBtLogOut.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Подтвердите действие")
                    .setMessage("Вы уверены, что хотите выйти?")
                    .setNegativeButton("Выйти", (dialog1, which) -> {
                        mSettingsController.clearAccount();
                        startActivity(new Intent(this, SplashActivity.class));
                        finish();
                    })
                    .setPositiveButton("Нет", null).create();
            dialog.show();
        });
        mBtLeaveGroup.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Подтвердите действие")
                    .setMessage("Вы уверены, что хотите выйти из группы?")
                    .setNegativeButton("Выйти", (dialog1, which) -> leaveFromGroup())
                    .setPositiveButton("Нет", null).create();
            dialog.show();
        });
        setTitle("Управление");
        mTxLogin.setText("Загрузка");
        mTxEmail.setText("Загрузка");
        mTxPro.setText("Загрузка");
        mTxAllMem.setText("Загрузка");
        mTxAvailMem.setText("Загрузка");
        mTxMaxPhotos.setText("Загрузка");
        mPbMem.setProgress(0);
        login();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_reload) {
            if (can) {
                mTxLogin.setText("Загрузка");
                mTxEmail.setText("Загрузка");
                mTxPro.setText("Загрузка");
                mTxAllMem.setText("Загрузка");
                mTxAvailMem.setText("Загрузка");
                mPbMem.setProgress(0);
                mTxMaxPhotos.setText("Загрузка");
                mBtUploadPhotos.setEnabled(false);
                mBtLogOut.setEnabled(false);
                mBtLeaveGroup.setEnabled(false);
                login();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String formatSize(int size) {
        String[] metrics = new String[]{"байт", "Кбайт", "Мбайт", "Гбайт", "Тбайт"};
        int metric = 0;
        while (Math.floor((float) size / 1000) > 0) {
            metric++;
            size /= 1000;
        }
        return Math.round(size) + " " + metrics[metric];
    }

    @SuppressLint("SetTextI18n")
    void checkData() {
        mTxLogin.setText("Загрузка");
        mTxEmail.setText("Загрузка");
        mTxPro.setText("Загрузка");
        mTxAllMem.setText("Загрузка");
        mTxAvailMem.setText("Загрузка");
        mPbMem.setProgress(0);
        mTxMaxPhotos.setText("Загрузка");
        mNetworkController.getFilesInfo(this, response -> {
            mTxLogin.setText("Логин: " + mUser.getLogin());
            mTxEmail.setText("Email: " + mUser.getEmail());
            mTxPro.setText("PRO-статус: " + (mUser.isPro() ? "активирован" : "нет"));
            try {
                JSONObject jsonResponse = new JSONObject(response);
                mTxAllMem.setText("Всего памяти выделено: " + jsonResponse.getString("allText"));
                mTxAvailMem.setText("Свободно памяти: " + jsonResponse.getString("availText"));
                int availableSize = jsonResponse.getInt("availableSize");
                int allSize = jsonResponse.getInt("allSize");
                int photosMax = jsonResponse.getInt("photosMax");
                int usedSize = allSize - availableSize;
                mPbMem.setMax(allSize);
                mPbMem.setProgress(usedSize);
                mTxMaxPhotos.setText("Расчитанно на: " + photosMax + " фотографий\n" +
                        "Это в среднем по " + formatSize(allSize / photosMax));
                mBtUploadPhotos.setEnabled(true);
                mBtLogOut.setEnabled(true);
                mBtLeaveGroup.setEnabled(true);
            } catch (Exception e) {
                Log.e(TAG, "checkData: ", e);
            }
            can = true;
        }, error -> {
            Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
            can = true;
            checkData();
            mBtUploadPhotos.setEnabled(true);
            mBtLogOut.setEnabled(true);
            mBtLeaveGroup.setEnabled(true);
        }, mUser.getToken());
    }

    void login() {
        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                if (jsonResponse.getBoolean("password_ok") && jsonResponse.getBoolean("ok")) {
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
                    checkData();
                    if (mSettingsController.loadUser().getGroup() == 0) {
                        startActivity(new Intent(this, GroupListActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Недействительный аккаунт. Перезайдите", Toast.LENGTH_SHORT).show();
                    mSettingsController.clearAccount();
                    startActivity(new Intent(this, SplashActivity.class));
                    finish();
                }
            } catch (Exception e) {
                Log.e(TAG, "login: ", e);
            }
        };

        mNetworkController.login(this, listener, null, mUser.getLogin(), mUser.getToken());
    }

    public void leaveFromGroup() {
        mBtUploadPhotos.setEnabled(false);
        mBtLogOut.setEnabled(false);
        mBtLeaveGroup.setEnabled(false);
        Toast.makeText(this, "Выход из группы..", Toast.LENGTH_SHORT).show();
        Response.Listener<String> listener = response -> {
            Log.d(TAG, "joinToGroup: " + response);
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                String error = jsonResponse.getString("error");
                if (status.equals("error")) {
                    if (error.equals("WRONG_TOKEN")) {
                        Toast.makeText(this, "Недействительный аккаунт. Перезайдите", Toast.LENGTH_SHORT).show();
                        mSettingsController.clearAccount();
                        startActivity(new Intent(this, SplashActivity.class));
                        finish();
                    }
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                } else if (status.equals("JOINED")) {
                    mSettingsController.clearGroup();
                    startActivity(new Intent(this, SplashActivity.class));
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                mBtUploadPhotos.setEnabled(true);
                mBtLogOut.setEnabled(true);
                mBtLeaveGroup.setEnabled(true);
            }
        };
        Response.ErrorListener errorListener = error -> {
            Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            mBtUploadPhotos.setEnabled(true);
            mBtLogOut.setEnabled(true);
            mBtLeaveGroup.setEnabled(true);
        };
        mNetworkController.joinToGroup(this, listener, errorListener, "0", mSettingsController.loadUser().getToken());
    }
}
