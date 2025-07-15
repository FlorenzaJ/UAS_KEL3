package edu.uph.uas_kelompok3.ui.profile;

import android.content.Intent;
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
import edu.uph.uas_kelompok3.Model.UserModel;
import edu.uph.uas_kelompok3.R;
import edu.uph.uas_kelompok3.RegisterActivity;

public class ProfileFragment extends Fragment {
    TextView tvFullName, tvEmail, tvGender, tvTanggalLahir;
    TextView tvFullNameInfo, tvEmailInfo;
    Button btnSignOut, btnEditProfile;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(view);
        loadUserData();
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
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            UserModel user = mainActivity.getUserModel();

            String nama = mainActivity.getUserNama();
            String email = mainActivity.getUserEmail();
            String gender = mainActivity.getUserGender();
            String tanggalLahir = mainActivity.getUserTanggalLahir();

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
                    if (getActivity() != null) {
                        getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();

                        Intent intent = new Intent(getActivity(), RegisterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish(); // Tutup activity sekarang
                    }
                }
            });
        }

        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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