package com.example.begcustomerapp.homeStructure.ui.appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.model.PatientNamesPojo;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.prefrences.LoginPrefrence;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.customer.drbegcustomer.customeDialogs.AppointmentDialog.booking;
//import static org.webrtc.ContextUtils.getApplicationContext;

public class CallFragment extends Fragment {

    public static final String TAG = "CallFragment";
    private TextInputLayout patient_age, patient_date, patient_time, patient_symptoms;
    private TextInputEditText patient_age_text, patient_symptoms_text, description, date_info,time_info;
    private Button pay_btn;
    private EditText dateTextc, timeText, dateText1,timeText1;
    private Spinner patient_name_spinner;
    String day;
    private NetworkingCalls networkingCalls;
    public static String id;
    private String patient_name;
    ImageButton datebtnc, timebtn;
    TextView fees, about_text, timeNote;
    String amt, String_date;

    LoginPrefrence loginPrefrence;

    CheckBox checkBox;


    String convertedCurrentDate, convertedCurrentDatetime;
    String family_id="", age_text="";
    List<PatientNamesPojo> patientNamesPojoList=new ArrayList<>();
    String appointment_type;
    String date_get, time_get,data_pick,time_pick;

    public static String problem, description_data, consultation_type, date, time, price, family, current_date, current_time;
    String order;
    int refNo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patient_age = view.findViewById(R.id.patient_age);
        patient_time = view.findViewById(R.id.person_text);
        patient_symptoms = view.findViewById(R.id.patient_symptoms);
        patient_age_text = view.findViewById(R.id.patient_age_text);
        patient_symptoms_text = view.findViewById(R.id.patient_symptoms_text);
        datebtnc = view.findViewById(R.id.datebtnc);
        date_info = view.findViewById(R.id.date_get_edit);
        about_text = view.findViewById(R.id.about_text);
        time_info = view.findViewById(R.id.time_get_edit);
        timebtn = view.findViewById(R.id.timebtn);
        dateTextc = view.findViewById(R.id.dateTextc);
        dateText1 = view.findViewById(R.id.dateText1);
        timeText1 = view.findViewById(R.id.timeText1);
        timeText = view.findViewById(R.id.timeText);
        timeNote = view.findViewById(R.id.timeNote);
        pay_btn = view.findViewById(R.id.pay_btn);
        fees = view.findViewById(R.id.fees);
        patient_name_spinner = view.findViewById(R.id.patient_name_spinner);
        checkBox = view.findViewById(R.id.checkBox);

        dateTextc.setEnabled(false);
        timeText.setEnabled(false);
        loginPrefrence = new LoginPrefrence(getContext());

        patient_age_text.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();
        order=""+uniqueID;

        if (booking == 1){
            appointment_type = "Video Call";
            fees.setText("Consultation charges: Rs. 1000");
            about_text.setText("Book a video appointment");

        }
        if (booking == 2){
            appointment_type = "Audio Call";
            fees.setText("Consultation charges: Rs. 700");
            about_text.setText("Book an audio appointment");
        }
        if (booking == 3){
            appointment_type = "OPD";
        }

        networkingCalls = new NetworkingCalls(getActivity(),getContext());
        networkingCalls.getPatientFamilyDetails(patient_name_spinner);


        Random random = new Random();
        refNo = random.nextInt(9999999-1000000)+1000000;

        //startPayment();
        String[] sp = timeText.getText().toString().split(":");

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = patient_name_spinner.getSelectedItem().toString();
                int pos = patient_name_spinner.getSelectedItemPosition();
                if (pos == 0) {
                    Toast.makeText(getActivity(), "Select Patient Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dateTextc.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Select Appointment Date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (timeText.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Select Appointment Time", Toast.LENGTH_SHORT).show();
                    return;
                } else if (timeNote.getVisibility() == View.VISIBLE) {
                    Toast.makeText(getActivity(), "Select Appointment Time within the slot", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (patient_symptoms_text.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Details", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!checkBox.isChecked()) {
                    Toast.makeText(getActivity(), "Please Check that I understand", Toast.LENGTH_SHORT).show();
                    return;
                }

                problem = patient_symptoms_text.getText().toString();
                consultation_type = appointment_type;

                date = dateText1.getText().toString();
                time = timeText.getText().toString();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    convertedCurrentDate =sdf1.format(sdf1.parse(date));
                    Log.e("tag date", ""+convertedCurrentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
                try {
                    convertedCurrentDatetime =sdf1.format(sdf2.parse(time));
                    Log.e("tag date", ""+convertedCurrentDate + "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("edittext_date_time",""+date+"           "+time);
                family = id;
                current_date = convertedCurrentDate;
                current_time =  convertedCurrentDatetime;
//                startPayment();
                Log.e("consultation_id", CallFragment.id+"");

                networkingCalls.initiatePayment(pay_btn, about_text);
                //This should be done after payment ......
            }
        });

        patient_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    patient_name=" ";
                }
                else{
                    patient_name = patient_name_spinner.getItemAtPosition(i).toString();
                    id = NetworkingCalls.patientNamesPojos.get(i).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        datebtnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(dateTextc);
            }
        });

        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeText.setText("");
                timeText1.setText("");
                showTimeDialog(timeText);
            }
        });

    }

    private void showTimeDialog(final TextView timeText) {
        if (dateTextc.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Select Appointment Date First", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                SimpleDateFormat sdfdate = new SimpleDateFormat("dd/MM/yyyy");
                String currentdate = sdfdate.format(new Date());

                if(String_date.equalsIgnoreCase(currentdate) ){
                    Log.e("dateif", "ok");

                    if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                        timeText.setText(sdf.format(calendar.getTime()));
                        Log.e("Time", timePicker.getCurrentHour()+"");

                        boolean bookedOnSameDay = true;

                        networkingCalls.getCorrectTime(timePicker , timeNote , bookedOnSameDay, day);
                    }
                    else {
                        Toast.makeText(getContext(), "Please select a valid time", Toast.LENGTH_SHORT).show();
                        timeText.setTextColor(getResources().getColor(R.color.charcoal));
                    }
                }

                else {

                    boolean bookedOnSameDay = false;

                    Log.e("dateelse", "ok");
                    timeText.setText(sdf.format(calendar.getTime()));
                    Log.e("Time", timePicker.getCurrentHour()+"");

                    networkingCalls.getCorrectTime(timePicker , timeNote , bookedOnSameDay, day);

                    timeText1.setText(simpleDateFormat.format(calendar.getTime()));
                }
            }
        };
        new TimePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private void showDateDialog(final TextView dateText) {
        String_date ="";

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
                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(i, i1, i2-1);
                day = simpledateformat.format(date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dateText1.setText(simpleDateFormat.format(calendar.getTime()));
                dateTextc.setText(sdf.format(calendar.getTime()));
                String_date = sdf.format(calendar.getTime());
                dateTextc.setTextColor(getResources().getColor(R.color.charcoal));
                datePicker.setMinDate(System.currentTimeMillis()-1000);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000)+(1000*60*60*24*6));
        datePickerDialog.show();
    }
}