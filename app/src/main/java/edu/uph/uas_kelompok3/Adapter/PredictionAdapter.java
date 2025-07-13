package edu.uph.uas_kelompok3.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import edu.uph.uas_kelompok3.Model.Predict;
import edu.uph.uas_kelompok3.PredictionResultFragment;
import edu.uph.uas_kelompok3.R;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder> {
    private List<Predict> predictionList;
    public PredictionAdapter(List<Predict> predictionList) {
        this.predictionList = predictionList;
    }

    @NonNull
    @Override
    public PredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prediction, parent, false);
        return new PredictionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionViewHolder holder, int position) {
        Predict model = predictionList.get(position);

        holder.txvPredictionDate.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(model.getCreatedAt()));
        holder.txvPredictionStatus.setText("Status: " + getStatusFromRisk(model.getRiskLevel()));
        holder.txvPredictionRisk.setText(model.getRiskLevel());

        setRiskLevelColor(holder.txvPredictionRisk, model.getRiskLevel());

        holder.itemView.setOnClickListener(v -> {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("predictionId", model.getId());

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_homeFragment_to_predictionResultFragment, bundle);

            } catch (Exception e) {
                // Fallback jika navigation gagal
                android.widget.Toast.makeText(v.getContext(), "Unable to open prediction details", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getStatusFromRisk(String riskLevel) {
        switch (riskLevel) {
            case "Low": return "Good";
            case "Moderate": return "Moderate";
            case "High": return "Poor";
            default: return "Unknown";
        }
    }

    private void setRiskLevelColor(TextView textView, String riskLevel) {
        int backgroundColor, textColor;

        switch (riskLevel) {
            case "Low":
                backgroundColor = Color.parseColor("#4CAF50"); // Green
                textColor = Color.WHITE;
                break;
            case "Moderate":
                backgroundColor = Color.parseColor("#FF9800"); // Orange
                textColor = Color.WHITE;
                break;
            case "High":
                backgroundColor = Color.parseColor("#F44336"); // Red
                textColor = Color.WHITE;
                break;
            default:
                backgroundColor = Color.parseColor("#9E9E9E"); // Gray
                textColor = Color.WHITE;
                break;
        }

        textView.setBackground(createRoundedBackground(backgroundColor));
        textView.setTextColor(textColor);
        textView.setPadding(16, 8, 16, 8);
    }

    private android.graphics.drawable.GradientDrawable createRoundedBackground(int color) {
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(16f);
        return drawable;
    }

    @Override
    public int getItemCount() {
        return predictionList.size();
    }

    static class PredictionViewHolder extends RecyclerView.ViewHolder {
        TextView txvPredictionDate, txvPredictionStatus, txvPredictionRisk;
        public PredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            txvPredictionDate = itemView.findViewById(R.id.txvPredictionDate);
            txvPredictionStatus = itemView.findViewById(R.id.txvPredictionStatus);
            txvPredictionRisk = itemView.findViewById(R.id.txvPredictionRisk);
        }
    }
}
