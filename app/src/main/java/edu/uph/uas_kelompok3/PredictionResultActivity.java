package edu.uph.uas_kelompok3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PredictionResultActivity extends AppCompatActivity {

    private TextView tvTitle, tvDate, tvRiskLevel, tvRiskScore;
    private TextView tvSmokingStatus, tvBreathingStatus, tvAgeStatus;
    private TextView tvSmokingDesc, tvBreathingDesc, tvAgeDesc;
    private TextView tvLungCapacity, tvRespiratoryRate, tvOxygenSat, tvCOPDRisk, tvCancerRisk;
    private TextView tvLungCapacityStatus, tvRespiratoryStatus, tvOxygenStatus, tvCOPDStatus, tvCancerStatus;
    private LinearLayout recommendationsLayout;
    private MaterialButton btnConsultDoctor, btnTrackProgress;
    private CardView mainCard;
    private ImageView riskIcon;

    // Dummy data for prediction results
    private String riskLevel = "Moderate";
    private String riskScore = "50%";
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_result);

        initViews();
        setupData();
        setupClickListeners();
    }

    private void initViews() {
        // Header
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Main card elements
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        tvRiskLevel = findViewById(R.id.tvRiskLevel);
        tvRiskScore = findViewById(R.id.tvRiskScore);
        riskIcon = findViewById(R.id.riskIcon);
        mainCard = findViewById(R.id.mainCard);

        // Key factors
        tvSmokingStatus = findViewById(R.id.tvSmokingStatus);
        tvBreathingStatus = findViewById(R.id.tvBreathingStatus);
        tvAgeStatus = findViewById(R.id.tvAgeStatus);
        tvSmokingDesc = findViewById(R.id.tvSmokingDesc);
        tvBreathingDesc = findViewById(R.id.tvBreathingDesc);
        tvAgeDesc = findViewById(R.id.tvAgeDesc);

        // Prediction details
        tvLungCapacity = findViewById(R.id.tvLungCapacity);
        tvRespiratoryRate = findViewById(R.id.tvRespiratoryRate);
        tvOxygenSat = findViewById(R.id.tvOxygenSat);
        tvCOPDRisk = findViewById(R.id.tvCOPDRisk);
        tvCancerRisk = findViewById(R.id.tvCancerRisk);

        tvLungCapacityStatus = findViewById(R.id.tvLungCapacityStatus);
        tvRespiratoryStatus = findViewById(R.id.tvRespiratoryStatus);
        tvOxygenStatus = findViewById(R.id.tvOxygenStatus);
        tvCOPDStatus = findViewById(R.id.tvCOPDStatus);
        tvCancerStatus = findViewById(R.id.tvCancerStatus);

        // Buttons
        btnConsultDoctor = findViewById(R.id.btnConsultDoctor);
        btnTrackProgress = findViewById(R.id.btnTrackProgress);

        recommendationsLayout = findViewById(R.id.recommendationsLayout);
    }

    private void setupData() {
        // Set current date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        currentDate = sdf.format(new Date());

        tvTitle.setText("Lung Health Assessment");
        tvDate.setText(currentDate);
        tvRiskLevel.setText(riskLevel);
        tvRiskScore.setText(riskScore + " Risk Score");

        // Set risk level styling
        setupRiskLevelStyling();

        // Set key factors (dummy data)
        tvSmokingStatus.setText("Smoking History");
        tvSmokingDesc.setText("Never smoked");
        setStatusIndicator(findViewById(R.id.smokingIndicator), "good");

        tvBreathingStatus.setText("Breathing Difficulty");
        tvBreathingDesc.setText("Severe difficulty");
        setStatusIndicator(findViewById(R.id.breathingIndicator), "caution");

        tvAgeStatus.setText("Age");
        tvAgeDesc.setText("22 years (low risk factor)");
        setStatusIndicator(findViewById(R.id.ageIndicator), "good");

        // Set prediction details (dummy data)
        tvLungCapacity.setText("65%");
        tvLungCapacityStatus.setText("Caution");
        setStatusBadge(tvLungCapacityStatus, "caution");

        tvRespiratoryRate.setText("Elevated");
        tvRespiratoryStatus.setText("Caution");
        setStatusBadge(tvRespiratoryStatus, "caution");

        tvOxygenSat.setText("94%");
        tvOxygenStatus.setText("Moderate");
        setStatusBadge(tvOxygenStatus, "moderate");

        tvCOPDRisk.setText("Low");
        tvCOPDStatus.setText("Good");
        setStatusBadge(tvCOPDStatus, "good");

        tvCancerRisk.setText("Low");
        tvCancerStatus.setText("Good");
        setStatusBadge(tvCancerStatus, "good");

        // Setup recommendations
        setupRecommendations();
    }

    private void setupRiskLevelStyling() {
        int textColor;

        switch (riskLevel.toLowerCase()) {
            case "low":
                textColor = Color.parseColor("#2E7D32");
                break;
            case "high":
                textColor = Color.parseColor("#C62828");
                break;
            default: // moderate
                textColor = Color.parseColor("#E65100");
                break;
        }

        tvRiskLevel.setTextColor(textColor);
    }

    private void setStatusIndicator(View indicator, String status) {
        int color;
        switch (status) {
            case "good":
                color = Color.parseColor("#4CAF50");
                break;
            case "caution":
                color = Color.parseColor("#FF9800");
                break;
            default:
                color = Color.parseColor("#F44336");
                break;
        }
        indicator.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
    }

    private void setStatusBadge(TextView textView, String status) {
        int backgroundColor, textColor;
        switch (status) {
            case "good":
                backgroundColor = Color.parseColor("#E8F5E8");
                textColor = Color.parseColor("#2E7D32");
                break;
            case "moderate":
                backgroundColor = Color.parseColor("#FFF3E0");
                textColor = Color.parseColor("#E65100");
                break;
            default: // caution
                backgroundColor = Color.parseColor("#FFEBEE");
                textColor = Color.parseColor("#C62828");
                break;
        }
        textView.setBackgroundTintList(android.content.res.ColorStateList.valueOf(backgroundColor));
        textView.setTextColor(textColor);
    }

    private void setupRecommendations() {
        String[] recommendations = {
                "Schedule a consultation with a pulmonologist",
                "Consider pulmonary function testing",
                "Continue to avoid smoking and second-hand smoke",
                "Implement a regular exercise routine focusing on cardio"
        };

        for (String recommendation : recommendations) {
            TextView tv = new TextView(this);
            tv.setText("• " + recommendation);
            tv.setTextSize(14);
            tv.setTextColor(Color.parseColor("#666666"));
            tv.setPadding(0, 8, 0, 8);
            recommendationsLayout.addView(tv);
        }
    }

    private void setupClickListeners() {
        btnConsultDoctor.setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Consultation feature coming soon!", android.widget.Toast.LENGTH_SHORT).show();
        });

        btnTrackProgress.setOnClickListener(v -> {
            finish();
        });

        // Bottom navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navPredict).setOnClickListener(v -> {
            finish(); // Go back to prediction
        });

        findViewById(R.id.navConsult).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Consultation feature coming soon!", android.widget.Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Profile feature coming soon!", android.widget.Toast.LENGTH_SHORT).show();
        });
    }
}