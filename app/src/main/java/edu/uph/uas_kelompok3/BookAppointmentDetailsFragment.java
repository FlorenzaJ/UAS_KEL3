package edu.uph.uas_kelompok3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.uph.uas_kelompok3.R;
import edu.uph.uas_kelompok3.Model.Doctor;

public class BookAppointmentDetailsFragment extends Fragment {

    private ImageView btnBack;
    private AutoCompleteTextView actvSelectDoctor;
    private TextInputEditText etAppointmentDate;
    private AutoCompleteTextView actvAppointmentTime;
    private TextInputEditText etReasonForVisit;

    private RadioGroup rgAppointmentType;
    private MaterialButton btnContinue;

    private Calendar calendar;
    private List<Doctor> availableDoctors;
    private Map<String, String> doctorSpecialtiesMap;
    private String selectedDoctorNameFromArgs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedDoctorNameFromArgs = getArguments().getString("selectedDoctorName");
        }

        availableDoctors = new ArrayList<>();
        doctorSpecialtiesMap = new HashMap<>();

        // Contoh data dokter
        Doctor doc1 = new Doctor("Dr. Michael Chen", "Pulmonologist", 4.9f, 124, "Available Today");
        Doctor doc2 = new Doctor("Dr. Emily Rodriguez", "Respiratory Specialist", 4.8f, 98, "Available Tomorrow");
        Doctor doc3 = new Doctor("Dr. James Wilson", "Pulmonary Rehabilitation", 4.7f, 86, "Available Today");

        availableDoctors.add(doc1);
        availableDoctors.add(doc2);
        availableDoctors.add(doc3);

        doctorSpecialtiesMap.put(doc1.getName(), doc1.getSpecialty());
        doctorSpecialtiesMap.put(doc2.getName(), doc2.getSpecialty());
        doctorSpecialtiesMap.put(doc3.getName(), doc3.getSpecialty());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_appointment_details, container, false);

        initViews(view);
        setupDropdowns();
        setupDateAndTimePickers();
        setupClickListeners();

        if (selectedDoctorNameFromArgs != null && !selectedDoctorNameFromArgs.isEmpty()) {
            actvSelectDoctor.setText(selectedDoctorNameFromArgs, false);
        }

        return view;
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);

        // PERBAIKAN: Pastikan ID yang dicari sesuai dengan tipe View di XML
        // actv_select_doctor adalah AutoCompleteTextView
        actvSelectDoctor = view.findViewById(R.id.actv_select_doctor);

        // et_appointment_date adalah TextInputEditText
        etAppointmentDate = view.findViewById(R.id.et_appointment_date);

        // actv_appointment_time adalah AutoCompleteTextView
        actvAppointmentTime = view.findViewById(R.id.actv_appointment_time);

        // et_reason_for_visit adalah TextInputEditText
        etReasonForVisit = view.findViewById(R.id.et_reason_for_visit);

        rgAppointmentType = view.findViewById(R.id.rg_appointment_type);
        btnContinue = view.findViewById(R.id.btn_continue);
    }

    private void setupDropdowns() {
        List<String> doctorNames = new ArrayList<>(doctorSpecialtiesMap.keySet());
        ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, doctorNames);
        actvSelectDoctor.setAdapter(doctorAdapter);

        String[] timeSlots = {"09:00 AM", "10:00 AM", "11:00 AM", "02:00 PM", "03:00 PM", "04:00 PM"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, timeSlots);
        actvAppointmentTime.setAdapter(timeAdapter);
    }

    private void setupDateAndTimePickers() {
        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateEditText();
        };

        etAppointmentDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateDateEditText() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etAppointmentDate.setText(sdf.format(calendar.getTime()));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        btnContinue.setOnClickListener(v -> {
            String doctor = actvSelectDoctor.getText().toString().trim();
            String date = etAppointmentDate.getText().toString().trim();
            String time = actvAppointmentTime.getText().toString().trim();
            String reason = etReasonForVisit.getText().toString().trim();

            int selectedTypeId = rgAppointmentType.getCheckedRadioButtonId();
            RadioButton selectedTypeRadioButton = rgAppointmentType.findViewById(selectedTypeId);
            String appointmentType = selectedTypeRadioButton != null ? selectedTypeRadioButton.getText().toString() : "";

            if (doctor.isEmpty() || date.isEmpty() || time.isEmpty() || appointmentType.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String doctorSpecialty = doctorSpecialtiesMap.get(doctor);
            if (doctorSpecialty == null) {
                doctorSpecialty = "Unknown Specialty";
            }

            Bundle bundle = new Bundle();
            bundle.putString("doctorName", doctor);
            bundle.putString("doctorSpecialty", doctorSpecialty);
            bundle.putString("appointmentDate", date);
            bundle.putString("appointmentTime", time);
            bundle.putString("appointmentType", appointmentType);
            bundle.putString("reasonForVisit", reason);

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_bookAppointmentDetailsFragment_to_bookAppointmentConfirmFragment, bundle);
        });
    }
}