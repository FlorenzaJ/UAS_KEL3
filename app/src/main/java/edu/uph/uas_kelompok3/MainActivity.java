package edu.uph.uas_kelompok3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import edu.uph.uas_kelompok3.databinding.ActivityMainBinding;
import edu.uph.uas_kelompok3.Model.UserModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private String userData_nama, userData_email, userData_gender, userData_tanggalLahir;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        userData_nama = prefs.getString("nama", "N/A");
        userData_email = prefs.getString("email", "N/A");
        userData_gender = prefs.getString("gender", "N/A");
        userData_tanggalLahir = prefs.getString("tanggalLahir", "N/A");

        Intent intent = getIntent();
        if (intent != null) {
            userData_nama = intent.getStringExtra("nama");
            userData_email = intent.getStringExtra("email");
            userData_gender = intent.getStringExtra("gender");
            userData_tanggalLahir = intent.getStringExtra("tanggalLahir");

            getSharedPreferences("UserData", MODE_PRIVATE)
                    .edit()
                    .putString("nama", userData_nama)
                    .putString("email", userData_email)
                    .putString("gender", userData_gender)
                    .putString("tanggalLahir", userData_tanggalLahir)
                    .apply();
        }

        if (userData_nama == null) {
            userData_nama = getSharedPreferences("UserData", MODE_PRIVATE).getString("nama", "N/A");
            userData_email = getSharedPreferences("UserData", MODE_PRIVATE).getString("email", "N/A");
            userData_gender = getSharedPreferences("UserData", MODE_PRIVATE).getString("gender", "N/A");
            userData_tanggalLahir = getSharedPreferences("UserData", MODE_PRIVATE).getString("tanggalLahir", "N/A");
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

        getSharedPreferences("UserData", MODE_PRIVATE)
                .edit()
                .putString("nama", nama)
                .putString("email", email)
                .putString("gender", gender)
                .putString("tanggalLahir", tanggalLahir)
                .apply();
    }

    public UserModel getUserModel() {
        return new UserModel(
                getUserNama(),
                getUserEmail(),
                getUserGender(),
                getUserTanggalLahir()
        );
    }
}