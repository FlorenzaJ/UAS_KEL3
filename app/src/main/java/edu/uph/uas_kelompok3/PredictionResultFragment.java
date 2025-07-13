package edu.uph.uas_kelompok3;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.uph.uas_kelompok3.Model.Predict;
import io.realm.Realm;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;

public class PredictionResultFragment extends Fragment {

    private TextView tvTitle, tvDate, tvRiskLevel, tvRiskScore;
    private TextView tvSmokingStatus, tvBreathingStatus, tvAgeStatus;
    private TextView tvSmokingDesc, tvBreathingDesc, tvAgeDesc;
    private TextView tvLungCapacity, tvRespiratoryRate, tvOxygenSat, tvCOPDRisk, tvCancerRisk;
    private TextView tvLungCapacityStatus, tvRespiratoryStatus, tvOxygenStatus, tvCOPDStatus, tvCancerStatus;
    private LinearLayout recommendationsLayout;
    private MaterialButton btnConsultDoctor, btnTrackProgress;
    private CardView mainCard;
    private ImageView riskIcon;
    private View smokingIndicator, breathingIndicator, ageIndicator;
    private Realm realm;
    private Predict prediction;
    private String predictionId;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            predictionId = getArguments().getString("predictionId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prediction_result, container, false);
        rootView = view; // simpan root view
        initViews(view);
        setupClickListeners();
        loadPredictionData();

        return view;
    }

    private void initViews(View view) {
        // Header
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        // Main card elements
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvRiskLevel = view.findViewById(R.id.tvRiskLevel);
        tvRiskScore = view.findViewById(R.id.tvRiskScore);
        riskIcon = view.findViewById(R.id.riskIcon);
        mainCard = view.findViewById(R.id.mainCard);

        // Key factors
        tvSmokingStatus = view.findViewById(R.id.tvSmokingStatus);
        tvBreathingStatus = view.findViewById(R.id.tvBreathingStatus);
        tvAgeStatus = view.findViewById(R.id.tvAgeStatus);
        tvSmokingDesc = view.findViewById(R.id.tvSmokingDesc);
        tvBreathingDesc = view.findViewById(R.id.tvBreathingDesc);
        tvAgeDesc = view.findViewById(R.id.tvAgeDesc);

        smokingIndicator = view.findViewById(R.id.smokingIndicator);
        breathingIndicator = view.findViewById(R.id.breathingIndicator);
        ageIndicator = view.findViewById(R.id.ageIndicator);

        // Prediction details
        tvLungCapacity = view.findViewById(R.id.tvLungCapacity);
        tvRespiratoryRate = view.findViewById(R.id.tvRespiratoryRate);
        tvOxygenSat = view.findViewById(R.id.tvOxygenSat);
        tvCOPDRisk = view.findViewById(R.id.tvCOPDRisk);
        tvCancerRisk = view.findViewById(R.id.tvCancerRisk);

        tvLungCapacityStatus = view.findViewById(R.id.tvLungCapacityStatus);
        tvRespiratoryStatus = view.findViewById(R.id.tvRespiratoryStatus);
        tvOxygenStatus = view.findViewById(R.id.tvOxygenStatus);
        tvCOPDStatus = view.findViewById(R.id.tvCOPDStatus);
        tvCancerStatus = view.findViewById(R.id.tvCancerStatus);

        // Buttons
        btnConsultDoctor = view.findViewById(R.id.btnConsultDoctor);
        btnTrackProgress = view.findViewById(R.id.btnTrackProgress);

        recommendationsLayout = view.findViewById(R.id.recommendationsLayout);
    }

    private void loadPredictionData() {
        realm = Realm.getDefaultInstance();

        if (predictionId != null) {
            prediction = realm.where(Predict.class)
                    .equalTo("id", predictionId)
                    .findFirst();
        }

        if (prediction != null) {
            setUpUI(prediction);
        }
    }

    private void setUpUI(Predict data) {
        // Set basic info
        tvTitle.setText("Lung Health Assessment");
        tvRiskLevel.setText(data.getRiskLevel());
        tvRiskScore.setText(String.format("%.0f%% Risk Score", data.getRiskScore()));

        // Format tanggal
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(data.getCreatedAt()));

        // Setup risk level styling
        setupRiskLevelStyling(data.getRiskLevel());

        // Set key factors
        setupKeyFactors(data);

        // Generate prediction details
        setupPredictionDetails(data);

        // Recommendations
        setupRecommendations(data);
    }

    private void setupKeyFactors(Predict data) {
        // Smoking History
        tvSmokingDesc.setText(getSmokingDescription(data.getSmokingHistory()));
        setStatusIndicator(smokingIndicator, getSmokingRiskLevel(data.getSmokingHistory()));

        // Breathing Difficulty
        tvBreathingDesc.setText(getBreathingDescription(data.getBreathingDifficulty()));
        setStatusIndicator(breathingIndicator, getBreathingRiskLevel(data.getBreathingDifficulty()));

        // Age
        tvAgeDesc.setText(data.getAge() + " years (" + getAgeRiskDescription(data.getAge()) + ")");
        setStatusIndicator(ageIndicator, getAgeRiskLevel(data.getAge()));
    }

    private void setupPredictionDetails(Predict data) {
        float riskScore = data.getRiskScore();

        // Lung Capacity
        int lungCapacity = Math.max(40, (int)(100 - riskScore));
        tvLungCapacity.setText(lungCapacity + "%");
        tvLungCapacityStatus.setText(lungCapacity > 80 ? "Good" : lungCapacity > 60 ? "Moderate" : "Caution");
        setStatusBadge(tvLungCapacityStatus, tvLungCapacityStatus.getText().toString().toLowerCase());

        // Respiratory Rate
        String respRate = riskScore > 50 ? "Elevated" : "Normal";
        tvRespiratoryRate.setText(respRate);
        tvRespiratoryStatus.setText(respRate.equals("Normal") ? "Good" : "Caution");
        setStatusBadge(tvRespiratoryStatus, tvRespiratoryStatus.getText().toString().toLowerCase());

        // Oxygen Saturation
        int oxygenSat = Math.max(85, (int)(100 - (riskScore / 3)));
        tvOxygenSat.setText(oxygenSat + "%");
        tvOxygenStatus.setText(oxygenSat > 95 ? "Good" : oxygenSat > 90 ? "Moderate" : "Caution");
        setStatusBadge(tvOxygenStatus, tvOxygenStatus.getText().toString().toLowerCase());

        // COPD Risk
        String copdRisk = riskScore > 60 ? "High" : "Low";
        tvCOPDRisk.setText(copdRisk);
        tvCOPDStatus.setText(copdRisk.equals("Low") ? "Good" : "Caution");
        setStatusBadge(tvCOPDStatus, tvCOPDStatus.getText().toString().toLowerCase());

        // Cancer Risk
        String cancerRisk = riskScore > 70 ? "High" : "Low";
        tvCancerRisk.setText(cancerRisk);
        tvCancerStatus.setText(cancerRisk.equals("Low") ? "Good" : "Caution");
        setStatusBadge(tvCancerStatus, tvCancerStatus.getText().toString().toLowerCase());
    }

    private void setupRecommendations(Predict data) {
        recommendationsLayout.removeAllViews();

        if (data.getBreathingDifficulty() > 50) {
            addRecommendation("• Consult a pulmonologist for breathing difficulties");
        }

        addRecommendation("• Maintain regular exercise routine");
        addRecommendation("• Avoid air pollution and second-hand smoke");
        addRecommendation("• Eat a healthy diet rich in antioxidants");
    }

    private String getSmokingDescription(String smoking) {
        if (smoking == null) return "Unknown";
        switch (smoking) {
            case "Never": return "Never smoked";
            case "Former": return "Former smoker";
            case "Current": return "Current smoker";
            default: return smoking;
        }
    }

    private String getSmokingRiskLevel(String smoking) {
        if (smoking == null) return "good";
        switch (smoking) {
            case "Never": return "good";
            case "Former": return "caution";
            case "Current": return "danger";
            default: return "good";
        }
    }

    private String getBreathingDescription(float difficulty) {
        if (difficulty < 25) return "Minimal difficulty";
        else if (difficulty < 50) return "Mild difficulty";
        else if (difficulty < 75) return "Moderate difficulty";
        else return "Severe difficulty";
    }

    private String getBreathingRiskLevel(float difficulty) {
        if (difficulty < 25) return "good";
        else if (difficulty < 75) return "caution";
        else return "danger";
    }

    private String getAgeRiskDescription(int age) {
        if (age < 25) return "low risk factor";
        else if (age < 40) return "moderate risk factor";
        else if (age < 60) return "increased risk factor";
        else return "high risk factor";
    }

    private String getAgeRiskLevel(int age) {
        if (age < 40) return "good";
        else if (age < 60) return "caution";
        else return "danger";
    }

    private void setupRiskLevelStyling(String riskLevel) {
        int textColor, bgColor, iconColor, barColor;
        int iconBackgroundRes;
        
        switch (riskLevel.toLowerCase()) {
            case "low":
                textColor = ContextCompat.getColor(requireContext(), R.color.cadmium_green);
                bgColor = ContextCompat.getColor(requireContext(), R.color.green_drift);
                iconColor = ContextCompat.getColor(requireContext(), R.color.pigment_green);
                barColor = ContextCompat.getColor(requireContext(), R.color.pigment_green);
                iconBackgroundRes = R.drawable.circle_warning_bg_low;
                break;
            case "high":
                textColor = ContextCompat.getColor(requireContext(), R.color.dev_maroon);
                bgColor = ContextCompat.getColor(requireContext(), R.color.surface_blush);
                iconColor = ContextCompat.getColor(requireContext(), R.color.accent_red);
                barColor = ContextCompat.getColor(requireContext(), R.color.accent_red);
                iconBackgroundRes = R.drawable.circle_warning_bg_high;
                break;
            default: // moderate
                textColor = ContextCompat.getColor(requireContext(), R.color.persimmon);
                bgColor = ContextCompat.getColor(requireContext(), R.color.latte);
                iconColor = ContextCompat.getColor(requireContext(), R.color.orange_peel);
                barColor = ContextCompat.getColor(requireContext(), R.color.orange_peel);
                iconBackgroundRes = R.drawable.circle_warning_bg_moderate;
                break;
        }
        
        // Set risk level text color
        tvRiskLevel.setTextColor(textColor);

        // Set risk score background and text color
        tvRiskScore.setBackgroundTintList(ColorStateList.valueOf(bgColor));
        tvRiskScore.setTextColor(textColor);

        // Set warning icon background and tint
        riskIcon.setBackgroundResource(iconBackgroundRes);
        riskIcon.setColorFilter(iconColor);

        // Set top view color
        if (rootView != null) {
            rootView.findViewById(R.id.vColor).setBackgroundTintList(ColorStateList.valueOf(barColor));
        }
    }

    private void setStatusIndicator(View indicator, String status) {
        if (indicator == null) return;
        int color;
        switch (status) {
            case "good":
                color = ContextCompat.getColor(requireContext(), R.color.pigment_green);
                break;
            case "caution":
                color = ContextCompat.getColor(requireContext(), R.color.orange_peel);
                break;
            default: // danger
                color = ContextCompat.getColor(requireContext(), R.color.accent_red);
                break;
        }
        indicator.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void setStatusBadge(TextView textView, String status) {
        int backgroundColor, textColor;
        switch (status) {
            case "good":
                backgroundColor = ContextCompat.getColor(requireContext(), R.color.green_drift);
                textColor = ContextCompat.getColor(requireContext(), R.color.cadmium_green);
                break;
            case "moderate":
                backgroundColor = ContextCompat.getColor(requireContext(), R.color.latte);
                textColor = ContextCompat.getColor(requireContext(), R.color.persimmon);
                break;
            default: // caution
                backgroundColor = ContextCompat.getColor(requireContext(), R.color.surface_blush);
                textColor = ContextCompat.getColor(requireContext(), R.color.dev_maroon);
                break;
        }
        textView.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        textView.setTextColor(textColor);
    }

    private void addRecommendation(String text) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setTextSize(14);
        tv.setTextColor(Color.parseColor("#666666"));
        tv.setPadding(0, 8, 0, 8);
        recommendationsLayout.addView(tv);
    }

    private void setupClickListeners() {
        btnConsultDoctor.setOnClickListener(v -> {
            android.widget.Toast.makeText(getContext(), "Consultation feature coming soon!", android.widget.Toast.LENGTH_SHORT).show();
        });

//        btnTrackProgress.setOnClickListener(v -> {
//            NavController navController = Navigation.findNavController(v);
//            navController.popBackStack();
//        });

//        btnTrackProgress.setOnClickListener(v -> {
//            finish();
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}