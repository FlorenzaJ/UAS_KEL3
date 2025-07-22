package edu.uph.uas_kelompok3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import edu.uph.uas_kelompok3.Model.Doctor;
import edu.uph.uas_kelompok3.R;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctors;
    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    public DoctorAdapter(List<Doctor> doctors, OnDoctorClickListener listener) {
        this.doctors = doctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.bind(doctor, listener);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName;
        TextView tvDoctorSpecialty;
        TextView tvDoctorRating;
        TextView tvDoctorReviews;
        TextView tvDoctorAvailability;
        ImageView ivChatIcon;
        MaterialButton btnBookDoctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvDoctorSpecialty = itemView.findViewById(R.id.tv_doctor_specialty);
            tvDoctorRating = itemView.findViewById(R.id.tv_doctor_rating);
            tvDoctorReviews = itemView.findViewById(R.id.tv_doctor_reviews);
            tvDoctorAvailability = itemView.findViewById(R.id.tv_doctor_availability);
            ivChatIcon = itemView.findViewById(R.id.iv_chat_icon);
            btnBookDoctor = itemView.findViewById(R.id.btn_book_doctor);
        }

        public void bind(final Doctor doctor, final OnDoctorClickListener listener) {
            tvDoctorName.setText(doctor.getName());
            tvDoctorSpecialty.setText(doctor.getSpecialty());
            tvDoctorRating.setText(String.format(java.util.Locale.US, "%.1f", doctor.getRating())); // Hanya rating
            tvDoctorReviews.setText(String.format(java.util.Locale.US, " • %d reviews", doctor.getReviews())); // Hanya reviews
            tvDoctorAvailability.setText(doctor.getAvailability());
            btnBookDoctor.setOnClickListener(v -> listener.onDoctorClick(doctor));
        }
    }
}
