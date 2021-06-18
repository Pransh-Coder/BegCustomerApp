package com.example.begcustomerapp.homeStructure.ui.user;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.google.android.material.appbar.AppBarLayout;


public class ConsultationFragment extends Fragment {

    RecyclerView recyclerView_consultation;
    RecyclerView.LayoutManager layoutManager;
    NetworkingCalls networkingCalls;
    TextView noConsTxt;
    Button fab_export;
    AppBarLayout appBarLayout;
    String id;
    ImageView backbtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView_consultation = view.findViewById(R.id.recyclerviewConsultation);
        fab_export = view.findViewById(R.id.fab_export);
        backbtn = view.findViewById(R.id.backbtn);
        noConsTxt = view.findViewById(R.id.noConsTxt);
        appBarLayout = view.findViewById(R.id.appBarLayout);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView_consultation.setLayoutManager(layoutManager);
        networkingCalls = new NetworkingCalls(getActivity(),getContext());

        Bundle mBundle = new Bundle();
        mBundle = getArguments();
        id =  mBundle.getString("family_id");  // key must be same which was given in first fragment

        Log.e("ConsultationFragment", ""+id);


        networkingCalls.getFamilyConsultation(id,recyclerView_consultation,noConsTxt, fab_export);

        fab_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEmailDialog();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }


    private void showEmailDialog() {

        final Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ImageView ivClear = dialog.findViewById(R.id.ivClear);
        Button btnContinue = dialog.findViewById(R.id.sendBtn);
        EditText enterEmail = dialog.findViewById(R.id.enterEmail);


        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enterEmail.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please Enter Email!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    //api call to send email
                    networkingCalls.sendAndDownloadPdf(id,enterEmail.getText().toString());
                }
            }
        });
    }
}