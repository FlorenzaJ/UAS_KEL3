package edu.uph.uas_kelompok3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import edu.uph.uas_kelompok3.Model.Consultation;
import edu.uph.uas_kelompok3.R;
import io.realm.Realm;

public class BookAppointmentConfirmFragment extends Fragment {

    private ImageView btnBackToolbar;
    private TextView tvConfirmDoctor, tvConfirmDate, tvConfirmTime, tvConfirmType, tvConfirmReason;
    private MaterialButton btnBackConfirm, btnConfirmBooking;
    private String doctorName, doctorSpecialty, appointmentDate, appointmentTime, appointmentType, reasonForVisit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorName = getArguments().getString("doctorName");
            doctorSpecialty = getArguments().getString("doctorSpecialty"); // Get specialty
            appointmentDate = getArguments().getString("appointmentDate");
            appointmentTime = getArguments().getString("appointmentTime");
            appointmentType = getArguments().getString("appointmentType");
            reasonForVisit = getArguments().getString("reasonForVisit");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_appointment_confirm, container, false);

        initViews(view);
        displayConfirmationDetails();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        btnBackToolbar = view.findViewById(R.id.btnBackToolbar);
        tvConfirmDoctor = view.findViewById(R.id.tv_confirm_doctor);
        tvConfirmDate = view.findViewById(R.id.tv_confirm_date);
        tvConfirmTime = view.findViewById(R.id.tv_confirm_time);
        tvConfirmType = view.findViewById(R.id.tv_confirm_type);
        tvConfirmReason = view.findViewById(R.id.tv_confirm_reason);
        btnBackConfirm = view.findViewById(R.id.btn_back_confirm);
        btnConfirmBooking = view.findViewById(R.id.btn_confirm_booking);
    }

    private void displayConfirmationDetails() {
        tvConfirmDoctor.setText(doctorName + " (" + doctorSpecialty + ")");
        tvConfirmDate.setText(appointmentDate);
        tvConfirmTime.setText(appointmentTime);
        tvConfirmType.setText(appointmentType);
        tvConfirmReason.setText(reasonForVisit);
    }

    private void setupClickListeners() {
        btnBackToolbar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        btnBackConfirm.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        btnConfirmBooking.setOnClickListener(v -> {
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                // Sesuaikan format waktu jika berbeda (misal: "hh:mm a" untuk 12-jam dengan AM/PM)
                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.US);

                Date parsedDate = null;
                Date parsedTime = null;
                try {
                    parsedDate = sdfDate.parse(appointmentDate);
                    parsedTime = sdfTime.parse(appointmentTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error parsing date/time: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gabungkan tanggal dan waktu menjadi satu objek Date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate); // Set bagian tanggal
                Calendar timeCalendar = Calendar.getInstance();
                timeCalendar.setTime(parsedTime); // Set bagian waktu

                calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
                calendar.set(Calendar.SECOND, 0); // Reset detik dan milidetik untuk konsistensi
                calendar.set(Calendar.MILLISECOND, 0);

                Date finalAppointmentDateTime = calendar.getTime();

                realm.executeTransaction(r -> {
                    Consultation newConsultation = r.createObject(Consultation.class, UUID.randomUUID().toString());
                    newConsultation.setDoctorName(doctorName);
                    newConsultation.setDoctorSpecialty(doctorSpecialty);
                    newConsultation.setAppointmentDate(finalAppointmentDateTime); // Simpan tanggal dan waktu lengkap
                    newConsultation.setAppointmentTime(appointmentTime); // Tetap simpan string waktu untuk tampilan jika diperlukan
                    newConsultation.setAppointmentType(appointmentType);
                    newConsultation.setReasonForVisit(reasonForVisit);
                    newConsultation.setStatus("Upcoming"); // Status default untuk booking baru
                    newConsultation.setCreatedAt(new Date());
                });

                Toast.makeText(getContext(), "Appointment confirmed for " + new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).format(finalAppointmentDateTime), Toast.LENGTH_LONG).show();

                // Setelah booking berhasil, kembali ke ConsultFragment (tab Available)
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_bookAppointmentConfirmFragment_to_consultFragment);

            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to save appointment: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }
}