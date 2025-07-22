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

import edu.uph.uas_kelompok3.Adapter.UpcomingConsultationAdapter;
import edu.uph.uas_kelompok3.Model.Consultation;
import edu.uph.uas_kelompok3.R;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class UpcomingConsultationsFragment extends Fragment {

    private RecyclerView rvUpcomingConsultations;
    private TextView tvNoUpcomingConsultations;
    private Realm realm;
    private UpcomingConsultationAdapter adapter;
    private RealmResults<Consultation> upcomingConsultations;
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
        View view = inflater.inflate(R.layout.fragment_upcoming_consultations, container, false);

        rvUpcomingConsultations = view.findViewById(R.id.rv_upcoming_consultations);
        tvNoUpcomingConsultations = view.findViewById(R.id.tv_no_upcoming_consultations);

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
            tvNoUpcomingConsultations.setText("No upcoming consultations.");
            tvNoUpcomingConsultations.setVisibility(View.VISIBLE);
            rvUpcomingConsultations.setVisibility(View.GONE);
            return view;
        }

        upcomingConsultations = realm.where(Consultation.class)
                .equalTo("userId", currentUserId)
                .equalTo("status", "Upcoming")
                .greaterThanOrEqualTo("appointmentDate", startOfToday)
                .sort("appointmentDate", Sort.ASCENDING)
                .sort("appointmentTime", Sort.ASCENDING)
                .findAll();

        setupRecyclerView();
        updateUI();

        upcomingConsultations.addChangeListener(consultations -> {
            adapter.notifyDataSetChanged();
            updateUI();
        });

        return view;
    }

    private void setupRecyclerView() {
        adapter = new UpcomingConsultationAdapter(upcomingConsultations, new UpcomingConsultationAdapter.OnActionListener() {
            @Override
            public void onJoinNowClick(Consultation consultation) {
                Toast.makeText(getContext(), "Joining call with " + consultation.getDoctorName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRescheduleClick(Consultation consultation) {
                Toast.makeText(getContext(), "Rescheduling appointment with " + consultation.getDoctorName(), Toast.LENGTH_SHORT).show();
            }
        });
        rvUpcomingConsultations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUpcomingConsultations.setAdapter(adapter);
    }

    private void updateUI() {
        if (upcomingConsultations.isEmpty()) {
            tvNoUpcomingConsultations.setVisibility(View.VISIBLE);
            rvUpcomingConsultations.setVisibility(View.GONE);
        } else {
            tvNoUpcomingConsultations.setVisibility(View.GONE);
            rvUpcomingConsultations.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
        if (upcomingConsultations != null) {
            upcomingConsultations.removeAllChangeListeners();
        }
    }
}