package edu.uph.uas_kelompok3;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class RegisterActivity extends AppCompatActivity {
    EditText edtNama, edtEmail, edtTanggalLahir, edtPassword, edtKonfPassword;
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
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtTanggalLahir = findViewById(R.id.edtTanggalLahir);
        edtPassword = findViewById(R.id.edtPassword);
        edtKonfPassword = findViewById(R.id.edtKonfPassword);
        btnRegister = findViewById(R.id.btnRegister);

        AutoCompleteTextView spnGender = findViewById(R.id.spnGender);
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
                String password = edtPassword.getText().toString();
                String konfirmasiPassword = edtKonfPassword.getText().toString();

                if (konfirmasiData(password, konfirmasiPassword) == true) {
                    toHome();
                } else {
                    edtKonfPassword.setError("Passwords do not match");
                }
            }
        });
    }

    public boolean konfirmasiData(String password, String konfirmasiPassword) {
        if (konfirmasiPassword.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public void toHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
