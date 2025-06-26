package edu.uph.uas_kelompok3.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import edu.uph.uas_kelompok3.Model.Predict;
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
        Predict predict = predictionList.get(position);

        holder.txvPredictionDate.setText(predict.getDate());
        holder.txvPredictionStatus.setText("Status: " + predict.getStatus());
        holder.txvPredictionRisk.setText(predict.getRiskLevel());

        // Set risk level color
        String riskLevel = predict.getRiskLevel();
        if (riskLevel.equals("Low Risk")) {
            holder.txvPredictionRisk.setBackgroundColor(Color.parseColor("#4CAF50"));
            holder.txvPredictionRisk.setTextColor(Color.WHITE);
        } else if (riskLevel.equals("Medium Risk")) {
            holder.txvPredictionRisk.setBackgroundColor(Color.parseColor("#FF9800"));
            holder.txvPredictionRisk.setTextColor(Color.WHITE);
        } else if (riskLevel.equals("High Risk")) {
            holder.txvPredictionRisk.setBackgroundColor(Color.parseColor("#F44336"));
            holder.txvPredictionRisk.setTextColor(Color.WHITE);
        }
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
