package edu.uph.uas_kelompok3;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import edu.uph.uas_kelompok3.Adapter.DoctorAdapter;
import edu.uph.uas_kelompok3.Model.Doctor;

public class AvailableDoctorsFragment extends Fragment {

    private RecyclerView rvDoctors;
    private DoctorAdapter doctorAdapter;
    private MaterialButton btnBookAppointmentBottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_doctors, container, false);

        rvDoctors = view.findViewById(R.id.rv_doctors);
        btnBookAppointmentBottom = view.findViewById(R.id.btn_book_appointment_bottom);

        setupRecyclerView();
        setupClickListeners();

        return view;
    }

    private void setupRecyclerView() {
        List<Doctor> doctors = new ArrayList<>();
        // Contoh data dokter
        doctors.add(new Doctor("Dr. Michael Chen", "Pulmonologist", 4.9f, 124, "Available Today"));
        doctors.add(new Doctor("Dr. Emily Rodriguez", "Respiratory Specialist", 4.8f, 98, "Available Tomorrow"));
        doctors.add(new Doctor("Dr. James Wilson", "Pulmonary Rehabilitation", 4.7f, 86, "Available Today"));
        // Tambahkan lebih banyak dokter jika diperlukan

        doctorAdapter = new DoctorAdapter(doctors, doctor -> {
            // Handle klik pada tombol "Book" di item dokter
            navigateToBookAppointmentDetails(doctor.getName());
        });
        rvDoctors.setAdapter(doctorAdapter);
    }

    private void setupClickListeners() {
        btnBookAppointmentBottom.setOnClickListener(v -> {
            navigateToBookAppointmentDetails(null); // Navigasi tanpa memilih dokter terlebih dahulu
        });
    }

    private void navigateToBookAppointmentDetails(String doctorName) {
        NavController navController = Navigation.findNavController(requireParentFragment().requireView());
        Bundle bundle = new Bundle();
        if (doctorName != null) {
            bundle.putString("selectedDoctorName", doctorName);
        }
        navController.navigate(R.id.action_consultFragment_to_bookAppointmentDetailsFragment, bundle);
    }
}