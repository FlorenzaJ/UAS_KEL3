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
        int textColor;
        int backgroundResId;

        switch (riskLevel) {
            case "Low":
                backgroundResId = R.drawable.status_badge_low;
                textColor = Color.parseColor("#166534");
                break;
            case "Moderate":
                backgroundResId = R.drawable.status_badge_moderate;
                textColor = Color.parseColor("#E65100");
                break;
            case "High":
                backgroundResId = R.drawable.status_badge_high;
                textColor = Color.parseColor("#991B1B");
                break;
            default:
                backgroundResId = R.drawable.status_badge_caution;
                textColor = Color.WHITE;
                break;
        }

        textView.setBackgroundResource(backgroundResId);
        textView.setTextColor(textColor);
        textView.setPadding(16, 8, 16, 8);
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
