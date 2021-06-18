package com.example.begcustomerapp.homeStructure.ui.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.networkingStructure.NetworkingInterface;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddPatientFragment extends Fragment implements NetworkingInterface {

    Spinner gender_spinner;
    String gender_selection="";
    Button submit;
    NetworkingCalls networkingCalls;
    EditText enterName,enterLastName,enterMothersName;
    View view1;
    ImageButton  dobbtnc;
    private TextInputEditText dobTextc, dobText;
    public static String date, dob = "", convertedCurrentDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_patient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gender_spinner = view.findViewById(R.id.gender_spinner);
        submit = view.findViewById(R.id.submit);
        enterName = view.findViewById(R.id.name);
        enterLastName = view.findViewById(R.id.enterLastName);
        enterMothersName = view.findViewById(R.id.enterMothersName);
        dobbtnc = view.findViewById(R.id.dobbtnc);
        dobTextc = view.findViewById(R.id.dobTextc);
        dobTextc.setEnabled(false);

        view1=view;
        networkingCalls = new NetworkingCalls(getContext(),getActivity(),this);

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(
                getContext(), R.array.gender_list, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(adapterGender);


        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position==1){
                    gender_selection="Male";
                }else if (position==2){
                    gender_selection="Female";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dobbtnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDobDialog(dobTextc);
            }
        });

        dob = date;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInput()){
                    Log.e("Add patient", "" + dob + date);
                    networkingCalls.addPatient( view , enterName.getText().toString(), date,gender_selection,enterLastName.getText().toString(),enterMothersName.getText().toString());



                }
            }
        });


    }

    public boolean checkInput(){
        String st = gender_spinner.getSelectedItem().toString();
        int pos = gender_spinner.getSelectedItemPosition();
        if (enterName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Enter First Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (enterLastName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Enter Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }  else if (dobTextc.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Enter Date of Birth", Toast.LENGTH_SHORT).show();
            return false;
        } else if (enterMothersName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Enter Mother's Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pos == 0) {
            Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error) {

        if (type==MethodType.addPatient&&status){
            Toast.makeText(getContext(), "Data added sucessfully!", Toast.LENGTH_SHORT).show();
        }
        else if (type==MethodType.addPatient&&!status){
            Toast.makeText(getContext(), "Error in adding data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error, Object o) {

    }

    private void showDobDialog(final TextInputEditText dobTextc) {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DATE);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                dobTextc.setText(sdf.format(calendar.getTime()));
                dobTextc.setText(simpleDateFormat.format(calendar.getTime()));
                date = simpleDateFormat.format(calendar.getTime());
                dobTextc.setTextColor(getResources().getColor(R.color.charcoal));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
        datePickerDialog.show();
    }
}