package edu.uph.uas_kelompok3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import io.realm.Realm;
import edu.uph.uas_kelompok3.Model.Consultation;

import java.util.Date;
import java.util.UUID;

public class ConsultFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageView btnBack;
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        if (realm.where(Consultation.class).findAll().isEmpty()) {
            realm.executeTransaction(r -> {
                Consultation consultation = r.createObject(Consultation.class, UUID.randomUUID().toString());
                consultation.setDoctorName("Dr. Sarah Johnson");
                consultation.setStatus("Upcoming");
                consultation.setAppointmentDate(new Date());
                consultation.setAppointmentTime("14:30");
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        btnBack = view.findViewById(R.id.btnBack);

        // Setup ViewPager with an adapter
        ConsultationPagerAdapter pagerAdapter = new ConsultationPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Link TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Upcoming");
                            break;
                        case 1:
                            tab.setText("Available");
                            break;
                        case 2:
                            tab.setText("Past");
                            break;
                    }
                }).attach();

        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        return view;
    }

    private static class ConsultationPagerAdapter extends FragmentStateAdapter {
        public ConsultationPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new UpcomingConsultationsFragment();
                case 1:
                    return new AvailableDoctorsFragment();
                case 2:
                    return new PastConsultationsFragment();
                default:
                    return new AvailableDoctorsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}