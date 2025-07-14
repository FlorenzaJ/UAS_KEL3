package edu.uph.uas_kelompok3;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    EditText edtNama, edtEmail, edtTanggalLahir, edtPassword, edtKonfPassword;
    AutoCompleteTextView spnGender;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi view
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtTanggalLahir = findViewById(R.id.edtTanggalLahir);
        edtPassword = findViewById(R.id.edtPassword);
        edtKonfPassword = findViewById(R.id.edtKonfPassword);
        btnRegister = findViewById(R.id.btnRegister);
        spnGender = findViewById(R.id.spnGender);

        // Setup gender dropdown
        String[] genderOptions = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                genderOptions
        );
        spnGender.setAdapter(adapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    toHome();
                }
            }
        });
    }

    private boolean validateInputs() {
        String nama = edtNama.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String tanggalLahir = edtTanggalLahir.getText().toString().trim();
        String gender = spnGender.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String konfPassword = edtKonfPassword.getText().toString();

        boolean valid = true;

        if (nama.isEmpty()) {
            edtNama.setError("Nama tidak boleh kosong");
            valid = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email tidak valid");
            valid = false;
        }

        if (tanggalLahir.isEmpty()) {
            edtTanggalLahir.setError("Tanggal lahir wajib diisi");
            valid = false;
        }

        if (gender.isEmpty()) {
            spnGender.setError("Pilih gender terlebih dahulu");
            valid = false;
        }

        if (password.isEmpty() || password.length() < 6) {
            edtPassword.setError("Password minimal 6 karakter");
            valid = false;
        }

        if (!password.equals(konfPassword)) {
            edtKonfPassword.setError("Konfirmasi password tidak cocok");
            valid = false;
        }

        return valid;
    }

    public void toHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
