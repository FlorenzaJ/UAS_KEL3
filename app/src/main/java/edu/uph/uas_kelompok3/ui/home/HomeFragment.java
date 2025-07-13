package edu.uph.uas_kelompok3.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.uph.uas_kelompok3.Adapter.PredictionAdapter;
import edu.uph.uas_kelompok3.Model.Predict;
import edu.uph.uas_kelompok3.R;
import edu.uph.uas_kelompok3.databinding.FragmentHomeBinding;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HomeFragment extends Fragment {
    TextView txvWelcome, txvStatus, txvLastUpdated;
    CardView crvPredict, consultation_card, crvHistory, crvSetting;
    RecyclerView rcvRecentPredictions;
    Realm realm;
    private RealmResults<Predict> predictions;
    PredictionAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupClickListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realm = Realm.getDefaultInstance();
        predictions = realm.where(Predict.class)
                .sort("createdAt", Sort.DESCENDING)
                .findAll();

        adapter = new PredictionAdapter(predictions);
        rcvRecentPredictions.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvRecentPredictions.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCurrentStatus();
    }

    public void initViews(View view) {
        txvWelcome = view.findViewById(R.id.txvWelcome);
        txvStatus = view.findViewById(R.id.txvStatus);
        txvLastUpdated = view.findViewById(R.id.txvLastUpdated);
        crvPredict = view.findViewById(R.id.crvPredict);
        consultation_card = view.findViewById(R.id.consultation_card);
        crvHistory = view.findViewById(R.id.crvHistory);
        crvSetting = view.findViewById(R.id.crvSetting);
        rcvRecentPredictions = view.findViewById(R.id.rcvRecentPredictions);
    }

    public void setupClickListeners() {
        crvPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
                    if (bottomNav != null) {
                        bottomNav.setSelectedItemId(R.id.nav_predict);
                    }
                }
            }
        });

        consultation_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
                    if (bottomNav != null) {
                        bottomNav.setSelectedItemId(R.id.nav_predict);
                    }
                }
            }
        });

//        crvHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to history screen
//                Intent intent = new Intent(getActivity(), HistoryActivity.class);
//                startActivity(intent);
//            }
//        });

//        crvSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to settings screen
//                Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public void updateCurrentStatus() {
        if (predictions != null && predictions.size() > 0) {
            Predict latest = predictions.first();
            if (latest != null) {
                String status = getStatusFromRisk(latest.getRiskLevel());
                txvStatus.setText(status);

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                txvLastUpdated.setText("Last updated: " + sdf.format(latest.getCreatedAt()));
            }
        } else {
            txvStatus.setText("No Data");
            txvLastUpdated.setText("No predictions yet");
        }
    }

    public String getStatusFromRisk(String riskLevel) {
        switch (riskLevel) {
            case "Low": return "Good";
            case "Moderate": return "Moderate";
            case "High": return "Poor";
            default: return "Unknown";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null) realm.close();
    }
}