package com.example.faculties.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faculties.R;
import com.example.faculties.model.Student;

import java.util.ArrayList;

public class GroupListAdapter extends BaseAdapter {
    ArrayList<Student> mStudents = new ArrayList<>();
    Context mContext;
    LayoutInflater mInflater;
    private int mPosition = -1;

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public int getPosition() {
        return mPosition;
    }

    public GroupListAdapter(ArrayList<Student> students, Context context) {
        mStudents = students;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() { return mStudents.size(); }

    @Override
    public Object getItem(int position){ return mStudents.get(position); }

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View view, ViewGroup parent){
        view = mInflater.inflate(R.layout.student_element, parent, false);
        if (mStudents.isEmpty()) return view;

        ((TextView) view.findViewById(R.id.tvElementFIO)).setText(mStudents.get(position).getFIO());
        ((TextView) view.findViewById(R.id.tvElementFaculty)).setText(mStudents.get(position).getFaculty());
        ((TextView) view.findViewById(R.id.tvElementGroup)).setText(mStudents.get(position).getGroup());
        if (position == mPosition) {
            ((TextView) view.findViewById(R.id.tvElementFIO)).setTextColor(
                    mContext.getResources().getColor(R.color.red)
            );
            ((TextView) view.findViewById(R.id.tvElementFaculty)).setTextColor(
                    mContext.getResources().getColor(R.color.red)
            );
            ((TextView) view.findViewById(R.id.tvElementGroup)).setTextColor(
                    mContext.getResources().getColor(R.color.red)
            );
            ((LinearLayout) view.findViewById(R.id.llElement)).setBackgroundColor(
                    mContext.getResources().getColor(R.color.white));
        }
        return view;
    }
}