package com.example.faculties.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.faculties.R;
import com.example.faculties.activities.GroupInfoActivity;
import com.example.faculties.activities.GroupInfoActivity;
import com.example.faculties.adapters.GroupListAdapter;
import com.example.faculties.databinding.FragmentHomeBinding;
import com.example.faculties.model.Faculty;
import com.example.faculties.model.Group;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

public class FacultyFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Faculty faculty;

    public FacultyFragment(Faculty faculty) {
        this.faculty = faculty;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        textView.setText(faculty.getName());
        return root;
    }

    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroups = new ArrayList<>(); //NEW1011 start
        
        createGroupList(null);
    }

    ArrayList<Group> mGroups;
    GroupListAdapter mGroupListAdapter;

    public void createGroupList(View view) {
        ListView listView = getActivity().findViewById(R.id.lvGroupList2);
        mGroupListAdapter=new GroupListAdapter(mGroups,getActivity());//todo под вопросом
        listView.setAdapter(mGroupListAdapter);

        ((LinearLayout) getActivity().findViewById(R.id.llFacultyInput)).setVisibility(View.VISIBLE);


        AdapterView.OnItemClickListener clGroup = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mGroupListAdapter.getPosition() == position) mGroupListAdapter.setPosition(-1);
                else mGroupListAdapter.setPosition(position);
                mGroupListAdapter.notifyDataSetChanged();
            }
        };
        listView.setOnItemClickListener(clGroup);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.miFacultyAbout:{
                AlertDialog.Builder infoDialog = new AlertDialog.Builder(getActivity());
                infoDialog.setTitle("О группе");
                infoDialog.setMessage("Это список студентов группы");
                infoDialog.setCancelable(false);
                infoDialog.setPositiveButton("Прочитано", null);
                infoDialog.show();
                return true;
            }
            case R.id.miFacultyExit:{
                return true;
            }
            case R.id.miFacultyAdd:{

                Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
                mGroupListAdapter.setPosition(mGroups.size());
                mGroupListAdapter.notifyDataSetChanged();
                mIntentActivityResultLauncher.launch(intent);
                return true;
            }
            case R.id.miFacultyChange:{
                if (mGroupListAdapter.getPosition() != -1 && mGroupListAdapter.getPosition() < mGroups.size()) {
                    //передача аргументов должна быть
                } else Toast.makeText(getActivity(),
                        "Ошибка выбора", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.miFacultyDelete:{
                if (mGroupListAdapter.getPosition() != -1) mGroups.remove(mGroupListAdapter.getPosition());
                else Toast.makeText(getActivity(),
                        "Ошибка выбора", Toast.LENGTH_SHORT).show();
                mGroupListAdapter.setPosition(-1);
                mGroupListAdapter.notifyDataSetChanged();
                return true;
            }
            default:{}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}