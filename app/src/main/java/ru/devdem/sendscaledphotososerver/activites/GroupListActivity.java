package ru.devdem.sendscaledphotososerver.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.devdem.sendscaledphotososerver.R;
import ru.devdem.sendscaledphotososerver.helpers.GroupsAdapter;
import ru.devdem.sendscaledphotososerver.helpers.NetworkController;
import ru.devdem.sendscaledphotososerver.helpers.SettingsController;
import ru.devdem.sendscaledphotososerver.helpers.objects.Group;
import ru.devdem.sendscaledphotososerver.helpers.objects.User;

public class GroupListActivity extends AppCompatActivity {

    private static final String TAG = "GroupListActivity";
    private NetworkController mNetworkController;
    private RecyclerView mGroupsView;
    private GroupsAdapter mGroupsAdapter;
    private SettingsController mSettingsController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNetworkController = NetworkController.getNetworkController();
        mSettingsController = SettingsController.getInstance(this);
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_grouplist, null);
        mGroupsView = view.findViewById(R.id.rv);
        mGroupsView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mGroupsView.setLayoutManager(llm);
        mGroupsAdapter = new GroupsAdapter();
        setContentView(view);
        setTitle("Выберите группу");
        updateGroups();
    }

    void updateGroups() {
        Toast.makeText(this, "Загрузка групп..", Toast.LENGTH_SHORT).show();
        Response.Listener<String> listener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int all = jsonObject.getInt("all");
                ArrayList<Group> groups = new ArrayList<>();
                for (int i = 0; i < all; i++) {
                    JSONObject groupjson = jsonObject.getJSONObject(String.valueOf(i));
                    Group group = new Group();
                    int id = groupjson.getInt("id");
                    String name = groupjson.getString("name");
                    String city = groupjson.getString("city");
                    String building = groupjson.getString("building");
                    String description = groupjson.getString("description");
                    String urlImage = groupjson.getString("urlImage");
                    String confirmed = groupjson.getString("confirmed");
                    String date_created = groupjson.getString("date_created");
                    group.setId(id);
                    group.setName(!name.equals("null") ? name : "");
                    group.setCity(!city.equals("null") ? city : "");
                    group.setBuilding(!building.equals("null") ? building : "");
                    group.setDescription(!description.equals("null") ? description : "Нет описания");
                    group.setUrl(!urlImage.equals("null") ? urlImage : "");
                    group.setConfirmed(confirmed.equals("Yes"));
                    group.setAuthor(new User());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    group.setDateCreated(!date_created.equals("null") ? format.parse(date_created) : new Date());
                    groups.add(group);
                }
                mGroupsAdapter.setGroups(groups, this);
                mGroupsView.setAdapter(mGroupsAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> Toast.makeText(this, "Произошла ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        mNetworkController.getGroups(this, listener, errorListener);
    }

    public void joinToGroup(int id) {
        Toast.makeText(this, "Вход в группу..", Toast.LENGTH_SHORT).show();
        Response.Listener<String> listener = response -> {
            Log.d(TAG, "joinToGroup: " + response);
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                String error = jsonResponse.getString("error");
                if (status.equals("error")) {
                    if (error.equals("WRONG_TOKEN")) {
                        mSettingsController.clearAccount();
                        restart();
                    }
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                } else if (status.equals("JOINED")) {
                    JSONObject group_info = jsonResponse.getJSONObject("group_info");
                    mSettingsController.updateGroupData(group_info);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        mNetworkController.joinToGroup(this, listener, errorListener, String.valueOf(id), mSettingsController.loadUser().getToken());
    }

    void restart() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
