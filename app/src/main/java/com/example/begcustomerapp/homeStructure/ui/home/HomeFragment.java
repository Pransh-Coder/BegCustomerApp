package com.example.begcustomerapp.homeStructure.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.customeDialogs.AppointmentDialog;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;

import static com.customer.drbegcustomer.homeStructure.ui.user.DashboardFragment.flag;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private ImageView vaccineImage, articleImage;
    private TextView vaccineText, articleText, vaccination_text, article_text, vaccination_list, select_family;
    private CardView vaccineCard, articleCard, appointmentCard, upcomingcard, previouscard, vaccineCard1, articleCard1;
    private RecyclerView articleRecyclerView, vaccineRecyclerView, previous_recycler;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager1;
    RecyclerView.LayoutManager layoutManager2;
    NetworkingCalls networkingCalls;
    LinearLayout upcoming_text, previous_text, upcoming_blue, previous_blue;
    TextView noVaccinationFound;

    ImageView addPatient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (NetworkingCalls.isSignup){
            Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_addPatientFragment);
            NetworkingCalls.isSignup=false;
        }

        flag = 0;
        vaccination_list = view.findViewById(R.id.vaccination_list);
        select_family = view.findViewById(R.id.select_family);
        vaccineImage = view.findViewById(R.id.vaccine_image);
        vaccineText = view.findViewById(R.id.vaccineText);
        noVaccinationFound = view.findViewById(R.id.noVaccinationFound);
        appointmentCard = view.findViewById(R.id.appointmentCard);
        articleImage = view.findViewById(R.id.articleImage);
        articleText = view.findViewById(R.id.articleText);
        vaccineCard = view.findViewById(R.id.vaccineCard);
        articleCard = view.findViewById(R.id.articleCard);

        vaccineCard1 = view.findViewById(R.id.vaccineCard1);
        articleCard1 = view.findViewById(R.id.articleCard1);
        article_text = view.findViewById(R.id.article_text);
        articleRecyclerView = view.findViewById(R.id.consultation_recycler);
        previous_recycler = view.findViewById(R.id.previous_recycler);
        vaccineRecyclerView = view.findViewById(R.id.vaccination_recycler);
        upcoming_text = view.findViewById(R.id.upcoming_text);
        previous_text = view.findViewById(R.id.previous_vaccination);
        upcoming_blue = view.findViewById(R.id.upcoming_blue);
        previous_blue = view.findViewById(R.id.previous_blue);
        addPatient =  view.findViewById(R.id.addPatient);

        upcomingcard = view.findViewById(R.id.upcoming_card);
        previouscard = view.findViewById(R.id.previous_card);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager2 = new LinearLayoutManager(getContext());
        articleRecyclerView.setLayoutManager(layoutManager);
        vaccineRecyclerView.setLayoutManager(layoutManager1);
        previous_recycler.setLayoutManager(layoutManager2);



        vaccineCard.setBackground((getContext().getResources().getDrawable(R.drawable.gradient_one)));

        networkingCalls = new NetworkingCalls(getActivity(), getContext());

        previous_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineRecyclerView.setVisibility(View.GONE);
                upcoming_blue.setVisibility(View.GONE);
                previous_text.setVisibility(View.GONE);
                previous_blue.setVisibility(View.VISIBLE);
                upcoming_text.setVisibility(View.VISIBLE);
                previous_recycler.setVisibility(View.VISIBLE);
                networkingCalls.getPreviousVaccinations(previous_recycler,noVaccinationFound);


            }
        });
        upcoming_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                previous_blue.setVisibility(View.GONE);
                upcoming_text.setVisibility(View.GONE);
                upcoming_blue.setVisibility(View.VISIBLE);
                previous_text.setVisibility(View.VISIBLE);
                previous_recycler.setVisibility(View.GONE);
                vaccineRecyclerView.setVisibility(View.VISIBLE);
                networkingCalls.getVaccineList(vaccineRecyclerView,noVaccinationFound);
            }
        });

        appointmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentDialog dialog = new AppointmentDialog(getContext(),getActivity(), view);
                dialog.show();

            }
        });

        vaccineCard.setOnClickListener(this);
        articleCard.setOnClickListener(this);

        networkingCalls.getVaccineList(vaccineRecyclerView, noVaccinationFound);


        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_addPatientFragment);
            }
        });

        articleCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleCard.performClick();
            }
        });

        vaccineCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineCard.performClick();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vaccineCard:
                Log.e("click","vaccineCard clicked");
                previous_recycler.setVisibility(View.GONE);
                vaccineCard.setVisibility(View.VISIBLE);
                vaccineCard1.setVisibility(View.INVISIBLE);
                articleCard.setVisibility(View.INVISIBLE);
                articleCard1.setVisibility(View.VISIBLE);

                articleCard.setBackground(null);
                articleCard.setElevation(5f);


                vaccineImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_vaccination));
                articleImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_plus_1));
                vaccineText.setTextColor(getResources().getColor(R.color.white));
                articleText.setTextColor(getResources().getColor(R.color.dark_grey));
                vaccineCard.setBackground((getContext().getResources().getDrawable(R.drawable.gradient_one)));

                articleCard.setCardBackgroundColor(getResources().getColor(R.color.white));
                vaccineCard.setTag("true");
                articleCard.setTag("false");
                previous_blue.setVisibility(View.GONE);
                upcoming_text.setVisibility(View.GONE);
                upcoming_blue.setVisibility(View.VISIBLE);
                previous_text.setVisibility(View.VISIBLE);
                vaccination_list.setVisibility(View.VISIBLE);
                articleRecyclerView.setVisibility(View.GONE);
                article_text.setVisibility(View.GONE);
                select_family.setVisibility(View.GONE);

                previous_text.setVisibility(View.VISIBLE);
                vaccineRecyclerView.setVisibility(View.VISIBLE);
                vaccineRecyclerView.setItemViewCacheSize(150);
                networkingCalls.getVaccineList(vaccineRecyclerView, noVaccinationFound);
                break;
            case R.id.articleCard:
                Log.e("click","articleCard clicked");
                articleCard.setBackground((getContext().getResources().getDrawable(R.drawable.gradient_one)));

                vaccineCard.setBackground(null);
                vaccineCard.setElevation(5f);

                articleCard.setVisibility(View.VISIBLE);
                articleCard1.setVisibility(View.INVISIBLE);

                vaccineCard.setVisibility(View.INVISIBLE);
                vaccineCard1.setVisibility(View.VISIBLE);


                vaccineImage.setImageDrawable(getResources().getDrawable(R.drawable.vaccine_blue));
                vaccineText.setTextColor(getResources().getColor(R.color.dark_grey));

                articleText.setTextColor(getResources().getColor(R.color.white));
                vaccineCard.setCardBackgroundColor(getResources().getColor(R.color.white));

                vaccineCard.setTag("false");
                articleCard.setTag("true");
                articleImage.setImageDrawable(getResources().getDrawable(R.drawable.clinic_white));

                articleRecyclerView.setVisibility(View.VISIBLE);
                previous_text.setVisibility(View.GONE);
                previous_blue.setVisibility(View.GONE);
                upcoming_blue.setVisibility(View.GONE);
                upcoming_text.setVisibility(View.GONE);
                vaccination_list.setVisibility(View.GONE);
                articleRecyclerView.setItemViewCacheSize(150);
                article_text.setVisibility(View.VISIBLE);
                select_family.setVisibility(View.VISIBLE);
                vaccineRecyclerView.setVisibility(View.INVISIBLE);
                previous_recycler.setVisibility(View.INVISIBLE);
                networkingCalls.getConsultationdetails(articleRecyclerView,noVaccinationFound);
                break;
        }
    }
}