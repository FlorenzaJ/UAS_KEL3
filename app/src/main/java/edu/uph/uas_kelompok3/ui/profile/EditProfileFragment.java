package edu.uph.uas_kelompok3.ui.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Calendar;

import edu.uph.uas_kelompok3.MainActivity;
import edu.uph.uas_kelompok3.R;

public class EditProfileFragment extends Fragment {

    private EditText edtFullName, edtDateOfBirth, edtEmail;
    private Spinner spnGender;
    private ImageView btnBack;
    private TextView btnSave;
    private Button btnSaveChanges;

    private String originalNama, originalEmail, originalGender, originalTanggalLahir;

    public EditProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        initializeViews(view);
        setupGenderSpinner();
        loadCurrentData();
        setupClickListeners();
        return view;
    }

    private void initializeViews(View view) {
        edtFullName = view.findViewById(R.id.edtFullName);
        edtDateOfBirth = view.findViewById(R.id.edtDateOfBirth);
        edtEmail = view.findViewById(R.id.edtEmail);
        spnGender = view.findViewById(R.id.spnGender);
        btnBack = view.findViewById(R.id.btnBack);
        btnSave = view.findViewById(R.id.btnSave);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
    }

    private void setupGenderSpinner() {
        String[] genderOptions = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                genderOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(adapter);
    }

    private void loadCurrentData() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();

            originalNama = mainActivity.getUserNama();
            originalEmail = mainActivity.getUserEmail();
            originalGender = mainActivity.getUserGender();
            originalTanggalLahir = mainActivity.getUserTanggalLahir();

            edtFullName.setText(originalNama);
            edtEmail.setText(originalEmail);
            edtDateOfBirth.setText(originalTanggalLahir);

            String[] genderOptions = {"Male", "Female", "Other"};
            for (int i = 0; i < genderOptions.length; i++) {
                if (genderOptions[i].equals(originalGender)) {
                    spnGender.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                        edtDateOfBirth.setText(selectedDate);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean validateInputs() {
        String nama = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String tanggalLahir = edtDateOfBirth.getText().toString().trim();
        String gender = spnGender.getSelectedItem().toString();

        boolean valid = true;

        if (nama.isEmpty()) {
            edtFullName.setError("Full name is required");
            valid = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Valid email is required");
            valid = false;
        }

        if (tanggalLahir.isEmpty()) {
            Toast.makeText(getContext(), "Date of birth is required", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (gender.isEmpty()) {
            Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void saveChanges() {
        if (!validateInputs()) {
            return;
        }

        String newNama = edtFullName.getText().toString().trim();
        String newEmail = edtEmail.getText().toString().trim();
        String newGender = spnGender.getSelectedItem().toString();
        String newTanggalLahir = edtDateOfBirth.getText().toString().trim();

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateUserData(newNama, newEmail, newGender, newTanggalLahir);
        }

        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}