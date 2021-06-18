package com.example.begcustomerapp.entryStructure;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.homeStructure.HomeActivity;
import com.customer.drbegcustomer.prefrences.LoginPrefrence;

public class SplashFragment extends Fragment {

    LoginPrefrence loginPrefrence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginPrefrence = new LoginPrefrence(getContext());

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (loginPrefrence.isLoggedIn())
                {
                    if (isAdded()) {
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        getContext().startActivity(intent);
                        getActivity().finish();
                    }
                }
                else {
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment2);
                }
            }
        }.start();// afterDelay will be executed after (secs*1000) milliseconds.
    }


}