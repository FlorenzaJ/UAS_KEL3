package edu.uph.uas_kelompok3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import edu.uph.uas_kelompok3.Model.Doctor;
import edu.uph.uas_kelompok3.R;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;
    private OnBookClickListener onBookClickListener;

    public interface OnBookClickListener {
        void onBookClick(Doctor doctor);
    }

    public DoctorAdapter(List<Doctor> doctorList, OnBookClickListener onBookClickListener) {
        this.doctorList = doctorList;
        this.onBookClickListener = onBookClickListener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.tvDoctorName.setText(doctor.getName());
        holder.tvDoctorSpecialty.setText(doctor.getSpecialty());
        holder.tvDoctorRating.setText(String.valueOf(doctor.getRating()));
        holder.tvDoctorReviews.setText(" • " + doctor.getReviews() + " reviews");
        holder.tvDoctorAvailability.setText(doctor.getAvailability());

        holder.btnBookDoctor.setOnClickListener(v -> {
            if (onBookClickListener != null) {
                onBookClickListener.onBookClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvDoctorSpecialty, tvDoctorRating, tvDoctorReviews, tvDoctorAvailability;
        MaterialButton btnBookDoctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvDoctorSpecialty = itemView.findViewById(R.id.tv_doctor_specialty);
            tvDoctorRating = itemView.findViewById(R.id.tv_doctor_rating);
            tvDoctorReviews = itemView.findViewById(R.id.tv_doctor_reviews);
            tvDoctorAvailability = itemView.findViewById(R.id.tv_doctor_availability);
            btnBookDoctor = itemView.findViewById(R.id.btn_book_doctor);
        }
    }
}
