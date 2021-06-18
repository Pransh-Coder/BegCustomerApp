package com.example.begcustomerapp.entryStructure;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.homeStructure.HomeActivity;
import com.customer.drbegcustomer.networkingStructure.BegURL;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.networkingStructure.NetworkingInterface;
import com.customer.drbegcustomer.prefrences.LoginPrefrence;
import com.hbb20.CountryCodePicker;


public class LoginFragment extends Fragment implements NetworkingInterface {

    TextView signupTxt;
    EditText enterPhoneNum,enterOTP;
    Button loginBtn;
    NetworkingCalls networkingCalls;
    CountryCodePicker countryCodePicker;
    LinearLayout linearOTP,forgotPassword;
    private LoginPrefrence loginPrefrence;
    BegURL begURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signupTxt = view.findViewById(R.id.signupTxt);
        enterPhoneNum = view.findViewById(R.id.customer_info_phone);
        countryCodePicker = view.findViewById(R.id.countryCodePicker);
        enterOTP = view.findViewById(R.id.enterOTP);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        loginBtn = view.findViewById(R.id.loginBtn);
        linearOTP = view.findViewById(R.id.linearOTP);
        networkingCalls = new NetworkingCalls(getContext(),getActivity(),this);
        begURL = new BegURL();
        loginPrefrence = new LoginPrefrence(getContext());

        signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkinput()){

                    networkingCalls.login(countryCodePicker.getSelectedCountryCodeWithPlus() + enterPhoneNum.getText().toString(), enterOTP.getText().toString());


                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(begURL.getRESET_PASSWORD()));
                startActivity(viewIntent);
            }
        });
    }


    //check if user has entered all values on registration page
    public boolean checkinput()
    {
        if(enterPhoneNum.getText().toString().isEmpty())
        {
            Toast toast=Toast.makeText(getActivity(),"Please enter phone number",Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        if(enterOTP.getText().toString().isEmpty())
        {
            Toast toast=Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        return true;
    }


    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error) {
    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error, Object o) {
        if (type==MethodType.login && status){
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (type==MethodType.login && !status){
            Toast.makeText(getContext(), "Unable to Login!", Toast.LENGTH_SHORT).show();
        }
    }
}