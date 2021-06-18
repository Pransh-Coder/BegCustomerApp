package com.example.begcustomerapp.homeStructure.ui.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.adapter.ViewPagerAdapter;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;

public class InfoFragment extends Fragment {

    private InfoViewModel notificationsViewModel;
    private ConstraintLayout doctor_layout, clinic_layout;
    private Button select_doctor, select_clinic;
    NetworkingCalls networkingCalls;
    ViewPager viewPager;

    //for doctor
    TextView field1,institueExp,doctor_name,clinic_more_info;

    //for clinic
    TextView clinic_field,clinic_doctor_name;

    WebView clinic_institueFacilities;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(InfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_info, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        final TextView textView = root.findViewById(R.id.text_notifications);
        doctor_layout = root.findViewById(R.id.doctor_layout);
        clinic_layout=  root.findViewById(R.id.clinic_layout);
        viewPager = root.findViewById(R.id.viewPager);
        select_doctor = root.findViewById(R.id.select_doctor_layout);
        select_clinic = root.findViewById(R.id.select_clinic_layout);
        field1 = root.findViewById(R.id.field1);
        institueExp = root.findViewById(R.id.institue);
        doctor_name = root.findViewById(R.id.doctor_name);
        clinic_more_info = root.findViewById(R.id.clinic_more_info);

        clinic_field = root.findViewById(R.id.clinic_field);
        clinic_institueFacilities  = root.findViewById(R.id.clinic_institue);
        clinic_doctor_name = root.findViewById(R.id.clinic_doctor_name);

        networkingCalls = new NetworkingCalls(getActivity(),getContext());

        networkingCalls.getDoctorDetails(field1,institueExp,doctor_name);
        networkingCalls.getHospitalDetails(clinic_field,clinic_institueFacilities);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);


        select_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clinic_layout.setVisibility(View.GONE);
                doctor_layout.setVisibility(View.VISIBLE);
            }
        });

        select_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctor_layout.setVisibility(View.GONE);
                clinic_layout.setVisibility(View.VISIBLE);

            }
        });
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        clinic_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir//Anuj+Apartment,+Dalibagh+Colony,+Butler+Colony,+Lucknow,+Uttar+Pradesh+226001/@26.8504062,80.9247653,13z/data=!4m8!4m7!1m0!1m5!1m1!1s0x399bfd1734077a97:0xb6eef15af6464182!2m2!1d80.9597848!2d26.8504105"));
                startActivity(intent);
            }
        });
    }
}