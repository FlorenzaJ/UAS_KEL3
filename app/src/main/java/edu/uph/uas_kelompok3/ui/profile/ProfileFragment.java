package edu.uph.uas_kelompok3.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.uph.uas_kelompok3.MainActivity;
import edu.uph.uas_kelompok3.R;

public class ProfileFragment extends Fragment {
    TextView tvFullName, tvEmail, tvGender, tvTanggalLahir;
    TextView tvFullNameInfo, tvEmailInfo;
    Button btnSignOut, btnEditProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        initializeViews(view);

        // Load user data
        loadUserData();

        // Setup button listeners
        setupButtonListeners();

        return view;
    }

    private void initializeViews(View view) {
        tvFullName = view.findViewById(R.id.tvFullName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvFullNameInfo = view.findViewById(R.id.tvFullNameInfo);
        tvEmailInfo = view.findViewById(R.id.tvEmailInfo);
        tvGender = view.findViewById(R.id.tvGender);
        tvTanggalLahir = view.findViewById(R.id.tvTanggalLahir);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
    }

    private void loadUserData() {
        // Get data from MainActivity
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();

            String nama = mainActivity.getUserNama();
            String email = mainActivity.getUserEmail();
            String gender = mainActivity.getUserGender();
            String tanggalLahir = mainActivity.getUserTanggalLahir();

            // Set data to views
            tvFullName.setText(nama);
            tvEmail.setText(email);
            tvFullNameInfo.setText(nama);
            tvEmailInfo.setText(email);
            tvGender.setText(gender);
            tvTanggalLahir.setText(tanggalLahir);
        }
    }

    private void setupButtonListeners() {
        if (btnSignOut != null) {
            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle sign out logic
                    if (getActivity() != null) {
                        getActivity().finish();
                        // You can also redirect to login activity here
                    }
                }
            });
        }

        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to EditProfileFragment using getParentFragmentManager()
                    if (getActivity() != null) {
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_main, new EditProfileFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }
    }
}