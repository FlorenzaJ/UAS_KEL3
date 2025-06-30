package edu.uph.uas_kelompok3.ui.predict;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import edu.uph.uas_kelompok3.R;

public class PredictFragment extends Fragment {

    private TextInputEditText etAge;
    private AutoCompleteTextView actvGender, actvSmokingHistory, actvCoughFrequency, actvAllergies, actvChronicConditions;
    private Slider sliderBreathingDifficulty;
    private MaterialButton btnGenerate;

    private String[] genderOptions = {"Male", "Female"};
    private String[] smokingOptions = {"Never", "Former", "Current"};
    private String[] coughOptions = {"Rarely", "Sometimes", "Often", "Always"};
    private String[] allergyOptions = {"None", "Dust", "Pollen", "Food", "Other"};
    private String[] chronicOptions = {"None", "Asthma", "COPD", "Diabetes", "Other"};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_predict, container, false);

        // Initialize views
        etAge = root.findViewById(R.id.et_age);
        actvGender = root.findViewById(R.id.actv_gender);
        actvSmokingHistory = root.findViewById(R.id.actv_smoking_history);
        actvCoughFrequency = root.findViewById(R.id.actv_cough_frequency);
        actvAllergies = root.findViewById(R.id.actv_allergies);
        actvChronicConditions = root.findViewById(R.id.actv_chronic_conditions);
        sliderBreathingDifficulty = root.findViewById(R.id.slider_breathing_difficulty);
        btnGenerate = root.findViewById(R.id.btn_generate_prediction);

        setupDropdown(actvGender, genderOptions);
        setupDropdown(actvSmokingHistory, smokingOptions);
        setupDropdown(actvCoughFrequency, coughOptions);
        setupDropdown(actvAllergies, allergyOptions);
        setupDropdown(actvChronicConditions, chronicOptions);

        btnGenerate.setOnClickListener(v -> {
            String age = etAge.getText().toString();
            String gender = actvGender.getText().toString();
            String smoking = actvSmokingHistory.getText().toString();
            String cough = actvCoughFrequency.getText().toString();
            String allergy = actvAllergies.getText().toString();
            String chronic = actvChronicConditions.getText().toString();
            float breathingDifficulty = sliderBreathingDifficulty.getValue();

            String result = "Age: " + age +
                    "\nGender: " + gender +
                    "\nSmoking: " + smoking +
                    "\nCough: " + cough +
                    "\nAllergy: " + allergy +
                    "\nChronic: " + chronic +
                    "\nBreathing Difficulty: " + breathingDifficulty;

            Toast.makeText(getContext(), "Prediction Generated!\n" + result, Toast.LENGTH_LONG).show();
        });

        return root;
    }

    private void setupDropdown(AutoCompleteTextView view, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, options);
        view.setAdapter(adapter);
    }
}
