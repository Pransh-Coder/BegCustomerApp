package com.example.begcustomerapp.homeStructure.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.homeStructure.HomeActivity;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;


public class QuestionnaireFragment extends Fragment {

    String nam,dob,sex , id;
    EditText enterName,enterDob,enterSex;

    EditText enterFatherWeight,enterMotherWeight,enterChildWeight,enterFatherHeight,enterMotherHeight,enterChildHeight,enterPlaceofBirth,enterNoOfSiblings,enterChiefComplaint,enterBirthOrder;
    Spinner type_spinner,chronic_spinner;

    RadioGroup radioTerm,radioPrevHosp,radioGroupConsanguineous,radioGroupDrug;

    RadioButton radioFullTerm,radioPreTerm,radioYesHosp,radioNoHosp,radioYesConsanguineous,radioNoConsanguineous,radioYesDrug,radioNoDrug;

    String term_radio_option="",prevHosp_radio_option="",consanguineous_radio_option="",drug_radio_option="",spinner_type_delivery_option="",spinner_type_chronic_option="";

    Button submit;

    NetworkingCalls networkingCalls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enterName = view.findViewById(R.id.enterName);
        enterDob = view.findViewById(R.id.enterDob);
        enterSex = view.findViewById(R.id.enterSex);

        enterFatherWeight = view.findViewById(R.id.enterFatherWeight);
        enterMotherWeight = view.findViewById(R.id.enterMotherWeight);
        enterChildWeight = view.findViewById(R.id.enterChildWeight);
        enterFatherHeight = view.findViewById(R.id.enterFatherHeight);
        enterMotherHeight = view.findViewById(R.id.enterMotherHeight);
        enterChildHeight = view.findViewById(R.id.enterChildHeight);
        enterPlaceofBirth = view.findViewById(R.id.enterPlaceofBirth);
        enterNoOfSiblings = view.findViewById(R.id.enterNoOfSiblings);
        enterChiefComplaint = view.findViewById(R.id.enterChiefComplaint);
        enterBirthOrder = view.findViewById(R.id.enterBirthOrder);

        radioTerm = view.findViewById(R.id.radioTerm);
        radioPrevHosp = view.findViewById(R.id.radioPrevHosp);
        radioGroupConsanguineous = view.findViewById(R.id.radioGroupConsanguineous);
        radioGroupDrug = view.findViewById(R.id.radioGroupDrug);

        radioFullTerm = view.findViewById(R.id.radioFullTerm);
        radioPreTerm = view.findViewById(R.id.radioPreTerm);
        radioYesHosp = view.findViewById(R.id.radioYesHosp);
        radioNoHosp = view.findViewById(R.id.radioNoHosp);
        radioYesConsanguineous = view.findViewById(R.id.radioYesConsanguineous);
        radioNoConsanguineous = view.findViewById(R.id.radioNoConsanguineous);
        radioYesDrug = view.findViewById(R.id.radioYesDrug);
        radioNoDrug = view.findViewById(R.id.radioNoDrug);

        type_spinner = view.findViewById(R.id.type_spinner);
        chronic_spinner = view.findViewById(R.id.chronic_spinner);

        submit = view.findViewById(R.id.submit);

        networkingCalls = new NetworkingCalls(getActivity(),getContext());

        if (getArguments() != null) {

            nam = getArguments().getString("name");
            dob = getArguments().getString("dob");
            sex = getArguments().getString("sex");
            id = getArguments().getString("id");
            enterName.setText(nam);
            enterName.setEnabled(false);
            enterDob.setText(dob);
            enterDob.setEnabled(false);
            enterSex.setText(sex);
            enterSex.setEnabled(false);
        }


        radioTerm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == radioFullTerm.getId()) {
                    term_radio_option = "Full Term";

                } else if (i == radioPreTerm.getId()) {
                    term_radio_option = "Pre Term";

                }
            }
        });

        radioPrevHosp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == radioYesHosp.getId()) {

                    prevHosp_radio_option="Yes";

                } else if (i == radioNoHosp.getId()) {

                    prevHosp_radio_option="No";

                }
            }
        });

        radioGroupConsanguineous.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == radioYesConsanguineous.getId()) {

                    consanguineous_radio_option="Yes";

                } else if (i == radioNoConsanguineous.getId()) {

                    consanguineous_radio_option="No";

                }
            }
        });

        radioGroupDrug.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == radioYesDrug.getId()) {

                    drug_radio_option="Yes";

                } else if (i == radioNoDrug.getId()) {

                    drug_radio_option="No";

                }
            }
        });

        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(getContext(), R.array.type_of_delivery, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(adapterType);

        ArrayAdapter<CharSequence> adapterIlness = ArrayAdapter.createFromResource(getContext(), R.array.type_of_illness, android.R.layout.simple_spinner_item);
        adapterIlness.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chronic_spinner.setAdapter(adapterIlness);


        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    spinner_type_delivery_option = "Normal";
                } else if (position == 2) {
                    spinner_type_delivery_option = "Cesarean";
                } else if (position == 3) {
                    spinner_type_delivery_option = "Instrumental";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        chronic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    spinner_type_chronic_option = "Thyroid Disorder";
                } else if (position == 2) {
                    spinner_type_delivery_option = "Diabetes";
                } else if (position == 3) {
                    spinner_type_delivery_option = "Hypertension";
                }else if (position == 4) {
                    spinner_type_delivery_option = "Asthama";
                }else if (position == 5) {
                    spinner_type_delivery_option = "Tuberclosis";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                networkingCalls.fillQuestionnaire(enterFatherWeight.getText().toString(),enterMotherWeight.getText().toString(),enterChildWeight.getText().toString(),enterFatherHeight.getText().toString(),enterMotherHeight.getText().toString(),enterChildHeight.getText().toString(),enterPlaceofBirth.getText().toString(),spinner_type_delivery_option,term_radio_option,prevHosp_radio_option,enterNoOfSiblings.getText().toString(),enterBirthOrder.getText().toString(),consanguineous_radio_option,drug_radio_option,spinner_type_chronic_option,enterChiefComplaint.getText().toString(),id);
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}