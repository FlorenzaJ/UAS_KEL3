package edu.uph.uas_kelompok3;

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
        pastConsultations = realm.where(Consultation.class)
                .beginGroup()
                .equalTo("status", "Completed")
                .or()
                .equalTo("status", "Cancelled")
                .endGroup()
                .lessThan("appointmentDate", new Date()) // Hanya yang sudah lewat
                .sort("appointmentDate", Sort.DESCENDING)
                .sort("appointmentTime", Sort.DESCENDING)
                .findAll();

        setupRecyclerView();
        updateUI();

        // Listen for changes in RealmResults
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