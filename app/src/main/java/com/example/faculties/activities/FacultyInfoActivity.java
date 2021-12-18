package com.example.faculties.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.example.faculties.R;
import com.example.faculties.model.Faculty;
import com.example.faculties.fragments.FacultyFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.faculties.databinding.ActivityFacultyInfoBinding;

import java.util.Arrays;
import java.util.List;

public class FacultyInfoActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private List<Faculty> faculties;
    private ActivityFacultyInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFacultyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        this.faculties = fetchFaculties();
        generateFacultiesMenu(this.faculties);
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.faculty_information);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    protected void generateFacultiesMenu(List<Faculty> faculties) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        for (Faculty faculty : faculties) {
            menu.add(faculty.getName()).setOnMenuItemClickListener(
                    new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            FacultyFragment fragment = new FacultyFragment(faculty);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.faculty_information, fragment);
                            ft.commit();
                            return false;
                        }
                    }
            ).setCheckable(true);
        }
    }

    protected List<Faculty> fetchFaculties() {
        Faculty fktipm = new Faculty(1, "ФКТиПМ");
        Faculty matfak = new Faculty(2, "Матфак");
        Faculty fismo = new Faculty(3, "ФИСМО");
        Faculty fiztech = new Faculty(4, "ФизТех");
        Faculty filfak = new Faculty(5, "ФилФак");
        return Arrays.asList(fktipm, matfak, fismo, fiztech, filfak);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.faculty_information);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}