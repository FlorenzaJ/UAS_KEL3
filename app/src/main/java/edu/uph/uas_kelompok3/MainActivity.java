package edu.uph.uas_kelompok3;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.uph.uas_kelompok3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private String userData_nama, userData_email, userData_gender, userData_tanggalLahir;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            userData_nama = intent.getStringExtra("nama");
            userData_email = intent.getStringExtra("email");
            userData_gender = intent.getStringExtra("gender");
            userData_tanggalLahir = intent.getStringExtra("tanggalLahir");
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }

    public String getUserNama() {
        return userData_nama != null ? userData_nama : "N/A";
    }

    public String getUserEmail() {
        return userData_email != null ? userData_email : "N/A";
    }

    public String getUserGender() {
        return userData_gender != null ? userData_gender : "N/A";
    }

    public String getUserTanggalLahir() {
        return userData_tanggalLahir != null ? userData_tanggalLahir : "N/A";
    }

    public void updateUserData(String nama, String email, String gender, String tanggalLahir) {
        userData_nama = nama;
        userData_email = email;
        userData_gender = gender;
        userData_tanggalLahir = tanggalLahir;

        // Optionally save to SharedPreferences for persistence
        getSharedPreferences("UserData", MODE_PRIVATE)
                .edit()
                .putString("nama", nama)
                .putString("email", email)
                .putString("gender", gender)
                .putString("tanggalLahir", tanggalLahir)
                .apply();
    }
}