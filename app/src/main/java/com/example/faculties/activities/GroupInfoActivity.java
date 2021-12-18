package com.example.faculties.activities;

//фио студентов и тд

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.faculties.R;
import com.example.faculties.adapters.StudentListAdapter;
import com.example.faculties.model.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class GroupInfoActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);



        mStudents = new ArrayList<>(); //NEW1011 start

        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        int size = sPref.getInt("Count", 0);
        if (size>0){
            Gson gson = (new GsonBuilder()).create();
            for (int i=0; i<size;++i){
                String s = sPref.getString("Student"+i, "");
                if (!s.equals("")){
                    Student st = gson.fromJson(s,Student.class);
                    mStudents.add(st);
                }
            }
        }
        createStudentList(null);
        //NEW1011 end

        mIntentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            Student s = intent.getParcelableExtra("student");
                            if (mStudentListAdapter.getPosition() == mStudents.size()) mStudents.add(s);
                            else mStudents.set(mStudentListAdapter.getPosition(), s);
                            Toast.makeText(getApplicationContext(),
                                    "Студент3: " + s.toString() + "\nУспешно сохранён", Toast.LENGTH_SHORT).show();
                            mStudentListAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );
    }

    ArrayList<Student> mStudents;
    StudentListAdapter mStudentListAdapter;

    public void createStudentList(View view) {
        ListView listView = findViewById(R.id.lvGroupList2);
        mStudentListAdapter=new StudentListAdapter(mStudents,this);
        listView.setAdapter(mStudentListAdapter);

        ((LinearLayout) findViewById(R.id.llGroupInput)).setVisibility(View.VISIBLE);


        AdapterView.OnItemClickListener clStudent = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mStudentListAdapter.getPosition() == position) mStudentListAdapter.setPosition(-1);
                else mStudentListAdapter.setPosition(position);
                mStudentListAdapter.notifyDataSetChanged();
            }
        };
        listView.setOnItemClickListener(clStudent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.miGroupAbout:{
                AlertDialog.Builder infoDialog = new AlertDialog.Builder(GroupInfoActivity.this);
                infoDialog.setTitle("О группе");
                infoDialog.setMessage("Это список студентов группы");
                infoDialog.setCancelable(false);
                infoDialog.setPositiveButton("Прочитано", null);
                infoDialog.show();
                return true;
            }
            case R.id.miGroupExit:{
                finish();
                return true;
            }
            case R.id.miGroupAdd:{

                Intent intent = new Intent(GroupInfoActivity.this, StudentInfoActivity.class);
                mStudentListAdapter.setPosition(mStudents.size());
                mStudentListAdapter.notifyDataSetChanged();
                mIntentActivityResultLauncher.launch(intent);
                return true;
            }
            case R.id.miGroupChange:{
                if (mStudentListAdapter.getPosition() != -1 && mStudentListAdapter.getPosition() < mStudents.size()) {
                    Intent intent = new Intent(GroupInfoActivity.this, StudentInfoActivity.class);
                    intent.putExtra("student", mStudents.get(mStudentListAdapter.getPosition()));
                    mIntentActivityResultLauncher.launch(intent);
                } else Toast.makeText(getApplicationContext(),
                        "Ошибка выбора", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.miGroupDelete:{
                if (mStudentListAdapter.getPosition() != -1) mStudents.remove(mStudentListAdapter.getPosition());
                else Toast.makeText(getApplicationContext(),
                        "Ошибка выбора", Toast.LENGTH_SHORT).show();
                mStudentListAdapter.setPosition(-1);
                mStudentListAdapter.notifyDataSetChanged();
                return true;
            }
            default:{}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        if (mStudents != null){
            SharedPreferences.Editor ed = getPreferences(MODE_PRIVATE).edit();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            ed.putInt("Count", mStudents.size());
            for (int i=0;i<mStudents.size();i++){
                String s = gson.toJson(mStudents.get(i));
                ed.putString("Student"+i,s);
            }
            ed.commit();
        }
        super.onDestroy();
    }
}
