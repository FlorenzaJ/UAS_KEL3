package edu.uph.uas_kelompok3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;

import edu.uph.uas_kelompok3.Adapter.PastConsultationAdapter;
import edu.uph.uas_kelompok3.Model.Consultation;
import edu.uph.uas_kelompok3.R;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class PastConsultationsFragment extends Fragment {

    private RecyclerView rvPastConsultations;
    private TextView tvNoPastConsultations;
    private Realm realm;
    private PastConsultationAdapter adapter;
    private RealmResults<Consultation> pastConsultations;
    private static final String PREFS_NAME = "LungHealthPrefs";
    private static final String KEY_USER_ID = "currentUserId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_consultations, container, false);

        rvPastConsultations = view.findViewById(R.id.rv_past_consultations);
        tvNoPastConsultations = view.findViewById(R.id.tv_no_past_consultations);

        realm = Realm.getDefaultInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfToday = calendar.getTime();

        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String currentUserId = prefs.getString(KEY_USER_ID, null);

        if (currentUserId == null) {
            tvNoPastConsultations.setText("No past consultations.");
            tvNoPastConsultations.setVisibility(View.VISIBLE);
            rvPastConsultations.setVisibility(View.GONE);
            return view;
        }

        pastConsultations = realm.where(Consultation.class)
                .equalTo("userId", currentUserId)
                .beginGroup()
                .equalTo("status", "Completed")
                .or()
                .equalTo("status", "Cancelled")
                .endGroup()
                .lessThan("appointmentDate", startOfToday)
                .sort("appointmentDate", Sort.DESCENDING)
                .sort("appointmentTime", Sort.DESCENDING)
                .findAll();

        setupRecyclerView();
        updateUI();

        pastConsultations.addChangeListener(consultations -> {
            adapter.notifyDataSetChanged();
            updateUI();
        });

        return view;
    }

    private void setupRecyclerView() {
        adapter = new PastConsultationAdapter(pastConsultations, new PastConsultationAdapter.OnActionListener() {
            @Override
            public void onViewSummaryClick(Consultation consultation) {
                Toast.makeText(getContext(), "Viewing summary for " + consultation.getDoctorName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRateDoctorClick(Consultation consultation) {
                Toast.makeText(getContext(), "Rating doctor " + consultation.getDoctorName(), Toast.LENGTH_SHORT).show();
            }
        });
        rvPastConsultations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPastConsultations.setAdapter(adapter);
    }

    private void updateUI() {
        if (pastConsultations.isEmpty()) {
            tvNoPastConsultations.setVisibility(View.VISIBLE);
            rvPastConsultations.setVisibility(View.GONE);
        } else {
            tvNoPastConsultations.setVisibility(View.GONE);
            rvPastConsultations.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
        if (pastConsultations != null) {
            pastConsultations.removeAllChangeListeners();
        }
    }
}