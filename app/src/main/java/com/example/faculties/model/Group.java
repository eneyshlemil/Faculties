package com.example.faculties.model;

public class Group {
    private String mName;

    public Group(String name) {
        this.mName = name;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "mName='" + mName + '\'' +
                '}';
    }
}
