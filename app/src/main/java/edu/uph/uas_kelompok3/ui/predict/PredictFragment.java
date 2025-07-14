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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import edu.uph.uas_kelompok3.Model.Predict;
import edu.uph.uas_kelompok3.R;
import io.realm.Realm;

public class PredictFragment extends Fragment {

    private TextInputEditText etAge;
    private AutoCompleteTextView actvGender, actvSmokingHistory, actvCoughFrequency, actvAllergies, actvChronicConditions;
    private Slider sliderBreathingDifficulty;
    private MaterialButton btnGenerate;

    private final String[] genderOptions = {"Male", "Female"};
    private final String[] smokingOptions = {"Never", "Former", "Current"};
    private final String[] coughOptions = {"Rarely", "Sometimes", "Often", "Always"};
    private final String[] allergyOptions = {"None", "Dust", "Pollen", "Food", "Other"};
    private final String[] chronicOptions = {"None", "Asthma", "COPD", "Diabetes", "Other"};

    @Override
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

        // Initial setup for dropdowns
        setupAllDropdowns();

        btnGenerate.setOnClickListener(v -> generatePrediction());

        return root;
    }

    // RE-SET dropdown setiap fragment ditampilkan kembali
    @Override
    public void onResume() {
        super.onResume();
        setupAllDropdowns(); // Agar dropdown tetap bisa dipilih berkali-kali
    }

    private void setupAllDropdowns() {
        setupDropdown(actvGender, genderOptions);
        setupDropdown(actvSmokingHistory, smokingOptions);
        setupDropdown(actvCoughFrequency, coughOptions);
        setupDropdown(actvAllergies, allergyOptions);
        setupDropdown(actvChronicConditions, chronicOptions);
    }

    private void setupDropdown(AutoCompleteTextView view, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, options);
        view.setAdapter(adapter);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
    }

    private void generatePrediction() {
        String ageStr = etAge.getText().toString();
        String gender = actvGender.getText().toString();
        String smoking = actvSmokingHistory.getText().toString();
        String cough = actvCoughFrequency.getText().toString();
        String allergy = actvAllergies.getText().toString();
        String chronic = actvChronicConditions.getText().toString();
        float breathingDifficulty = sliderBreathingDifficulty.getValue();

        // Validation
        if (ageStr.isEmpty() || gender.isEmpty() || smoking.isEmpty() || cough.isEmpty() || allergy.isEmpty() || chronic.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0) {
                Toast.makeText(getContext(), "Please enter a valid age", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Age must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        float predictedScore = calculateRiskScore(age, smoking, breathingDifficulty, cough, chronic);
        String predictedRisk = getRiskLevel(predictedScore);

        Realm realm = Realm.getDefaultInstance();
        String predictionId = UUID.randomUUID().toString();

        try {
            realm.executeTransaction(r -> {
                Predict prediction = r.createObject(Predict.class, predictionId);
                prediction.setAge(age);
                prediction.setGender(gender);
                prediction.setSmokingHistory(smoking);
                prediction.setBreathingDifficulty(breathingDifficulty);
                prediction.setCoughFrequency(cough);
                prediction.setAllergies(allergy);
                prediction.setChronicConditions(chronic);
                prediction.setRiskLevel(predictedRisk);
                prediction.setRiskScore(predictedScore);
                prediction.setCreatedAt(new Date());
            });

            Bundle bundle = new Bundle();
            bundle.putString("predictionId", predictionId);
            NavController navController = NavHostFragment.findNavController(PredictFragment.this);
            navController.navigate(R.id.action_predictFragment_to_predictionResultFragment, bundle);

            Toast.makeText(getContext(), "Prediction generated successfully!", Toast.LENGTH_SHORT).show();

            // Optional: Reset form untuk prediksi berikutnya
            // resetForm();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error saving prediction: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            realm.close();
        }
    }

    private void resetForm() {
        etAge.setText("");
        actvGender.setText("");
        actvSmokingHistory.setText("");
        actvCoughFrequency.setText("");
        actvAllergies.setText("");
        actvChronicConditions.setText("");
        sliderBreathingDifficulty.setValue(50f);
    }

    private float calculateRiskScore(int age, String smoking, float breathingDifficulty, String cough, String chronic) {
        float score = 0;

        if (age < 30) score += 5;
        else if (age < 40) score += 10;
        else if (age < 50) score += 15;
        else if (age < 60) score += 20;
        else score += 25;

        switch (smoking) {
            case "Never": score += 0; break;
            case "Former": score += 15; break;
            case "Current": score += 30; break;
        }

        score += (breathingDifficulty / 100) * 25;

        switch (cough) {
            case "Rarely": score += 0; break;
            case "Sometimes": score += 3; break;
            case "Often": score += 7; break;
            case "Always": score += 10; break;
        }

        if (!chronic.equals("None")) score += 10;

        Random random = new Random();
        score += (random.nextFloat() - 0.5f) * 10;

        return Math.max(0, Math.min(100, score));
    }

    private String getRiskLevel(float score) {
        if (score < 30) return "Low";
        else if (score < 70) return "Moderate";
        else return "High";
    }
}

