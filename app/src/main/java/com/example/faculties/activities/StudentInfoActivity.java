package com.example.faculties.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faculties.R;
import com.example.faculties.adapters.SubjectListAdapter;
import com.example.faculties.model.Student;
import com.example.faculties.model.Subject;

public class StudentInfoActivity extends AppCompatActivity {

    private SubjectListAdapter mSubjectListAdapter;
    private Student s;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.info_miAdd:{
                mSubjectListAdapter.setPosition(s.getSubjects().size());
                mSubjectListAdapter.notifyDataSetChanged();
                addSubject();
                return true;
            }
            case R.id.info_miChange:{
                if (mSubjectListAdapter.getPosition() != -1 && mSubjectListAdapter.getPosition() < s.getSubjects().size()) {
                    addSubject();
                    mSubjectListAdapter.notifyDataSetChanged();
                } else Toast.makeText(getApplicationContext(),
                        "Ошибка выбора", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.info_miDelete:{
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(StudentInfoActivity.this);

                inputDialog.setCancelable(false);
                if (mSubjectListAdapter.getPosition() != -1 && mSubjectListAdapter.getPosition() < s.getSubjects().size()) {
                    inputDialog.setTitle("Вы уверены?");
                    inputDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            s.getSubjects().remove(mSubjectListAdapter.getPosition());
                            mSubjectListAdapter.setPosition(-1);
                            mSubjectListAdapter.notifyDataSetChanged();
                        }
                    })
                            .setNegativeButton("Отмена",null);
                    inputDialog.show();
                }
                else Toast.makeText(getApplicationContext(),
                        "Ошибка выбора", Toast.LENGTH_SHORT).show();
                return true;
            }
            default:{}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        ListView listView = findViewById(R.id.lvASI_Subjects);
        s = getIntent().getParcelableExtra("student");

        if (s == null) {
            s = new Student("", "", "");
        }
        ((TextView) findViewById(R.id.editDialog_FIO)).setText(s.getFIO());
        ((TextView) findViewById(R.id.editDialog_Faculty)).setText(s.getFaculty());
        ((TextView) findViewById(R.id.editDialog_Group)).setText(s.getGroup());
        mSubjectListAdapter = new SubjectListAdapter(s.getSubjects(), StudentInfoActivity.this);
        listView.setAdapter(mSubjectListAdapter);
        AdapterView.OnItemClickListener clSubject = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(mSubjectListAdapter.getPosition() == position) mSubjectListAdapter.setPosition(-1);
                else mSubjectListAdapter.setPosition(position);
                mSubjectListAdapter.notifyDataSetChanged();
            }
        };
        listView.setOnItemClickListener(clSubject);

    }


    public void addSubject() {
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.subject_input, null);
        final EditText mName = vv.findViewById(R.id.editDialog_SubjectName);
        final Spinner mMark = vv.findViewById(R.id.sDialog_Mark);
        if (mSubjectListAdapter.getPosition() != -1 && mSubjectListAdapter.getPosition() < s.getSubjects().size()){
            mName.setText(s.getSubjects().get(mSubjectListAdapter.getPosition()).getName());
            mMark.setSelection(s.getSubjects().get(mSubjectListAdapter.getPosition()).getMark()-2);
        }
        AlertDialog inputDialog = new AlertDialog.Builder(StudentInfoActivity.this)
                .setTitle("Информация о дисциплине")
                .setCancelable(false)
                .setView(vv)
                .setPositiveButton("Сохранить", null)
                .setNegativeButton("Отмена",null)
                .create();
        inputDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ((AlertDialog) inputDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSubjectListAdapter.getPosition() != -1 && mSubjectListAdapter.getPosition() < s.getSubjects().size()){
                            s.getSubjects().get(mSubjectListAdapter.getPosition()).setName(mName.getText().toString());
                            s.getSubjects().get(mSubjectListAdapter.getPosition()).setMark(Integer.parseInt(mMark.getSelectedItem().toString()));
                        }
                        else {
                            if (mName.getText().toString().isEmpty()) mName.setError("Не указано название дисциплины");
                            else s.addSubject(new Subject(
                                    mName.getText().toString(),
                                    Integer.parseInt(mMark.getSelectedItem().toString())
                            ));
                        }
                        mSubjectListAdapter.notifyDataSetChanged();
                        if (!mName.getText().toString().isEmpty()) inputDialog.dismiss();
                    }
                });
            }
        });
        inputDialog.show(); //NEW1011 end
    }

    public void clSave(View view) {
        if (((TextView) findViewById(R.id.editDialog_FIO)).getText().toString().isEmpty())
            ((EditText) findViewById(R.id.editDialog_FIO)).setError("Не указан ФИО");
        else s.setFIO(((TextView) findViewById(R.id.editDialog_FIO)).getText().toString());

        if (((TextView) findViewById(R.id.editDialog_Faculty)).getText().toString().isEmpty())
            ((EditText) findViewById(R.id.editDialog_Faculty)).setError("Не указан факультет");
        else s.setFaculty(((TextView) findViewById(R.id.editDialog_Faculty)).getText().toString());

        if (((TextView) findViewById(R.id.editDialog_Group)).getText().toString().isEmpty())
            ((EditText) findViewById(R.id.editDialog_Group)).setError("Не указана группа");
        else s.setGroup(((TextView) findViewById(R.id.editDialog_Group)).getText().toString());
        if (!s.getFIO().isEmpty() && !s.getFaculty().isEmpty() && !s.getGroup().isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra("student", s);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void clExit(View view) {
        finish();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle("Сохранить изменения?");
        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clSave(null);
            }
        })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clExit(null);
                    }
                })
                .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
        quitDialog.show();
    }
}