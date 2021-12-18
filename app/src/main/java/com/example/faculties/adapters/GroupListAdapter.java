package com.example.faculties.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faculties.R;
import com.example.faculties.model.Group;

import java.util.ArrayList;

public class GroupListAdapter extends BaseAdapter {
    ArrayList<Group> mGroups = new ArrayList<>();
    Context mContext;
    LayoutInflater mInflater;
    private int mPosition = -1;

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public int getPosition() {
        return mPosition;
    }

    public GroupListAdapter(ArrayList<Group> groups, Context context) {
        mGroups = groups;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() { return mGroups.size(); }

    @Override
    public Object getItem(int position){ return mGroups.get(position); }

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View view, ViewGroup parent){
        view = mInflater.inflate(R.layout.group_element, parent, false);
        if (mGroups.isEmpty()) return view;

        ((TextView) view.findViewById(R.id.tvStudentElementGroupName)).setText(mGroups.get(position).getmName());
        if (position == mPosition) {
            ((TextView) view.findViewById(R.id.tvStudentElementGroupName)).setTextColor(
                    mContext.getResources().getColor(R.color.red)
            );
        }
        return view;
    }
}
