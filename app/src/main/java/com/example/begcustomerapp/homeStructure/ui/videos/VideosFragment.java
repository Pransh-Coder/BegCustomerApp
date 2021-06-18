package com.example.begcustomerapp.homeStructure.ui.videos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.YoutubeDataModel.YoutubeDataModel;
import com.customer.drbegcustomer.adapter.YouTubeAdapter;
import com.customer.drbegcustomer.networkingStructure.Networking;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.networkingStructure.YouTubeNetworkingInterface;


public class VideosFragment extends Fragment implements YouTubeNetworkingInterface {

    RecyclerView recyclerViewVideos;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView recyclerViewArticles;
    RecyclerView.LayoutManager layoutManager2;

    Button videos_btn,articles_btn;

    LinearLayout linearLayout1,linearLayout2;
    private static String GOOGLE_API_KEY = "AIzaSyCxUwFNbvFEwPmS7rxnEmrL9awITkJGEmo";
    private static String CHANNEL_ID = "UCOXLOl87vVBgs2Lwcs3bLlg";

    private static String CHANNEL_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&order=date&channelId="+CHANNEL_ID+"&maxResults=20&key="+GOOGLE_API_KEY;

    private YoutubeDataModel dataModel = new YoutubeDataModel();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videos, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewVideos = view.findViewById(R.id.recyclerViewVideos);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerViewVideos.setLayoutManager(layoutManager);
        recyclerViewVideos.setNestedScrollingEnabled(false);

        recyclerViewArticles = view.findViewById(R.id.recyclerViewArticles);
        layoutManager2 = new LinearLayoutManager(getContext());
        recyclerViewArticles.setLayoutManager(layoutManager2);
        recyclerViewArticles.setNestedScrollingEnabled(false);

        videos_btn = view.findViewById(R.id.article_videos_btn);
        articles_btn = view.findViewById(R.id.articles_btn);

        linearLayout1 = view.findViewById(R.id.linear_videos);

        linearLayout2 = view.findViewById(R.id.linear_article);

        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(getContext()));

        if (dataModel.getEtag() == null) {
            Networking.shared.requestInterface = this;
            Networking.shared.fetch(YoutubeDataModel.class, CHANNEL_GET_URL, getActivity());
        }

        NetworkingCalls networkingCalls = new NetworkingCalls(getActivity(),getContext());


        videos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout2.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.VISIBLE);
                recyclerViewArticles.setVisibility(View.GONE);


                recyclerViewVideos.setVisibility(View.VISIBLE);

            }
        });

        articles_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                recyclerViewVideos.setVisibility(View.GONE);
                recyclerViewArticles.setVisibility(View.VISIBLE);

                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);

                networkingCalls.getArticles(recyclerViewArticles);

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        Networking.shared.requestInterface = this;
        Networking.shared.fetch(YoutubeDataModel.class, CHANNEL_GET_URL, getActivity());
    }

    @Override
    public <T> void fetchRequestPerformed(T type) {
        dataModel = (YoutubeDataModel) type;
        recyclerViewVideos.setAdapter(new YouTubeAdapter(dataModel, getActivity()));
    }
}