package edu.uph.uas_kelompok3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.uph.uas_kelompok3.Model.Consultation;
import edu.uph.uas_kelompok3.R;
import io.realm.RealmResults;

public class PastConsultationAdapter extends RecyclerView.Adapter<PastConsultationAdapter.PastConsultationViewHolder> {
    private RealmResults<Consultation> consultations;
    private OnActionListener onActionListener;

    public interface OnActionListener {
        void onViewSummaryClick(Consultation consultation);
        void onRateDoctorClick(Consultation consultation);
    }

    public PastConsultationAdapter(RealmResults<Consultation> consultations, OnActionListener onActionListener) {
        this.consultations = consultations;
        this.onActionListener = onActionListener;
    }

    @NonNull
    @Override
    public PastConsultationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_consultation, parent, false);
        return new PastConsultationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastConsultationViewHolder holder, int position) {
        Consultation consultation = consultations.get(position);
        if (consultation == null) return;

        holder.tvDoctorName.setText(consultation.getDoctorName());
        holder.tvDoctorSpecialty.setText(consultation.getDoctorSpecialty());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        holder.tvDate.setText(sdf.format(consultation.getAppointmentDate()));
        holder.tvTypeLabel.setText(consultation.getAppointmentType());

        // Set icon based on appointment type
        if ("Video Call".equals(consultation.getAppointmentType())) {
            holder.ivTypeIcon.setImageResource(R.drawable.ic_video_call);
        } else if ("Chat".equals(consultation.getAppointmentType())) {
            holder.ivTypeIcon.setImageResource(R.drawable.ic_chat);
        } else {
            holder.ivTypeIcon.setImageResource(R.drawable.ic_profile);
        }

        holder.btnViewSummary.setOnClickListener(v -> {
            if (onActionListener != null) {
                onActionListener.onViewSummaryClick(consultation);
            }
        });

        holder.btnRateDoctor.setOnClickListener(v -> {
            if (onActionListener != null) {
                onActionListener.onRateDoctorClick(consultation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consultations.size();
    }

    static class PastConsultationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvDoctorSpecialty, tvDate, tvTypeLabel;
        ImageView ivTypeIcon;
        MaterialButton btnViewSummary, btnRateDoctor;

        public PastConsultationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tv_past_doctor_name);
            tvDoctorSpecialty = itemView.findViewById(R.id.tv_past_doctor_specialty);
            tvDate = itemView.findViewById(R.id.tv_past_date);
            tvTypeLabel = itemView.findViewById(R.id.tv_past_type_label);
            ivTypeIcon = itemView.findViewById(R.id.iv_past_type_icon);
            btnViewSummary = itemView.findViewById(R.id.btn_view_summary);
            btnRateDoctor = itemView.findViewById(R.id.btn_rate_doctor);
        }
    }
}
