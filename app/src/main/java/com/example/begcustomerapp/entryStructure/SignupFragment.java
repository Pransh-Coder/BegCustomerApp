package com.example.begcustomerapp.entryStructure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.homeStructure.HomeActivity;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.networkingStructure.NetworkingInterface;
import com.customer.drbegcustomer.prefrences.LoginPrefrence;
import com.hbb20.CountryCodePicker;


public class SignupFragment extends Fragment implements NetworkingInterface {

    AlertDialog alertDialog;
    CheckBox termscheck;


    TextView loginTxt, terms;
    EditText enterPhoneNum,enterFirstName,enterLastName,enterOTP,enterCity,enterEmail,enterPassword,confirmPassword;
    CountryCodePicker countryCodePicker;
    Button btnSignup;
    NetworkingCalls networkingCalls;


    LinearLayout linearOTP;

    private LoginPrefrence loginPrefrence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countryCodePicker = view.findViewById(R.id.countryCodePicker);
        loginTxt = view.findViewById(R.id.loginTxt);
        enterPhoneNum = view.findViewById(R.id.customer_info_phone);
        enterFirstName = view.findViewById(R.id.enterFirstName);
        enterLastName = view.findViewById(R.id.enterLastName);
        enterOTP = view.findViewById(R.id.enterOTP);
        enterCity = view.findViewById(R.id.enterCity);
        btnSignup = view.findViewById(R.id.btnSignup);
        linearOTP = view.findViewById(R.id.linearOTP);
        terms = view.findViewById(R.id.terms);
        termscheck = view.findViewById(R.id.termscheck);
        enterEmail = view.findViewById(R.id.enterEmail);
        enterPassword = view.findViewById(R.id.enterPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        networkingCalls = new NetworkingCalls(getContext(),getActivity(),this);

        loginPrefrence = new LoginPrefrence(getContext());


        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment);
            }
        });



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkinput()){

                    final String fname = enterFirstName.getText().toString();
                    String lasName = enterLastName.getText().toString();
                    final String mobile = enterPhoneNum.getText().toString();
                    final String city = enterCity.getText().toString();

                    networkingCalls.signup(countryCodePicker.getSelectedCountryCodeWithPlus() + mobile,fname, lasName,city,enterEmail.getText().toString(),enterPassword.getText().toString());
                }
            }
        });

          terms.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  alertDialog = new AlertDialog.Builder(getActivity()).create();
                  alertDialog.setTitle("Terms & Conditions");
                  final TextView message = new TextView(getContext());
                  // i.e.: R.string.dialog_message =>
                  // "Test this dialog following the link to dtmilano.blogspot.com"
                  FrameLayout container = new FrameLayout(getContext());
                  FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                  params.setMargins(20,10,10,10);
                  params.leftMargin = 50;
                  message.setLayoutParams(params);
                  container.addView(message);
                  message.setTextColor(getResources().getColor(R.color.black));
                  final SpannableString s =
                          new SpannableString(getContext().getText(R.string.dialog_message));
                  Linkify.addLinks(s, Linkify.WEB_URLS);
                  message.setText(s);
                  message.setMovementMethod(LinkMovementMethod.getInstance());
                  alertDialog.setView(container);
                  alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "AGREE", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.dismiss();
                            termscheck.setChecked(true);
                      }
                  });
                  alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "DISAGREE", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.dismiss();
                          termscheck.setChecked(false);
                      }
                  });
                  alertDialog.show();
              }
          });
    }

    //check if user has entered all values on registration page
    public boolean checkinput()
    {
        if(enterFirstName.getText().toString().isEmpty())
        {
            Toast toast=Toast.makeText(getActivity(),"Please enter first name",Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        else if (enterLastName.getText().toString().isEmpty()){
            Toast toast=Toast.makeText(getActivity(),"Please enter last name",Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        else if(enterPhoneNum.getText().toString().isEmpty())
        {
            Toast toast=Toast.makeText(getActivity(),"Please enter phone number",Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        else if(enterPhoneNum.getText().toString().length()!=10)
        {
            Toast toast=Toast.makeText(getActivity(),"Enter a valid phone number",Toast.LENGTH_LONG);
            toast.show();
            return false;
        } else if (termscheck.isChecked() == false) {
            Toast.makeText(getActivity(), "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (enterEmail.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter email id", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (enterPassword.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!enterPassword.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())){
            Toast.makeText(getActivity(), "please enter same password in both the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error) {

    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error, Object o) {
        if (type==MethodType.signUp && status){
            networkingCalls.login(countryCodePicker.getSelectedCountryCodeWithPlus() + enterPhoneNum.getText().toString(), enterPassword.getText().toString());
        }
        else if (type==MethodType.signUp && !status){
            Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
        }
        else if (type==MethodType.login && status){

            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        else if (type==MethodType.login && !status){
            Toast.makeText(getContext(), "Unable to Login!", Toast.LENGTH_SHORT).show();
        }
    }
}