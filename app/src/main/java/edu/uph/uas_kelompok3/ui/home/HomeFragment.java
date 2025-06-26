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

import java.util.ArrayList;
import java.util.List;

import edu.uph.uas_kelompok3.Adapter.PredictionAdapter;
import edu.uph.uas_kelompok3.Model.Predict;
import edu.uph.uas_kelompok3.R;
import edu.uph.uas_kelompok3.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    TextView txvWelcome, txvStatus, txvLastUpdated;
    CardView crvPredict, consultation_card, crvHistory, crvSetting;
    RecyclerView rcvRecentPredictions;
    private static PredictionAdapter predictionAdapter;
    List<Predict> predictionList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        return view;
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

    public void setupRecyclerView() {
        predictionList = new ArrayList<>();
        // Sample data
        predictionList.add(new Predict("Apr 15, 2025", "Good", "Low Risk"));
        predictionList.add(new Predict("Mar 30, 2025", "Good", "Low Risk"));
        predictionList.add(new Predict("Mar 15, 2025", "Moderate", "Medium Risk"));

        predictionAdapter = new PredictionAdapter(predictionList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvRecentPredictions.setLayoutManager(layoutManager);
        rcvRecentPredictions.setAdapter(predictionAdapter);

        rcvRecentPredictions.setNestedScrollingEnabled(false);
        rcvRecentPredictions.setHasFixedSize(true);
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
}