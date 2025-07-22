package edu.uph.uas_kelompok3;

import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String PREFS_NAME = "LungHealthPrefs";
    private static final String KEY_USER_ID = "currentUserId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorName = getArguments().getString("doctorName");
            doctorSpecialty = getArguments().getString("doctorSpecialty");
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                Date parsedDate = null;
                try {
                    parsedDate = sdf.parse(appointmentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error parsing date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Date finalParsedDate = parsedDate;

                SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                String currentUserId = prefs.getString(KEY_USER_ID, null);
                if (currentUserId == null) {
                    currentUserId = UUID.randomUUID().toString();
                    prefs.edit().putString(KEY_USER_ID, currentUserId).apply();
                    Toast.makeText(getContext(), "New user ID generated: " + currentUserId, Toast.LENGTH_SHORT).show();
                }

                String finalCurrentUserId = currentUserId;
                realm.executeTransaction(r -> {
                    Consultation newConsultation = r.createObject(Consultation.class, UUID.randomUUID().toString());
                    newConsultation.setUserId(finalCurrentUserId);
                    newConsultation.setDoctorName(doctorName);
                    newConsultation.setDoctorSpecialty(doctorSpecialty);
                    newConsultation.setAppointmentDate(finalParsedDate);
                    newConsultation.setAppointmentTime(appointmentTime);
                    newConsultation.setAppointmentType(appointmentType);
                    newConsultation.setReasonForVisit(reasonForVisit);
                    newConsultation.setStatus("Upcoming");
                    newConsultation.setCreatedAt(new Date());
                });

                Toast.makeText(getContext(), "Appointment confirmed!", Toast.LENGTH_LONG).show();

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