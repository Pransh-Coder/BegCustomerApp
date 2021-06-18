package com.example.begcustomerapp.homeStructure.ui.stats;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.customer.drbegcustomer.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class StatsFragment extends Fragment {

    private EditText input_height, input_weight,input_age;
    ConstraintLayout constraintLayout;
    private TextView bmi_value, condition, text_stature,difference,category,verySeverelyWeight;
    private Button calculate;
    private int get_height, get_weight;
    private CustomGauge gauge;
    private ImageView health;
    private RelativeLayout relativeLayout, layout_bmi, layout_height, layout_weight, layout_circumference;
    private FloatingActionButton fab_height, fab_weight, fab_circumference, fab_bmi;
    private FloatingActionsMenu fab_menu;

    CardView height_clayout, weight_clayout, bmi_clayout, head_clayout;
    ConstraintLayout option_layout;

    static WebView webView_head, webview_height, webView_weight;

    int age = 0;
    int url_condition = 0;
    double differenceVal;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait!!!");
        input_height = view.findViewById(R.id.input_height);
        input_weight = view.findViewById(R.id.input_weight);
        difference = view.findViewById(R.id.difference);
        input_age = view.findViewById(R.id.input_age);
        verySeverelyWeight = view.findViewById(R.id.verySeverelyWeight);
        constraintLayout = view.findViewById(R.id.constraint);
        text_stature = view.findViewById(R.id.textview);
        text_stature.setVisibility(View.INVISIBLE);
        bmi_value = view.findViewById(R.id.bmi_value);
        category = view.findViewById(R.id.category);
        calculate = view.findViewById(R.id.calculate_bmi);
        gauge = view.findViewById(R.id.gauge);
        health = view.findViewById(R.id.health_image);

        relativeLayout = view.findViewById(R.id.layout_relative);

        fab_menu = view.findViewById(R.id.fab_menu);

        //Options_layout
        height_clayout = view.findViewById(R.id.height_layout);
        weight_clayout = view.findViewById(R.id.weight_layout);
        bmi_clayout = view.findViewById(R.id.bmi_layout);
        head_clayout = view.findViewById(R.id.head_layout);
        option_layout = view.findViewById(R.id.option_layout);

        //fab buttons
        fab_bmi = view.findViewById(R.id.fab_bmi);
        fab_height = view.findViewById(R.id.fab_height);
        fab_weight = view.findViewById(R.id.fab_weight);
        fab_circumference = view.findViewById(R.id.fab_circumference);

        //layouts
        layout_bmi = view.findViewById(R.id.frag_bmi);
        layout_height = view.findViewById(R.id.frag_height);
        layout_weight = view.findViewById(R.id.frag_weight);
        layout_circumference = view.findViewById(R.id.frag_circumference);

        webView_head = (WebView) view.findViewById(R.id.webview_head);
        WebSettings webSettings_head = webView_head.getSettings();
        webSettings_head.setJavaScriptEnabled(true);
        webView_head.setWebViewClient(new WebViewClient());

        webview_height = (WebView) view.findViewById(R.id.webview_height);
        WebSettings webSettings_height = webview_height.getSettings();
        webSettings_height.setJavaScriptEnabled(true);
        webview_height.setWebViewClient(new WebViewClient());

        webView_weight = (WebView) view.findViewById(R.id.webview_weight);
        WebSettings webSettings_weight = webView_weight.getSettings();
        webSettings_weight.setJavaScriptEnabled(true);
        webView_weight.setWebViewClient(new WebViewClient());

        verySeverelyWeight.setText("<16");

        bmi_clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option_layout.setVisibility(View.GONE);
                layout_height.setVisibility(View.GONE);
                layout_weight.setVisibility(View.GONE);
                layout_circumference.setVisibility(View.GONE);
                layout_bmi.setVisibility(View.VISIBLE);
                fab_menu.setVisibility(View.VISIBLE);

            }
        });

        height_clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = 1;
                option_layout.setVisibility(View.GONE);
                layout_height.setVisibility(View.VISIBLE);
                layout_weight.setVisibility(View.GONE);
                layout_circumference.setVisibility(View.GONE);
                layout_bmi.setVisibility(View.GONE);
                fab_menu.setVisibility(View.VISIBLE);
                selectageGroup();


            }
        });

        weight_clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = 2;
                option_layout.setVisibility(View.GONE);
                layout_height.setVisibility(View.GONE);
                layout_weight.setVisibility(View.VISIBLE);
                layout_circumference.setVisibility(View.GONE);
                layout_bmi.setVisibility(View.GONE);
                fab_menu.setVisibility(View.VISIBLE);
                selectageGroup();


            }
        });

        head_clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                new LoadData_web_head().execute();
                option_layout.setVisibility(View.GONE);
                layout_height.setVisibility(View.GONE);
                layout_weight.setVisibility(View.GONE);
                layout_circumference.setVisibility(View.VISIBLE);
                layout_bmi.setVisibility(View.GONE);
                fab_menu.setVisibility(View.VISIBLE);

            }
        });


        fab_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_height.setVisibility(View.GONE);
                layout_weight.setVisibility(View.GONE);
                layout_circumference.setVisibility(View.GONE);
                layout_bmi.setVisibility(View.VISIBLE);
                fab_menu.collapse();
            }
        });

        fab_circumference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                new LoadData_web_head().execute();
                layout_height.setVisibility(View.GONE);
                layout_weight.setVisibility(View.GONE);
                layout_bmi.setVisibility(View.GONE);
                layout_circumference.setVisibility(View.VISIBLE);
                fab_menu.collapse();

            }
        });

        fab_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = 2;
                layout_height.setVisibility(View.GONE);
                layout_bmi.setVisibility(View.GONE);
                layout_circumference.setVisibility(View.GONE);
                layout_weight.setVisibility(View.VISIBLE);
                fab_menu.collapse();
                selectageGroup();



            }
        });

        fab_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = 1;
                layout_bmi.setVisibility(View.GONE);
                layout_weight.setVisibility(View.GONE);
                layout_circumference.setVisibility(View.GONE);
                layout_height.setVisibility(View.VISIBLE);
                fab_menu.collapse();
                selectageGroup();
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_age.length()==0){
                    input_age.setError("Enter Age");
                    return;
                }
                if (input_weight.length() == 0) {
                    input_weight.setError("Enter weight");
                    return;
                }
                if (input_height.length() == 0) {
                    input_height.setError("Enter height");
                    return;
                }
                relativeLayout.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);

                get_height = 0;
                get_height = Integer.parseInt(input_height.getText().toString());
                get_weight = Integer.parseInt(input_weight.getText().toString());
                double height_double = (double) (get_height) / 100;
                double weight_double = (double) get_weight;
                double add = weight_double / (height_double * height_double);
                bmi_value.setVisibility(View.VISIBLE);
                bmi_value.setText(String.format("%.1f", add));
                if (add < 18) {
                    if (add < 16) {
                        gauge.setValue(16);
                    } else {
                        gauge.setValue((int) add);
                    }
                } else if (add > 25) {
                    if (add > 40) {
                        gauge.setValue(40);
                    } else {
                        gauge.setValue((int) add);
                    }
                } else {
                    gauge.setValue((int) add);
                }

                if (add<18.5){
                    differenceVal = 18.5-add;
                    difference.setText("-"+String.format("%.1f",differenceVal)+" lb");
                    category.setText("Underweight");
                }
                else if (add>24.9){
                    differenceVal = add-24.9;
                    difference.setText(String.format("%.1f",differenceVal)+" lb");
                    category.setText("Overweight");
                }
                else {
                    difference.setText(getString(R.string.tick));
                    category.setText("Normal");
                }
            }
        });
    }

    private void selectageGroup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setCancelable(false);
        if (age == 1){
            alert.setMessage("Please Select Age Group")
                    .setPositiveButton("2-20 Years", new DialogInterface.OnClickListener()                 {

                        public void onClick(DialogInterface dialog, int which) {
                            url_condition = 2;
                            if (age == 1){
                                progressDialog.show();
                                text_stature.setVisibility(View.VISIBLE);
                                new LoadData_web_height().execute();

                            } else if( age == 2){
                                progressDialog.show();
                                new LoadData_web_weight().execute();
                            }
                        }
                    }).setNegativeButton("0-3 Years", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    url_condition = 1;
                    if (age == 1){
                        progressDialog.show();
                        text_stature.setVisibility(View.INVISIBLE);
                        new LoadData_web_height().execute();
                    } else if( age == 2){
                        progressDialog.show();
                        new LoadData_web_weight().execute();
                    }
                }
            });

        }else {
            alert.setMessage("Please Select Age Group")
                    .setPositiveButton("2-20 Years", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            url_condition = 2;
                            if (age == 1) {
                                progressDialog.show();
                                new LoadData_web_height().execute();
                            } else if (age == 2) {
                                progressDialog.show();
                                new LoadData_web_weight().execute();
                            }
                        }
                    }).setNegativeButton("0-2 Years", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    url_condition = 1;
                    if (age == 1) {
                        progressDialog.show();
                        new LoadData_web_height().execute();
                    } else if (age == 2) {
                        progressDialog.show();
                        new LoadData_web_weight().execute();
                    }
                }
            });
        }

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private class  LoadData_web_head extends AsyncTask<Void,Void,Void>
    {
        String html=new String();
        Document doc = null;
        @Override
        protected Void doInBackground(Void... params) {

            try {

                doc = Jsoup.connect("https://www.infantchart.com/infantheadage.php").get();
                doc.getElementsByClass("ajcenter").remove();
                doc.getElementsByClass("ajtext").remove();
                doc.getElementsByClass("fb-root").remove();
                doc.getElementsByClass("x-tip x-layer x-tip-default x-border-box").remove();
                doc.select("h1").remove();
                doc.select("h2").remove();
                doc.select("h3").remove();
                doc.select("ol").remove();
                doc.select("hr").remove();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            webView_head.loadDataWithBaseURL(null,doc.html(),
                    "text/html", "utf-8", null);
            progressDialog.dismiss();

        }
    }

    private class  LoadData_web_weight extends AsyncTask<Void,Void,Void>
    {
        String html=new String();
        Document doc = null;
        @Override
        protected Void doInBackground(Void... params) {

            try {

                if(url_condition == 1){
                    doc = Jsoup.connect("https://www.infantchart.com/").get();
                    doc.select("a").remove();
                    doc.getElementsByTag("English | ").remove();

                } else if(url_condition == 2){
                    doc = Jsoup.connect("https://www.infantchart.com/child/").get();

                }
                doc.getElementsByClass("ajcenter").remove();
                doc.getElementsByClass("ajtext").remove();
                doc.getElementsByClass("fb-root").remove();
                doc.getElementsByClass("x-tip x-layer x-tip-default x-border-box").remove();
                doc.select("h1").remove();
                doc.select("h2").remove();
                doc.select("h3").remove();
                doc.select("ol").remove();
                doc.select("hr").remove();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            webView_weight.loadDataWithBaseURL(null,doc.html(),
                    "text/html", "utf-8", null);
            progressDialog.dismiss();

        }
    }

    private class  LoadData_web_height extends AsyncTask<Void,Void,Void>
    {
        String html=new String();
        Document doc = null;
        @Override
        protected Void doInBackground(Void... params) {

            try {
                if(url_condition == 1){
                    doc = Jsoup.connect("https://www.infantchart.com/cdc0to3lengthforage.php").get();

                } else if(url_condition == 2){
                    doc = Jsoup.connect("https://www.infantchart.com/child/childrenstatureage.php").get();

                }
                doc.getElementsByClass("ajcenter").remove();
                doc.getElementsByClass("ajtext").remove();
                doc.getElementsByClass("fb-root").remove();
                doc.getElementsByClass("x-tip x-layer x-tip-default x-border-box").remove();
                doc.select("h1").remove();
                doc.select("h2").remove();
                doc.select("h3").remove();
                doc.select("ol").remove();
                doc.select("hr").remove();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            webview_height.loadDataWithBaseURL(null,doc.html(),
                    "text/html", "utf-8", null);
            progressDialog.dismiss();
        }
    }
}