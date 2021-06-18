package com.example.begcustomerapp.networkingStructure;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cashfree.pg.CFPaymentService;
import com.customer.drbegcustomer.EntryActivity;
import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.adapter.RecyclerAdapterArticles;
import com.customer.drbegcustomer.adapter.RecyclerAdapterConsultation;
import com.customer.drbegcustomer.adapter.RecyclerAdapterConsultationHistory;
import com.customer.drbegcustomer.adapter.RecyclerAdapterPreviousVaccination;
import com.customer.drbegcustomer.adapter.RecyclerAdapterUpcomingAppointment;
import com.customer.drbegcustomer.adapter.RecyclerAdapterVaccine;
import com.customer.drbegcustomer.model.ArticlesPojo;
import com.customer.drbegcustomer.model.ConsultPojo;
import com.customer.drbegcustomer.model.ConsultationPojo;
import com.customer.drbegcustomer.model.PatientNamesPojo;
import com.customer.drbegcustomer.model.PreviousVaccinePojo;
import com.customer.drbegcustomer.model.TimeData;
import com.customer.drbegcustomer.model.Upcoming_Appointment_Pojo;
import com.customer.drbegcustomer.model.VaccinePojo;
import com.customer.drbegcustomer.prefrences.LoginPrefrence;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.customer.drbegcustomer.utils.Constants.AUDIO_PRICE;
import static com.customer.drbegcustomer.utils.Constants.VIDEO_PRICE;


public class NetworkingCalls {

    private LoginPrefrence loginPrefrence;
    private Context context;
    private Activity activity;
    private BegURL begURL = new BegURL();
    NetworkingInterface networkingInterface;
    RequestQueue requestQueue;
    String order;
    SimpleDateFormat date_current, time_current;
    String current_time, current_date;

    public static boolean isSignup = false;

    List<ConsultationPojo> consultationHistoryPojoList = new ArrayList<>();
    List<VaccinePojo> vaccinePojoList = new ArrayList<>();
    List<PreviousVaccinePojo> previousVaccinePojoList = new ArrayList<>();
    List<ArticlesPojo> articlesPojoList = new ArrayList<>();
    List<ConsultPojo> consultationPojoList = new ArrayList<>();
    List<String> patientNamesPojoList = new ArrayList<>();
    public static List<PatientNamesPojo> patientNamesPojos = new ArrayList<>();
    List<PatientNamesPojo> patientDetailsPojoList = new ArrayList<>();
    List<Upcoming_Appointment_Pojo> upcoming_appointment_pojoList = new ArrayList<>();

    List<String> qualificationList = new ArrayList<>();
    List<String> experienceList = new ArrayList<>();

    List<String> facilitiesList;

    //CheckIFSuccess checkIFSuccess;

    public NetworkingCalls(Context context, Activity activity, NetworkingInterface networkingInterface) {
        this.context = context;
        this.activity = activity;
        this.networkingInterface = networkingInterface;
        loginPrefrence = new LoginPrefrence(context);
        requestQueue = Volley.newRequestQueue(context);
    }


    public NetworkingCalls(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        loginPrefrence = new LoginPrefrence(context);
        requestQueue = Volley.newRequestQueue(this.context);
    }

    public void addToQueue(JsonObjectRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(30),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public void signup(String phoneNumber, String fname, String lname,String city,String email,String password) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Signing UP...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getSIGNUP(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponseSignup", "" + response);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("SUCCESS")) {

                        isSignup = true;
                        //checkIFSuccess.sucessResponseFromSignup(true);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        String first_name = jsonObject1.getString("first_name");
                        String last_name = jsonObject1.getString("last_name");
                        String email = jsonObject1.getString("email");

                        loginPrefrence.setFirstName(first_name);
                        loginPrefrence.setPhoneNumber(phoneNumber);
                        loginPrefrence.setLastName(last_name);
                        loginPrefrence.setLocation(city);
                        loginPrefrence.setEmail(email);

                        networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.signUp, true, null, null);


                    } else if (status.equalsIgnoreCase("ERROR")) {
                        networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.signUp, false, "User Already Registered!", null);
                    }

                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());

                    networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.signUp, false, e.toString(), null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResponseSignup", "" + error);
                Log.e("error", error.toString());
                String er = "";

                try {
                    er = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(er);
                    er = jsonObject.getString("message");
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

                networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.signUp, false, "Number Already Exists!!");

                Log.e("error solo", error.networkResponse + ":" + error.getMessage() + ":" + error.getLocalizedMessage() + ":" + er);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobile",  phoneNumber);
                map.put("first_name", fname);
                map.put("last_name", lname);
                map.put("location", city);
                map.put("email",email);
                map.put("password",password);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void login(String phoneNumber, String password) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Logging In...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getLOGIN(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("onResponseLogin", "" + response);

                    loginPrefrence.setLoggedIn(true);
                    loginPrefrence.setAuthToken(jsonObject.getString("token"));

                    Log.e("AUTH TOKEN", loginPrefrence.getAuthToken());

                    String first_name = "", last_name = "", mobile = "", location = "", otp = "";

                    first_name = jsonObject.getString("first_name");
                    last_name = jsonObject.getString("last_name");
                    mobile = jsonObject.getString("mobile");
                    location = jsonObject.getString("location");

                    loginPrefrence.setFirstName(first_name);
                    loginPrefrence.setPhoneNumber(mobile);
                    loginPrefrence.setLastName(last_name);
                    loginPrefrence.setLocation(location);


                    networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.login, true, null, otp);

                } catch (JSONException e) {
                    e.printStackTrace();
                    networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.login, false, e.toString(), null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
              //  Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.login, false, error, null);
                Log.e("onErrorResponseLogin", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobile", phoneNumber);
                map.put("password", password);
                map.put("fcm", EntryActivity.newToken);

                Log.e("NetWorkingCalls", ":FCM" + EntryActivity.newToken);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void bookAppointment(String patient_name, String problem, String description, String consultation_type, String date, String time) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Booking An Appointment...");
        dialog.show();
        String family = null;

        for (int i = 0; i < patientDetailsPojoList.size(); i++) {
            if (patient_name.equals(patientDetailsPojoList.get(i).getFullname())) {
                family = patientDetailsPojoList.get(i).getId();
                break;
            }
        }
        String finalFamily = family;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getPATIENT_CONSULTATION_DETAIL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("onResponseAppointment", "" + response);
                    networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.bookAppointment, true, null);

                } catch (JSONException e) {
                    e.printStackTrace();
                    networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.bookAppointment, false, e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorAppointment", "" + error + error.networkResponse.data);
                networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.bookAppointment, false, error.toString());
                Log.e("ErrResSendAndDownPdfRes", error.toString());
                Log.e("error", error.toString());
                String er = "";
                try {
                    er = new String(error.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("error solo", error.networkResponse + ":" + error.getMessage() + ":" + error.getLocalizedMessage() + ":" + er);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("family", finalFamily);
                map.put("problem", problem);
                map.put("description", description);
                map.put("consultation_type", "OPD");
                map.put("date", date);
                map.put("time", "15:00");
                Log.e("Appointment Booked", finalFamily + " " + problem + " " + consultation_type + " " + date + " " + time + " ");
                return map;
            }

        };
        requestQueue.add(stringRequest);
    }

    public void getVaccineList(RecyclerView recyclerView, TextView noVaccinationFound) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Getting Vaccine List...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, begURL.getUPCOMING_VACCINATION(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponseGetVaccine", response);
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        String vaccine_name = "", intervals = "", dosage = "", family_name = "", first_name = "", last_name = "", date = "";
                        boolean vaccinated;
                        vaccinePojoList = new ArrayList<>();        // I have initialized it here so that after each api call the data should not be appended in the arraylist as 1st time 3 data were added afain on next time when api called the list size was 6

                        if (jsonArray.length()>0){
                            noVaccinationFound.setVisibility(View.GONE);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                final VaccinePojo vaccinePojo = new VaccinePojo();

                                JSONObject jsonObject2 = jsonObject1.getJSONObject("vaccine");

                                vaccine_name = jsonObject2.getString("name");
                                dosage = jsonObject2.getString("order");

                                JSONObject jsonObject3 = jsonObject1.getJSONObject("family");

                                first_name = jsonObject3.getString("first_name");
                                last_name = jsonObject3.getString("last_name");

                                family_name = first_name + " " + last_name;

                                date = jsonObject1.getString("date");
                                vaccinated = jsonObject1.getBoolean("vaccinated");

                                vaccinePojo.setVaccine_name(vaccine_name);
                                vaccinePojo.setDosage(dosage);
                                vaccinePojo.setFamily_name(family_name);
                                vaccinePojo.setIntervals(date);
                                vaccinePojo.setVaccinated(vaccinated);
                                if (i == 0)
                                {
                                    vaccinePojo.setFirstVaccine(true);
                                }
                                vaccinePojoList.add(vaccinePojo);

                            }
                            Collections.reverse(vaccinePojoList);
                            Log.e("vaccinePojoListSize", "" + vaccinePojoList.size());
                            final RecyclerAdapterVaccine recyclerAdapterVaccine = new RecyclerAdapterVaccine(context, vaccinePojoList, activity); // had declared recyclerview here to bring it in scope
                            recyclerView.setAdapter(recyclerAdapterVaccine);
                        }
                        else {
                            noVaccinationFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("JsonExcepGetVaccines", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResGetVaccine", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("getHeaders", "" + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getPreviousVaccinations(RecyclerView recyclerView, TextView noVaccinationFound){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Getting Vaccine List...");
        dialog.show();
        recyclerView.setItemViewCacheSize(100);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, begURL.getPREVIOUS_VACCINATION(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("PREVIOUSVACCINERESPONSE", response);
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        String vaccine_name = "", intervals = "", dosage = "", family_name = "", first_name = "", last_name = "", date = "";
                        boolean vaccinated;
                        previousVaccinePojoList = new ArrayList<>();        // I have initialized it here so that after each api call the data should not be appended in the arraylist as 1st time 3 data were added afain on next time when api called the list size was 6

                        if (jsonArray.length()>0){
                            noVaccinationFound.setVisibility(View.GONE);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                final PreviousVaccinePojo previousVaccinePojo = new PreviousVaccinePojo();

                                JSONObject jsonObject2 = jsonObject1.getJSONObject("vaccine");

                                vaccine_name = jsonObject2.getString("name");
                                dosage = jsonObject2.getString("order");

                                JSONObject jsonObject3 = jsonObject1.getJSONObject("family");

                                first_name = jsonObject3.getString("first_name");
                                last_name = jsonObject3.getString("last_name");

                                family_name = first_name + " " + last_name;

                                date = jsonObject1.getString("date");
                                vaccinated = jsonObject1.getBoolean("vaccinated");

                                previousVaccinePojo.setVaccine_name(vaccine_name);
                                previousVaccinePojo.setDosage(dosage);
                                previousVaccinePojo.setFamily_name(family_name);
                                previousVaccinePojo.setIntervals(date);
                                previousVaccinePojo.setVaccinated(vaccinated);
                                if (i == 0){
                                    previousVaccinePojo.setFirstVaccine(true);
                                }

                                previousVaccinePojoList.add(previousVaccinePojo);
                            }


                            Log.e("vaccinePojoListSize", "" + previousVaccinePojoList.size());
                            final RecyclerAdapterPreviousVaccination recyclerAdapterPreviousVaccination = new RecyclerAdapterPreviousVaccination(context, previousVaccinePojoList, activity); // had declared recyclerview here to bring it in scope
                            recyclerView.setAdapter(recyclerAdapterPreviousVaccination);
                        }
                        else {
                            noVaccinationFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }


                } catch (JSONException e) {
                    Log.e("JsonExcepGetVaccines", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResGetVaccine", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("getHeaders", "" + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getArticles(RecyclerView recyclerView) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Getting Article Details...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, begURL.getARTICLE_LIST(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponseGetArticle", response);
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        //final RecyclerAdapterArticles recyclerAdapterArticles = new RecyclerAdapterArticles(context , articlesPojoList, activity); // had declared recyclerview here to bring it in scope

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        String title = "", description = "", date = "", author = "", id = "", image = "";
                        articlesPojoList = new ArrayList<>();                // I have initialized it here so that after each api call the data should not be appended in the arraylist as 1st time 3 data were added afain on next time when api called the list size was 6
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            final ArticlesPojo articlesPojo = new ArticlesPojo();

                            title = jsonObject1.getString("title");
                            description = jsonObject1.getString("description");
                            date = jsonObject1.getString("date");
                            author = jsonObject1.getString("author");
                            id = jsonObject1.getString("id");
                            image = jsonObject1.getString("image");

                            articlesPojo.setTitle(title);
                            articlesPojo.setDate(date);
                            articlesPojo.setAutho(author);
                            articlesPojo.setDescription(description);
                            articlesPojo.setId(id);
                            articlesPojo.setImage(begURL.getBASE_URL() + image);

                            articlesPojoList.add(articlesPojo);
                            //recyclerAdapterArticles.notifyDataSetChanged();

                        }
                        Log.e("values", "" + title + " " + description + " " + date + " " + author + " " + id + " " + image);

                        final RecyclerAdapterArticles recyclerAdapterArticles = new RecyclerAdapterArticles(context, articlesPojoList, activity); // had declared recyclerview here to bring it in scope
                        recyclerView.setAdapter(recyclerAdapterArticles);
                    }

                } catch (JSONException e) {
                    Log.e("JsonExcepGetArticles", "" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResGetArticle", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getConsultationdetails(RecyclerView recyclerView, TextView noVaccinationFound) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Getting Consultation History...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, begURL.getPATIENT_FAMILY(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponseGetConsultHis", response);
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        String first_name = "", last_name = "", sex = "", id = "",dob="";
                        String age = "0";
                        consultationPojoList = new ArrayList<>();                // I have initialized it here so that after each api call the data should not be appended in the arraylist as 1st time 3 data were added afain on next time when api called the list size was 6

                        if (jsonArray.length()>0){

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                final ConsultPojo consultPojo = new ConsultPojo();

                                first_name = jsonObject1.getString("first_name");
                                //  description = jsonObject1.getString("description");
                                last_name = jsonObject1.getString("last_name");
                                age = jsonObject1.getString("age");
                                sex = jsonObject1.getString("sex");
                                id = jsonObject1.getString("id");           //family_id
                                dob = jsonObject1.getString("dob");

                                consultPojo.setFullname(first_name + " " + last_name);
                                consultPojo.setGender(sex);
                                consultPojo.setAge(age);
                                consultPojo.setId(id);
                                consultPojo.setDob(dob);

                                consultationPojoList.add(consultPojo);

                            }
                            Log.e("values", "" + first_name + " " + age + " " + sex + " id--> " + id+" dob "+dob);
                            noVaccinationFound.setVisibility(View.GONE);
                            final RecyclerAdapterConsultation recyclerAdapterConsultation = new RecyclerAdapterConsultation(context, consultationPojoList, activity); // had declared recyclerview here to bring it in scope
                            recyclerView.setAdapter(recyclerAdapterConsultation);
                    }else {
                        noVaccinationFound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }


                } catch (JSONException e) {
                    Log.e("JsonExcepGetConsultHis", "" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResGetConsultHis", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getPatientFamilyDetails(Spinner patient_name_spinner) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Getting Patient Names...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, begURL.getPATIENT_FAMILY(), new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.e("onResponseGetPatientNames", response);
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        String first_name = "", last_name = "", id = "";
                        String age = "?";
                        patientNamesPojoList = new ArrayList<>();                // I have initialized it here so that after each api call the data should not be appended in the arraylist as 1st time 3 data were added afain on next time when api called the list size was 6
                        patientDetailsPojoList = new ArrayList<>();
                        PatientNamesPojo patientNamesPojo1 = new PatientNamesPojo();
                        patientNamesPojoList.add("Select Patient Name");
                        patientNamesPojo1.setId("0");
                        patientNamesPojo1.setFullname("Select Patient Name");
                        patientNamesPojo1.setAge("0");
                        patientNamesPojos.add(patientNamesPojo1);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            final PatientNamesPojo patientNamesPojo = new PatientNamesPojo();
                            first_name = jsonObject1.getString("first_name");
                            last_name = jsonObject1.getString("last_name");
                            age = jsonObject1.getString("age");
                            id = jsonObject1.getString("id");
                            patientNamesPojo1 = new PatientNamesPojo();
                            patientNamesPojo1.setId(id);
                            patientNamesPojo1.setFullname(first_name + " " + last_name);
                            patientNamesPojo1.setAge(age);
                            patientNamesPojo.setFullname(first_name + " " + last_name);
                            patientNamesPojo.setAge(age);
                            patientNamesPojo.setId(id);
                            patientNamesPojos.add(patientNamesPojo1);
                            patientNamesPojoList.add(patientNamesPojo.getFullname());
                            patientDetailsPojoList.add(patientNamesPojo);

                        }
                        Log.e("valuesAtTimeOfBooking", "" + first_name + " " + last_name + " " + age + " family_id " + id + " ");
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, patientNamesPojoList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        patient_name_spinner.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    Log.e("JsonExcepGetConsultHis", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResGetConsultHis", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("Authorization", "token " + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getPatientAge(String patient_name, TextInputEditText app_age) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait!!");
        dialog.setTitle("Getting Patient Age...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, begURL.getPATIENT_FAMILY(), new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.e("onResponseGetPatientAge", response);
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        String first_name = "", last_name = "", id = "";
                        String age = "0";             // I have initialized it here so that after each api call the data should not be appended in the arraylist as 1st time 3 data were added afain on next time when api called the list size was 6
                        patientDetailsPojoList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            final PatientNamesPojo patientNamesPojo = new PatientNamesPojo();
                            first_name = jsonObject1.getString("first_name");
                            last_name = jsonObject1.getString("last_name");
                            age = jsonObject1.getString("age");
                            id = jsonObject1.getString("id");
                            patientNamesPojo.setFullname(first_name + " " + last_name);
                            patientNamesPojo.setAge(age);
                            patientNamesPojo.setId(id);
                            patientDetailsPojoList.add(patientNamesPojo);
                            for (int j = 0; j < patientDetailsPojoList.size(); j++) {
                                if (patient_name.equals(patientDetailsPojoList.get(j).getFullname())) {
                                    app_age.setText(String.valueOf(patientDetailsPojoList.get(j).getAge()));
                                    Log.e("Patient Age", " " + age);
                                    break;
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    Log.e("JsonExcepGetConsultHis", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResGetConsultHis", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("Authorization", "token " + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void getDoctorDetails(TextView field1, TextView institueExp, TextView doctor_name) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, new BegURL().getDOCTOR_DETAILS(), new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.e("onResGetDoctorDetails", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("user");
                        String f_name_doc = jsonObject2.getString("first_name");
                        String l_name_doc = jsonObject2.getString("last_name");

                        Log.e("fullName", "" + f_name_doc + l_name_doc);

                        doctor_name.setText("Dr. " + f_name_doc+" "+ l_name_doc);
                        JSONArray jsonArray = jsonObject1.getJSONArray("qualification");

                        qualificationList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                            String qualification = jsonObject3.getString("qualification");

                            qualificationList.add(qualification);

                            Log.e("qualification", "" + qualification);
                        }
                        Log.e("qualificationListSize", "" + qualificationList.size());


                        for (int j = 0; j < qualificationList.size(); j++) {

                            String appendedText = qualificationList.get(j) + "\n";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                field1.append(Html.fromHtml(appendedText, Html.FROM_HTML_MODE_COMPACT));
                            }
                            else {
                                field1.append(Html.fromHtml(appendedText, Html.FROM_HTML_MODE_COMPACT));
                            }
                            //field1.setText(qualificationList.get(j));
                        }

                        JSONArray jsonArray2 = jsonObject1.getJSONArray("experience");

                        experienceList = new ArrayList<>();
                        for (int i = 0; i < jsonArray2.length(); i++) {

                            JSONObject jsonObject3 = jsonArray2.getJSONObject(i);

                            String experience = jsonObject3.getString("experience");

                            experienceList.add(experience);

                            Log.e("experience", "" + experience);
                        }
                        Log.e("experienceListSize", "" + experienceList.size());

                        for (int k = 0; k < experienceList.size(); k++) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                institueExp.append(Html.fromHtml(experienceList.get(k), Html.FROM_HTML_MODE_COMPACT) + "\n");
                            }
                            else {
                                institueExp.append(Html.fromHtml(experienceList.get(k), Html.FROM_HTML_MODE_COMPACT));
                            }
                        }
                    }

                } catch (JSONException e) {
                    Log.e("jsonException", "" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorgetDoctorDetails", "");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("authtoken", "" + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getHospitalDetails(TextView clinic_field, WebView clinic_institueFacilities) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, new BegURL().getCLINIC_DETAILS(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResGetHospitalDetails", "" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        JSONArray jsonArray = jsonObject1.getJSONArray("facilities");

                        facilitiesList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            String facility = jsonObject2.getString("facility");

                            Log.e("facility", "" + facility);

                            facilitiesList.add(facility);

                        }
                        Log.e("facilitiesListSize", "" + facilitiesList.size());


                        for (int j = 0; j < facilitiesList.size(); j++) {

                            //clinic_institueFacilities.append(facilitiesList.get(j) + "\n");
                            clinic_institueFacilities.loadData(facilitiesList.get(j), "text/html", null);

                        }


                    }
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrgetHospitalDetails", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("authtoken", "" + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void getCorrectTime(TimePicker timePicker , TextView timeNote , boolean bookedOnSameDay, String day) {
        Log.e("day", day);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, new BegURL().getCORRECT_TIME(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("CORRECT TIME DETAILS", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    long timeOnCalendar = ( timePicker.getCurrentHour() * 3600000 ) +  ( timePicker.getCurrentMinute() * 60000 ) ;
                    if (status.equalsIgnoreCase("success")) {

                        String data = jsonObject.getString("data");

                        JSONArray jsonArray =  new JSONArray(data);
                        Map<String, TimeData> timeDataHashMap = new HashMap<>();

                        for (int i = 0; i <jsonArray.length() ; i++) {

                            JSONObject morningTimings = jsonArray.getJSONObject(i);

                            String startMorning = morningTimings.getString("time_from");

                            String endMorning = morningTimings.getString("time_to");

                            String day = morningTimings.getString("day");



                            JSONObject eveningTimings = jsonArray.getJSONObject(i);

                            String startEvening = eveningTimings.getString("evening_time_from");

                            String endEvening = eveningTimings.getString("evening_time_to");        //time_to


                            String StartMorningHour = startMorning.substring(0,2);
                            String StartMorningMinutes = startMorning.substring(3,5);

                            long startMHours = Long.parseLong(StartMorningHour);
                            long startMMinutes = Long.parseLong(StartMorningMinutes);

                            long startMHmilliseconds = (startMHours * 3600000);
                            long startMMmilliseonds = (startMMinutes * 60 * 1000);

                            long netMorningStartTime = startMHmilliseconds + startMMmilliseonds;

                            String endMorningHour = endMorning.substring(0,2);
                            String endMorningMinutes = endMorning.substring(3,5);

                            long endMHours = Long.parseLong(endMorningHour);
                            long endMMinutes = Long.parseLong(endMorningMinutes);

                            long endMHmilliseconds = (endMHours * 3600000);
                            long endMMmilliseonds = ( endMMinutes * 60 * 1000);

                            long netMorningEndTime = endMHmilliseconds + endMMmilliseonds ;

                            String startEveningHour = startEvening.substring(0,2);
                            String startEveningMinutes = startEvening.substring(3,5);


                            long startEHours = Long.parseLong(startEveningHour);
                            long startEMinutes = Long.parseLong(startEveningMinutes);

                            long startEHmilliseconds = (startEHours * 3600000);
                            long startEMmilliseonds = (startEMinutes * 60 * 1000);

                            long netEveningStartTime = startEHmilliseconds + startEMmilliseonds ;

                            String endEveningHour = endEvening.substring(0,2);
                            String endEveningMinutes = endEvening.substring(3,5);

                            long endEHours = Long.parseLong(endEveningHour);
                            long endEMinutes = Long.parseLong(endEveningMinutes);

                            long endEHmilliseconds = (endEHours * 3600000);
                            long endEMmilliseonds = ( endEMinutes * 60 * 1000);

                            long netEveningEndTime = endEHmilliseconds + endEMmilliseonds ;



//                        Toast.makeText(context, "start :"+netMorningStartTime + "end :"+netMorningEndTime, Toast.LENGTH_SHORT).show();
                            TimeData timeData = new TimeData();
                            timeData.setEndEveningHour(endEveningHour);
                            timeData.setEndEveningMin(endEveningMinutes);
                            timeData.setEndMorningHour(endMorningHour);
                            timeData.setEndMorningMin(endMorningMinutes);
                            timeData.setStartEveningHour(startEveningHour);
                            timeData.setStartEveningMin(startEveningMinutes);
                            timeData.setStartMorningHour(StartMorningHour);
                            timeData.setStartMorningMin(StartMorningMinutes);
                            timeData.setStartMorning(startMorning);
                            timeData.setEndMorning(endMorning);
                            timeData.setStartEvening(startEvening);
                            timeData.setEndEvening(endEvening);
                            timeData.setNetMorningStartTime(netMorningStartTime);
                            timeData.setNetMorningEndTime(netMorningEndTime);
                            timeData.setNetEveningStartTime(netEveningStartTime);
                            timeData.setNetEveningEndTime(netEveningEndTime);

                            timeDataHashMap.put(day, timeData);
                        }
                        TimeData timeData = timeDataHashMap.get(day);
                        Long netMorningStartTime = timeData.getNetMorningStartTime();
                        Long netMorningEndTime = timeData.getNetMorningEndTime();
                        Long netEveningStartTime = timeData.getNetEveningStartTime();
                        Long netEveningEndTime = timeData.getNetEveningEndTime();
                        String startMorning = timeData.getStartMorning();
                        String endMorning = timeData.getEndMorning();
                        String startEvening = timeData.getStartEvening();
                        String endEvening = timeData.getEndEvening();
                        Log.e("MORNING START TIME :" + startMorning , "MORNING END TIME :" +endMorning);
                        Log.e("eveningTime","start Evening"+startEvening+" end Evening"+endEvening);
                        Log.e("eveningTime", ""+netMorningStartTime+":"+netMorningEndTime+":"+netEveningStartTime+":"+netEveningEndTime+":"+timeOnCalendar);
                        if(bookedOnSameDay) {
                            if (timeOnCalendar >= netMorningStartTime && timeOnCalendar < netMorningEndTime ) {
                                timeNote.setVisibility(View.GONE);
                                Log.e("Time if", timePicker.getCurrentHour() + "");
                            } else if (timeOnCalendar >= netEveningStartTime && timeOnCalendar < netEveningEndTime ) {
                                timeNote.setVisibility(View.GONE);
                            } else {
                                String text = "Please select appointment time between " + startMorning + " to " + endMorning + " or between " + startEvening + " to " + endEvening ;
                                timeNote.setText(text);
                                timeNote.setVisibility(View.VISIBLE);
                                Log.e("Time else", timePicker.getCurrentHour() + "");
                            }
                        }

                        else {

                            if (timeOnCalendar >= netMorningStartTime && timeOnCalendar < netMorningEndTime ) {
                                timeNote.setVisibility(View.GONE);
                                Log.e("Time if", timePicker.getCurrentHour()+"");
                            } else if (timeOnCalendar >= netEveningStartTime && timeOnCalendar < netEveningEndTime ) {
                                timeNote.setVisibility(View.GONE);
                            } else {
                                String text = "Please select appointment time between " + startMorning + " to " + endMorning + " or between " + startEvening + " to " + endEvening;
                                timeNote.setText(text);
                                timeNote.setVisibility(View.VISIBLE);
                                Log.e("Time else", timePicker.getCurrentHour() + "");
                            }


                        }
                    }

                }

                catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CORRECT TIME ERROR", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("authtoken", "" + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getArticleDetails(String id, ImageView article_image, TextView article_title, TextView article_details) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, new BegURL().getARTICLE_DETAIL() + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResGetArticleDetails", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {


                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        String title = "", description = "", image = "";

                        title = jsonObject1.getString("title");
                        description = jsonObject1.getString("description");
                        image = jsonObject1.getString("image");

                        //      }
                        article_title.setText(title);
                        article_details.setText(description);
                        Picasso.get().load(image).into(article_image);      //http://128.199.17.214/media/consultation/documents/1608211245558.png      image
                    }
                } catch (JSONException e) {
                    Log.e("onjsonexceptionArticle", "" + e);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onerrorArticleDetails", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("authtoken", "" + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getFamilyConsultation(String id, RecyclerView recyclerView, TextView noConsTxt, Button button) {

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please Wait!!");
        dialog.setMessage("Getting Family Consultation History");
        dialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, begURL.getPATIENT_FAMILY_CONSULTATION() + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("onRespGetFamiConsultHis", response);
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length()>0){

                            String problem = "", description = "", consultation_type = "", id = "", created_at = "";
                            consultationHistoryPojoList = new ArrayList<>();                // I have initialized it here so that after each api call the data should not be appended in the arraylist as 1st time 3 data were added afain on next time when api called the list size was 6

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                final ConsultationPojo consultationPojo = new ConsultationPojo();

                                id = jsonObject1.getString("id");
                                problem = jsonObject1.getString("problem");
                                description = jsonObject1.getString("description");
                                consultation_type = jsonObject1.getString("consultation_type");
                                created_at = jsonObject1.getString("date");           //family_id

                                consultationPojo.setProblem(problem);
                                consultationPojo.setConsultation_type(consultation_type);
                                consultationPojo.setId(id);
                                consultationPojo.setDescription(description);
                                consultationPojo.setDate(created_at);

                                consultationHistoryPojoList.add(consultationPojo);
                                Collections.sort(consultationPojoList, Collections.reverseOrder());

                            }
                            Log.e("values", "ID: " + id);

                            final RecyclerAdapterConsultationHistory recyclerAdapterConsultationHistory = new RecyclerAdapterConsultationHistory(context, consultationHistoryPojoList, activity); // had declared recyclerview here to bring it in scope
                            recyclerView.setAdapter(recyclerAdapterConsultationHistory);
                        }
                        else {
                            Log.e("inElse","jsonLength<0" );
                            noConsTxt.setVisibility(View.VISIBLE);
                            button.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    Log.e("JsonExcepGetConsultHis", "" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onErrorGetFamilyConHis", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return map;

            }
        };
        requestQueue.add(stringRequest);

    }

    public void getConsultationDetails(String id, TextView disease_name, TextView consultation_date, TextView consultation_type, TextView disease_detail, CardView report_download) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, new BegURL().getCONSULTATION_DETAIL() + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResGetConsultationD", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        String disease_name_get = "", consultation_date_get = "", consultation_type_get = "", disease_detail_get = "" ;


                        JSONObject jsonObject2 = jsonObject.getJSONObject("data");

                        disease_name_get = jsonObject2.getString("problem");
                        disease_detail_get = jsonObject2.getString("description");
                        consultation_type_get = jsonObject2.getString("consultation_type");
                        consultation_date_get = jsonObject2.getString("date");

                        JSONArray doc = jsonObject2.getJSONArray("documents");
                        if (doc.length() > 0) {
                            report_download.setVisibility(View.VISIBLE);
                            for (int i = 0; i < doc.length(); i++) {
                                JSONObject document = doc.getJSONObject(i);
                                String doclink = document.getString("document");
                                new DownloadingImage().execute(doclink);
                                Log.e("Link", "" + doclink);
                            }
                        } else {
                            report_download.setVisibility(View.GONE);
                        }

                        disease_name.setText(disease_name_get);
                        consultation_date.setText(consultation_date_get);

                        if(!disease_detail_get.equals("null")){

                            disease_detail.setVisibility(View.VISIBLE);

                            disease_detail.setText(disease_detail_get);


                        }


                        consultation_type.setText(consultation_type_get);


                    }
                } catch (JSONException e) {
                    Log.e("onjsonexcConsultation", "" + e);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onerrorConsultationD", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                Log.e("authtoken", "" + loginPrefrence.getAuthToken());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getUpcomingConsultation(TextView upcoming_appointment, RecyclerView recyclerView_appointment) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, begURL.getUPCOMING_APPOINTMENT(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("onRespGetUpApp", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        //final RecyclerAdapterArticles recyclerAdapterArticles = new RecyclerAdapterArticles(context , articlesPojoList, activity); // had declared recyclerview here to bring it in scope

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        String problem = "", time = "", consultation_type = "", id = "", created_at = "";
                        upcoming_appointment_pojoList = new ArrayList<>();                // I have initialized it here so that after each api call the data should not be appended in the arraylist as 1st time 3 data were added afain on next time when api called the list size was 6
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            Upcoming_Appointment_Pojo upcoming_appointment_pojo = new Upcoming_Appointment_Pojo();

                            id = jsonObject1.getString("id");
                            problem = jsonObject1.getString("problem");
                            time = jsonObject1.getString("time");
                            //age = jsonObject1.getInt("age");
                            consultation_type = jsonObject1.getString("consultation_type");
                            created_at = jsonObject1.getString("date");           //family_id

                            upcoming_appointment_pojo.setProblem(problem);
                            upcoming_appointment_pojo.setConsultation_type(consultation_type);
                            upcoming_appointment_pojo.setId(id);
                            upcoming_appointment_pojo.setTime(time);
                            upcoming_appointment_pojo.setDate(created_at);

                            upcoming_appointment_pojoList.add(upcoming_appointment_pojo);
                            if (upcoming_appointment_pojoList.size() < 1) {
                                upcoming_appointment.setVisibility(View.GONE);
                            } else {
                                upcoming_appointment.setVisibility(View.VISIBLE);
                            }
                        }
                        Log.e("values", "ID: " + id);
                        Log.e("values", "ID: " + time);


                        RecyclerAdapterUpcomingAppointment recyclerAdapterUpcomingAppointment = new RecyclerAdapterUpcomingAppointment(context, upcoming_appointment_pojoList, activity); // had declared recyclerview here to bring it in scope
                        recyclerView_appointment.setAdapter(recyclerAdapterUpcomingAppointment);
                    }

                } catch (JSONException e) {
                    Log.e("JsonExcepGetConsultHis", "" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onErrorGetFamilyConHis", "" + error);
                upcoming_appointment.setVisibility(View.GONE);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return map;

            }
        };
        requestQueue.add(stringRequest);
    }

    public void sendAndDownloadPdf(String id, String email) {

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please Wait!!");
        dialog.setMessage("Sending & Downloading");
        dialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getSEND_AND_DOWNLOAD(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(context, "Pdf sent successfully", Toast.LENGTH_SHORT).show();
                Log.e("sendAndDownloadPdfRes", "" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrResSendAndDownPdfRes", error.toString());
                Log.e("error", error.toString());
                String er = "";
                try {
                    er = new String(error.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("error solo", error.networkResponse + ":" + error.getMessage() + ":" + error.getLocalizedMessage() + ":" + er);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("family", id);
                map.put("email", email);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void addPatient(View view , String name, String dob, String sex, String lastname, String mothers_name) {

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please Wait!!");
        dialog.setMessage("Ading Patient..");
        dialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getADD_FAMILY_MEMB(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("addingPatientRes", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

//                    String id = jsonObject.getString("id");

                    if (status.equalsIgnoreCase("SUCCESS")) {

                        String id = jsonObject.getString("id");

                        networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.addPatient, true, null);

                        Bundle bundle = new Bundle();
                        bundle.putString("name",name+" "+ lastname);
                        bundle.putString("dob",dob);
                        bundle.putString("sex",sex);
                        bundle.putString("id" , id);

                        Navigation.findNavController(view).navigate(R.id.action_addPatientFragment_to_questionnaireFragment,bundle);

                    } else {
                        Toast.makeText(context, "Error in adding!", Toast.LENGTH_SHORT).show();
                        networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.addPatient, false, "Error in adding customer");
                    }
                } catch (JSONException e) {
                    Log.e("jsonExcepaddingPatient", e.toString());
                    networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.addPatient, false, e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrResaddingPatient", error.toString());
                Log.e("error", error.toString());
                String er = "";
                try {
                    er = new String(error.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("error solo", error.networkResponse + ":" + error.getMessage() + ":" + error.getLocalizedMessage() + ":" + er);
                networkingInterface.networkingRequestPerformed(NetworkingInterface.MethodType.addPatient, false, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("first_name", name);
                map.put("last_name", lastname);
                map.put("dob", dob);
                map.put("sex", sex);
                map.put("mother_name", mothers_name);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void bookAppointment_audio_video(String problem, String description_data, String consultation_type, String current_date, String current_time, String family) {

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please Wait!!");
        dialog.setMessage("Adding Appointment..");
        dialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getPATIENT_CONSULTATION_DETAIL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("test", problem + " des "+description_data + consultation_type + current_date + current_time + "fam "+family + "");
                Log.e("get", response + "");

                dialog.dismiss();
                Log.e("addingAppointRes", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("SUCCESS")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String id= jsonObject1.getString("id");

                        Toast.makeText(context, "Data added sucessfuly!", Toast.LENGTH_SHORT).show();
                        record_transaction(description_data, id, consultation_type);

                    } else {
                        Toast.makeText(context, "Error in adding!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("testerror", problem + description_data + consultation_type + current_date + current_time + " fam "+ family + "");

                Log.e("ErrResaddingPatient", error.toString());
                Log.e("error", error.toString());
                String er = "";
                try {
                    er = new String(error.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("error Appointment", error.networkResponse + ":" + error.getMessage() + ":" + error.getLocalizedMessage() + ":" + er);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("problem", problem + "");
                map.put("description", description_data + "");
                map.put("consultation_type", consultation_type + "");
                map.put("date", current_date + "");
                map.put("time", current_time + "");
                map.put("doctor", 3 + "");
                map.put("family", family + "");
                Random rand = new Random();
                String appoint = String.format("%04d", rand.nextInt(10000));
                map.put("appointment_no", appoint + "");

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void initiatePayment(Button pay_btn, TextView about_text){
        current_date ="";
        current_time="";
        String uniqueID = UUID.randomUUID().toString();
        order =""+uniqueID;
        SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
        current_date = sdfdate.format(new Date());
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
        current_time = sdftime.format(new Date());

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("orderId",order );
            if (about_text.getText().toString().equals("Book a video appointment")) {
            jsonBody.put("orderAmount",VIDEO_PRICE);     //1000
            } else {
                jsonBody.put("orderAmount",AUDIO_PRICE);  //700
            }
            jsonBody.put("orderCurrency","INR");
            Log.e("JsonBody", jsonBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, begURL.getPAYMENT_CASHFREE(), jsonBody, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("onResponse",response.toString());

                    if (response.getString("status").equalsIgnoreCase("OK")){
                        String token = response.getString("cftoken");
                        Log.e("cashFreeToken", ""+token);

                        Map<String, String> params = new HashMap<>();
                        params.put(PARAM_APP_ID, "89153d7ebf7f22cd1fd00950c35198");
                        params.put(PARAM_ORDER_ID, order);
                        if (about_text.getText().toString().equals("Book a video appointment")) {
                            params.put(PARAM_ORDER_AMOUNT, VIDEO_PRICE);     //1000
                        } else {
                            params.put(PARAM_ORDER_AMOUNT, AUDIO_PRICE);      //700
                        }
                        params.put(PARAM_ORDER_NOTE, "Booking Appointment");
                        params.put(PARAM_CUSTOMER_NAME,loginPrefrence.getFirstName());
                        params.put(PARAM_CUSTOMER_PHONE, loginPrefrence.getPhoneNumber());
                        params.put(PARAM_CUSTOMER_EMAIL, "mirzawaqarbeg@yahoo.com");
                        params.put(PARAM_ORDER_CURRENCY, "INR");
                        params.put("notifyUrl" ,"https://api.gocashfree.com/notify");
                        for(Map.Entry entry : params.entrySet()) {
                            Log.d("CFSKDSample", entry.getKey() + " " + entry.getValue());
                        }

                        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
                        cfPaymentService.setOrientation(0);
                        cfPaymentService.doPayment(activity, params, token, "PROD", "#F8A31A", "#FFFFFF", true);
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onError",error.toString());
                Toast.makeText(context, "Something went wrong!! try again later!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("x-client-id","89153d7ebf7f22cd1fd00950c35198");
                map.put("x-client-secret","cfed4b90188b9fe4fd81c6ced17c3d233106789b");
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public class Downloading extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... url) {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/drBeg");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
            String date = dateFormat.format(new Date());

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir("/drBeg", date + ".pdf");

            manager.enqueue(request);
            Log.e("path", mydir.getAbsolutePath() + File.separator + date + ".pdf");
            return mydir.getAbsolutePath() + File.separator + date + ".pdf";
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("DownloadImgFromServer", "Saved!!");
        }
    }

    public class DownloadingImage extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... url) {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/drBeg");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
            String date = dateFormat.format(new Date());

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES /*"/drbeg"*/, date + ".jpg");

            manager.enqueue(request);
            Log.e("path", mydir.getAbsolutePath() + File.separator + date + ".jpg");
            return mydir.getAbsolutePath() + File.separator + date + ".jpg";
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(context, "Download successful ! ", Toast.LENGTH_SHORT).show();
            Log.e("DownloadImgFromServer", "Saved!!");
        }
    }

    public void fillQuestionnaire(String fathers_weight, String mothers_weight, String childs_weight, String fathers_height, String mothers_height, String childs_height, String place_of_delivery, String type_of_delivery, String term, String history_of_Previous_hospitalization, String no_of_Siblings, String birth_order_of_child, String history_of_consanguineous_marriage, String history_of_drug_allergy, String family_history_of_chronic_illness, String chief_complaint_of_child, String id) {

           ProgressDialog dialog = new ProgressDialog(context);
           dialog.setTitle("Please Wait!!");
           dialog.setMessage("Submitting Questionnaire..");
           dialog.setCancelable(false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getFILL_QUESTIONNAIRE(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Log.e("onRespFillQuestionnaire", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();

                    map.put("family", id);
                    map.put("fathers_weight", fathers_weight);
                    map.put("mothers_weight", mothers_weight);
                    map.put("childs_weight", childs_weight);
                    map.put("fathers_height", fathers_height);
                    map.put("mothers_height",mothers_height);
                    map.put("childs_height", childs_height);
                    map.put("place_of_delivery", place_of_delivery);
                    map.put("type_of_delivery",type_of_delivery);
                    map.put("term",term);
                    map.put("history_of_Previous_hospitalization ",history_of_Previous_hospitalization);
                    map.put("no_of_Siblings",no_of_Siblings);
                    map.put("birth_order_of_child",birth_order_of_child);
                    map.put("history_of_consanguineous_marriage",history_of_consanguineous_marriage);
                    map.put("history_of_drug_allergy",history_of_drug_allergy);
                    map.put("family_history_of_chronic_illness",family_history_of_chronic_illness);
                    map.put("chief_complaint_of_child",chief_complaint_of_child);

                    return map;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("Authorization", "token " + loginPrefrence.getAuthToken());
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest);
        }

    public void record_transaction(String description_data, String id, String consultation_type) {

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please Wait!!");
        dialog.setMessage("Adding Appointment..");
        dialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getTRANSACTION_DETAIL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dialog.dismiss();
                Log.e("addingAppointRes", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(context, "Data added sucessfuly!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error in adding!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("ErrResaddingPatient", error.toString());
                Log.e("error", error.toString());
                String er = "";
                try {
                    er = new String(error.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("dataaaaaa", current_date+"           "+current_time);

                Log.e("error Appointment", error.networkResponse + ":" + error.getMessage() + ":" + error.getLocalizedMessage() + ":" + er);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String amount = "";
                if (consultation_type.equals("video")){
                    amount = VIDEO_PRICE;
                } else {
                    amount = AUDIO_PRICE;
                }
                map.put("amount", amount + "");
                map.put("vendor_amount_paid", amount + "");
                map.put("description", description_data + "");
                map.put("consultation", id + "");
                map.put("date", current_date + "");
                map.put("time", current_time + "");
                map.put("doctor", 3 + "");
                map.put("order_id", order + "");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", "token " + loginPrefrence.getAuthToken());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void forgetPassword(String email){

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please Wait!!");
        dialog.setMessage("Sending Email");
        dialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, begURL.getFORGOT_PASSWORD(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("onResForgotPassword", ""+response);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Email has been sent to your registered email-id!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("onErrorResForgotPass", ""+error + error.networkResponse.data);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Email-id not found in our database!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("Authorization","token "+loginPrefrence.getAuthToken());
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("email",email);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}