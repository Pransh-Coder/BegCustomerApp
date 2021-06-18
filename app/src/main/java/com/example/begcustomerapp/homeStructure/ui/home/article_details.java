package com.example.begcustomerapp.homeStructure.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;

public class article_details extends Fragment {

    private ImageView article_image;
    private TextView article_title, article_details;
    NetworkingCalls networkingCalls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_details, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkingCalls = new NetworkingCalls(getActivity(), getContext());


        article_image = view.findViewById(R.id.article_imageView);
        article_title = view.findViewById(R.id.ariticle_title);
        article_details = view.findViewById(R.id.article_details);
        Bundle mBundle = new Bundle();
        mBundle = getArguments();
        String id =  mBundle.getString("id");  // key must be same which was given in first fragment

        Log.e("ConsultationFragment", ""+id);

        networkingCalls.getArticleDetails(id, article_image,article_title,article_details);

    }

}


