package ru.devdem.sendscaledphotososerver.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.devdem.sendscaledphotososerver.R;
import ru.devdem.sendscaledphotososerver.helpers.NetworkController;
import ru.devdem.sendscaledphotososerver.helpers.SettingsController;
import ru.devdem.sendscaledphotososerver.helpers.objects.ImageSend;
import ru.devdem.sendscaledphotososerver.helpers.objects.User;

public class UploadPhotosActivity extends AppCompatActivity {

    private static final String TAG = "UploadPhotosActivity";
    private static int REQUEST_ID = 228;
    private static int MAX_PHOTOS = 25;
    private TextView mTxGroup;
    private TextView mTxLesson;
    private Spinner mSpLessons;
    private RadioButton mRbHigh;
    private RadioButton mRbMedium;
    private RadioButton mRbLow;
    private LinearLayout mPhotosView;
    private TextView mTxNoPhotos;
    private TextView mTxTech;
    private Button mBtSend;
    private SettingsController mSettingsController;
    private NetworkController mNetworkController;
    private User mUser;

    private ArrayList<ImageSend> mImages = new ArrayList<>();
    private ArrayList<View> mImageViews = new ArrayList<>();
    private int mQuality;
    private ArrayList<String> mLessons = new ArrayList<>();
    private String mGroupName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setTitle("Отправить фотографии");
        mSettingsController = SettingsController.getInstance(this);
        mUser = mSettingsController.loadUser();
        mNetworkController = NetworkController.getNetworkController();
        mTxGroup = findViewById(R.id.groupText);
        mTxLesson = findViewById(R.id.lessonText);
        mSpLessons = findViewById(R.id.lessonsSpinner);
        mRbHigh = findViewById(R.id.rbHigh);
        mRbMedium = findViewById(R.id.rbMedium);
        mRbLow = findViewById(R.id.rbLow);
        TextView txHigh = findViewById(R.id.rbTxHigh);
        txHigh.setOnClickListener(v -> mRbHigh.toggle());
        mRbHigh.setVisibility(mUser.isPro() ? View.VISIBLE : View.GONE);
        txHigh.setVisibility(mUser.isPro() ? View.VISIBLE : View.GONE);
        TextView txMedium = findViewById(R.id.rbTxMedium);
        txMedium.setOnClickListener(v -> mRbMedium.toggle());
        TextView txLow = findViewById(R.id.rbTxLow);
        txLow.setOnClickListener(v -> mRbLow.toggle());
        mPhotosView = findViewById(R.id.listImages);
        mTxNoPhotos = findViewById(R.id.textNoPhotos);
        mTxTech = findViewById(R.id.techInfo);
        mBtSend = findViewById(R.id.btnSend);
        RadioGroup rgQuality = findViewById(R.id.rgQuality);
        rgQuality.setOnCheckedChangeListener((group, checkedId) -> {
            changedQuality(checkedId);
        });
        checkPhotos();
        switch (mSettingsController.getLastQuality()) {
            case 1:
                mRbHigh.toggle();
                break;
            case 2:
                mRbMedium.toggle();
                break;
            case 3:
                mRbLow.toggle();
                break;
            default:
                Toast.makeText(this, "Произошла неизвестная ошибка, но не критично :D", Toast.LENGTH_SHORT).show();
                break;
        }

        mNetworkController.getFilesInfo(this, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                if (status.equals("ok")) {
                    mGroupName = jsonResponse.getString("group_name");
                    mTxGroup.setText("Ваша группа: " + mGroupName);
                    JSONObject jsonLessons = jsonResponse.getJSONObject("lessons");
                    int count = jsonLessons.getInt("count");
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
                    adapter.add("Выберите предмет");
                    mLessons.add("Выберите предмет");
                    for (int i = 0; i < count; i++) {
                        String lessonName = jsonLessons.getString("" + i);
                        adapter.add(lessonName);
                        mLessons.add(lessonName);
                    }
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpLessons.setAdapter(adapter);
                } else if (status.equals("INVALID_TOKEN")) {
                    Toast.makeText(this, "Неверный токен. Перепроверка..", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else if (status.equals("NO_TOKEN")) {
                    Toast.makeText(this, "Произошла ошибка. Выход из аккаунта..", Toast.LENGTH_SHORT).show();
                    mSettingsController.clearAccount();
                    startActivity(new Intent(this, SplashActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Произошла неизвестная ошибка.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "checkData: " + response, new Exception());
                }
            } catch (Exception e) {
                Log.e(TAG, "onCreate: response try error", e);
                Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }, error -> {
            Toast.makeText(this, "Произошла сетевая ошибка: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: error network: ", error);
            onBackPressed();
        }, mUser.getToken());
    }

    void changedQuality(int checkedId) {
        int quality = 0;
        switch (checkedId) {
            case R.id.rbHigh:
                quality = 1;
                break;
            case R.id.rbMedium:
                quality = 2;
                break;
            case R.id.rbLow:
                quality = 3;
                break;
            default:
                Log.e(TAG, "changedQuality: хз что за хрень выбрана", new IndexOutOfBoundsException());
                break;
        }
        mQuality = quality;
        mSettingsController.saveLastQuality(mQuality);
        checkPhotos();
    }

    void checkPhotos() {
        mPhotosView.removeAllViews();
        for (ImageSend imageSend : mImages) {
            View view = View.inflate(this, R.layout.image_item, null);
            ImageView imageView = view.findViewById(R.id.imageView);
            ImageButton btnDelete = view.findViewById(R.id.imageDelete);
            TextView textView = view.findViewById(R.id.textInfo);
            String text = "Сжатый размер: " + imageSend.getPreparedSize() + " Кбайт\n" +
                    "Оригинальное разрешение: " + imageSend.getDimens()[0] + "x" + imageSend.getDimens()[1] + "\n" +
                    "Сжатое разрешение: " + imageSend.getPreparedDimens()[0] + "x" + imageSend.getPreparedDimens()[1];
            textView.setText(text);
            imageView.setImageBitmap(imageSend.getPreparedBitmap());
            mPhotosView.addView(view);
        }
        mTxNoPhotos.setVisibility(mImages.size() >= 1 ? View.GONE : View.VISIBLE);
        mBtSend.setEnabled(mImages.size() >= 1);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void addPhotosIntent(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, REQUEST_ID);
    }

    @Override
    protected void onDestroy() {
        mImages.clear();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Обработка фотографии")
                    .setMessage("Пожалуйста, подождите")
                    .setCancelable(false)
                    .create();
            dialog.show();
            new Thread(null, () -> {
                try {
                    if (path == null) throw new FileNotFoundException();
                    InputStream imageStream = getContentResolver().openInputStream(path);
                    if (imageStream == null) throw new FileNotFoundException();
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ImageSend image = new ImageSend();
                    image.setBitmap(bitmap);
                    Bitmap scaledBitmap;
                    ByteArrayOutputStream outputStream;
                    image.setDimens(bitmap.getWidth(), bitmap.getHeight());
                    int size;
                    int needSize;
                    int count = 0;
                    do {
                        count++;
                        double needX = bitmap.getWidth() / (count * 1.05);
                        double needY = bitmap.getHeight() / (count * 1.1);
                        scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) needX, (int) needY, true);
                        outputStream = new ByteArrayOutputStream();
                        switch (mQuality) {
                            case 1:
                                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                needSize = 512;
                                break;
                            case 2:
                                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                                needSize = 324;
                                break;
                            case 3:
                                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                                needSize = 102;
                                break;
                            default:
                                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                                needSize = 102;
                                break;
                        }
                        byte[] bytes = outputStream.toByteArray();
                        size = (bytes.length / 1024);
                    } while (size >= needSize);
                    image.setPreparedBitmap(BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.toByteArray().length));
                    image.setPreparedSize(size);
                    image.setPreparedDimens(new int[]{scaledBitmap.getWidth(), scaledBitmap.getHeight()});
                    mImages.add(image);
                    runOnUiThread(this::checkPhotos);
                    dialog.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Произошла ошибка. " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }, "Image editor").start();
        }
    }
}
