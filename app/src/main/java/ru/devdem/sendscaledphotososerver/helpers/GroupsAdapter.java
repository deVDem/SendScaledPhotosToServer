package ru.devdem.sendscaledphotososerver.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.devdem.sendscaledphotososerver.R;
import ru.devdem.sendscaledphotososerver.activites.GroupListActivity;
import ru.devdem.sendscaledphotososerver.helpers.objects.Group;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewer> {

    private ArrayList<Group> mGroups;
    private GroupListActivity mActivity;

    public void setGroups(ArrayList<Group> groups, GroupListActivity activity) {
        mGroups = groups;
        mActivity = activity;
    }

    @NonNull
    public GroupViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new GroupViewer(v);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewer holder, int position) {
        Group group = mGroups.get(position);
        holder.mGroupName.setText(group.getName());
        holder.mBuilding.setText(group.getBuilding());
        holder.mCity.setText(group.getCity());
        String description = group.getDescription();
        Date groupCreated = group.getDateCreated();
        if (groupCreated != null) {
            Calendar calendar_now = Calendar.getInstance();
            calendar_now.setTime(new Date());
            int year = calendar_now.YEAR;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(groupCreated);
            int yearGroup = calendar.YEAR;
            String dateString;
            if (year != yearGroup)
                dateString = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(groupCreated);
            else
                dateString = new SimpleDateFormat("d MMMM", Locale.getDefault()).format(groupCreated);
            description += "\nДата создания: " + dateString;
        }
        holder.mDescription.setText(description);
        holder.mGoButton.setOnClickListener(v -> mActivity.joinToGroup(group.getId()));
        if (position == mGroups.size() - 1) holder.mSpace.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    static class GroupViewer extends RecyclerView.ViewHolder {

        RelativeLayout mRelativeLayout;
        TextView mGroupName;
        TextView mBuilding;
        TextView mDescription;
        TextView mCity;
        Button mGoButton;
        Space mSpace;

        GroupViewer(View itemView) {
            super(itemView);
            mRelativeLayout = itemView.findViewById(R.id.relativeLayoutCard);
            mGroupName = itemView.findViewById(R.id.textViewTitle);
            mBuilding = itemView.findViewById(R.id.textViewBuilding);
            mDescription = itemView.findViewById(R.id.textViewSubTitle);
            mGoButton = itemView.findViewById(R.id.btnJoinToGroup);
            mSpace = itemView.findViewById(R.id.space);
            mCity = itemView.findViewById(R.id.textViewCity);
        }
    }
}
