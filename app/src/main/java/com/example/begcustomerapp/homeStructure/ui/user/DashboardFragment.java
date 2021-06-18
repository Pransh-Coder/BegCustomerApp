package com.example.begcustomerapp.homeStructure.ui.user;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.customer.drbegcustomer.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private CardView accounts_card, consultation_card;
    public static int flag = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accounts_card = view.findViewById(R.id.card_account_settings_option);
        consultation_card = view.findViewById(R.id.card_consultion_settings_option);
        flag = 1;
        Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_accountSettingsFragment);
        accounts_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                Navigation.findNavController(v).navigate(R.id.action_navigation_user_to_accountSettingsFragment);
            }
        });

        consultation_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_consultationFragment);
            }
        });

    }
}