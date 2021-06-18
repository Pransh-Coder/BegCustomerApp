package com.example.begcustomerapp.homeStructure.ui.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.drbegcustomer.EntryActivity;
import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.prefrences.LoginPrefrence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AccountSettingsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    NetworkingCalls networkingCalls;
    LoginPrefrence loginPrefrence;

    TextView name,phoneNum, location,noVaccinationFound;
    FloatingActionButton floatingActionButton;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        networkingCalls = new NetworkingCalls(getActivity(),getContext());
        loginPrefrence = new LoginPrefrence(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        name = view.findViewById(R.id.name);
        phoneNum = view.findViewById(R.id.phoneNum);
        location = view.findViewById(R.id.location);
        noVaccinationFound = view.findViewById(R.id.noVaccinationFound);
        floatingActionButton = view.findViewById(R.id.fab);
        logout = view.findViewById(R.id.logout);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        name.setText(loginPrefrence.getFirstName()+" "+loginPrefrence.getLastName());
        phoneNum.setText(loginPrefrence.getPhoneNumber());
        location.setText(loginPrefrence.getLocation());
        Log.e("valuesFromPrefs", ""+loginPrefrence.getFirstName()+"  "+loginPrefrence.getPhoneNumber()+" "+loginPrefrence.getLocation());
        networkingCalls.getConsultationdetails(recyclerView,noVaccinationFound);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_accountSettingsFragment_to_addPatientFragment);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder logout_alert = new AlertDialog.Builder(getContext());
                logout_alert.setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {

                            public void onClick(DialogInterface dialog, int which) {
                                loginPrefrence.removeLoginPrefrence();
                                Intent intent = new Intent(getContext(), EntryActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).setNegativeButton("Cancel", null);
                logout_alert.show();
            }
        });
    }
}