package com.example.begcustomerapp.entryStructure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;

public class ForgotPasswordFragment extends Fragment {

    EditText enterEmail;
    Button submitBtn;
    NetworkingCalls networkingCalls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enterEmail = view.findViewById(R.id.enterEmail);
        submitBtn = view.findViewById(R.id.submitBtn);
        networkingCalls = new NetworkingCalls(getActivity(),getContext());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkinput()){

                    networkingCalls.forgetPassword(enterEmail.getText().toString());
                }
            }
        });
    }


    public boolean checkinput()
    {
        if(enterEmail.getText().toString().isEmpty())
        {
            Toast toast=Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        return true;
    }
}