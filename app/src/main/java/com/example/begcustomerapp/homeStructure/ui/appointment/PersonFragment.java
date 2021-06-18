package com.example.begcustomerapp.homeStructure.ui.appointment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.homeStructure.HomeActivity;
import com.customer.drbegcustomer.model.PatientNamesPojo;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.networkingStructure.NetworkingInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PersonFragment extends Fragment implements NetworkingInterface {

    public static final String TAG = "PersonFragment";
    private TextInputLayout app_age, app_date, symptoms, symptoms_description;
    private TextInputEditText app_age_text, app_date_text, symptoms_text, symptoms_description_text;
    private Button book_btn;
    private ImageButton datebtn;
    private TextView dateText, dateText11;
    private Spinner patient_name_spinner_p;
    private NetworkingCalls networkingCalls;
    private String patient_name, dateTosend;
    private List<PatientNamesPojo> patientDetailsPojoList;

    public static String date, current_date, convertedCurrentDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app_age = view.findViewById(R.id.app_age);
        symptoms = view.findViewById(R.id.symptoms);
        app_age_text = view.findViewById(R.id.app_age_text);
        symptoms_text = view.findViewById(R.id.symptoms_text);
        book_btn = view.findViewById(R.id.book_btn);
        datebtn = view.findViewById(R.id.datebtn);
        dateText = view.findViewById(R.id.dateText);
        dateText11 = view.findViewById(R.id.dateText11);

        dateText.setEnabled(false);

        patient_name_spinner_p = view.findViewById(R.id.patient_name_spinner_p);
        patientDetailsPojoList = new ArrayList<>();
        networkingCalls = new NetworkingCalls(getContext(), getActivity(), this);
        networkingCalls.getPatientFamilyDetails(patient_name_spinner_p);
        Log.i("Patient Details", patientDetailsPojoList.toString());
        patient_name_spinner_p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    patient_name=" ";
                }
                else{
                    patient_name = patient_name_spinner_p.getItemAtPosition(i).toString();
                    networkingCalls.getPatientAge(patient_name, app_age_text);
                    Log.e("Patient Selected", patient_name);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(dateText);
            }
        });


        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = dateTosend;
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    convertedCurrentDate =sdf1.format(sdf1.parse(date));
                    Log.e("tag date", ""+convertedCurrentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                current_date = dateTosend;
                String st = patient_name_spinner_p.getSelectedItem().toString();
                int pos = patient_name_spinner_p.getSelectedItemPosition();
                if (pos == 0) {
                    Toast.makeText(getActivity(), "Select Patient Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (app_age_text.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Age", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dateText.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Select Appointment Date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (symptoms_text.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Calendar calendar = Calendar.getInstance();
                    String time = calendar.getTime().toString();
                    Log.e("date", current_date);
                    networkingCalls.bookAppointment(patient_name, symptoms_text.getText().toString(),"", "OPD", PersonFragment.current_date, time);
                }
            }
        });

        app_age_text.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
    }

    private void showDateDialog(final TextView dateText) {
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                dateText.setText(sdf.format(calendar.getTime()));
                dateText11.setText(simpleDateFormat.format(calendar.getTime()));
                dateTosend = simpleDateFormat.format(calendar.getTime());
                dateText.setTextColor(getResources().getColor(R.color.charcoal));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000)+(1000*60*60*24*6));
        datePickerDialog.show();
    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error) {

        if (type==MethodType.bookAppointment && status){
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        else if (type==MethodType.bookAppointment && !status){
            Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error, Object o) {

    }
}