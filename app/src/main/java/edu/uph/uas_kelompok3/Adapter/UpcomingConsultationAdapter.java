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
import java.util.Date;
import java.util.Locale;

import edu.uph.uas_kelompok3.Model.Consultation;
import edu.uph.uas_kelompok3.R;
import io.realm.RealmResults;

public class UpcomingConsultationAdapter extends RecyclerView.Adapter<UpcomingConsultationAdapter.UpcomingConsultationViewHolder> {
    private RealmResults<Consultation> consultations;
    private OnActionListener onActionListener;

    public interface OnActionListener {
        void onJoinNowClick(Consultation consultation);
        void onRescheduleClick(Consultation consultation);
    }

    public UpcomingConsultationAdapter(RealmResults<Consultation> consultations, OnActionListener onActionListener) {
        this.consultations = consultations;
        this.onActionListener = onActionListener;
    }

    @NonNull
    @Override
    public UpcomingConsultationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upcoming_consultation, parent, false);
        return new UpcomingConsultationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingConsultationViewHolder holder, int position) {
        Consultation consultation = consultations.get(position);
        if (consultation == null) return;

        holder.tvDoctorName.setText(consultation.getDoctorName());
        holder.tvDoctorSpecialty.setText(consultation.getDoctorSpecialty());

        // Format date
        SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat sdfToday = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate;
        if (sdfToday.format(consultation.getAppointmentDate()).equals(sdfToday.format(new Date()))) {
            formattedDate = "Today";
        } else {
            formattedDate = sdfDate.format(consultation.getAppointmentDate());
        }
        holder.tvDate.setText(formattedDate);
        holder.tvTime.setText(consultation.getAppointmentTime());

        holder.tvTypeLabel.setText(consultation.getAppointmentType());
        // Set icon based on appointment type
        if ("Video Call".equals(consultation.getAppointmentType())) {
            holder.ivTypeIcon.setImageResource(R.drawable.ic_video_call);
        } else if ("Chat".equals(consultation.getAppointmentType())) {
            holder.ivTypeIcon.setImageResource(R.drawable.ic_chat);
        } else {
            holder.ivTypeIcon.setImageResource(R.drawable.ic_profile);
        }

        holder.btnJoinNow.setOnClickListener(v -> {
            if (onActionListener != null) {
                onActionListener.onJoinNowClick(consultation);
            }
        });

        holder.btnReschedule.setOnClickListener(v -> {
            if (onActionListener != null) {
                onActionListener.onRescheduleClick(consultation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consultations.size();
    }

    static class UpcomingConsultationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvDoctorSpecialty, tvDate, tvTime, tvTypeLabel;
        ImageView ivTypeIcon;
        MaterialButton btnJoinNow, btnReschedule;

        public UpcomingConsultationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tv_upcoming_doctor_name);
            tvDoctorSpecialty = itemView.findViewById(R.id.tv_upcoming_doctor_specialty);
            tvDate = itemView.findViewById(R.id.tv_upcoming_date);
            tvTime = itemView.findViewById(R.id.tv_upcoming_time);
            tvTypeLabel = itemView.findViewById(R.id.tv_upcoming_type_label);
            ivTypeIcon = itemView.findViewById(R.id.iv_upcoming_type_icon);
            btnJoinNow = itemView.findViewById(R.id.btn_join_now);
            btnReschedule = itemView.findViewById(R.id.btn_reschedule);
        }
    }
}
