package com.example.begcustomerapp.homeStructure.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.networkingStructure.BegURL;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.networkingStructure.VolleyMultipartRequest;
import com.customer.drbegcustomer.prefrences.LoginPrefrence;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;

public class consultation_details extends Fragment {
    private TextView disease_name, consultation_date, consultation_type, disease_detail;
    CardView report_download;
    NetworkingCalls networkingCalls;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    RequestQueue requestQueue;
    File file;
    Bitmap bitmap;
    BegURL begURL;
    String id;
    Button cancel, confirm;
    ImageView back, imageUp;
    LoginPrefrence loginPrefrence;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultation_details, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkingCalls = new NetworkingCalls(getActivity(), getContext());
        requestQueue = Volley.newRequestQueue(getContext());
        begURL = new BegURL();
        loginPrefrence = new LoginPrefrence(getContext());

        disease_name = view.findViewById(R.id.disease_name);
        consultation_date = view.findViewById(R.id.consult_date);
        consultation_type = view.findViewById(R.id.consultation_type);
        report_download = view.findViewById(R.id.report_download);
        imageUp = view.findViewById(R.id.imageUp);
        cancel = view.findViewById(R.id.cancel);
        confirm = view.findViewById(R.id.confirm);
        back = view.findViewById(R.id.back);
        report_download.setVisibility(View.GONE);
        disease_detail = view.findViewById(R.id.detail_disease);
        fab = view.findViewById(R.id.fab);
        disease_detail.setVisibility(View.GONE);


        Bundle mBundle = new Bundle();
        mBundle = getArguments();
        id =  mBundle.getString("id");  // key must be same which was given in first fragment

        Log.e("Consultation_details", ""+id);

        networkingCalls.getConsultationDetails(id, disease_name,consultation_date,consultation_type,disease_detail, report_download);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectImage(getActivity());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUp.setImageBitmap(null);
                imageUp.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPdf();
                imageUp.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
            }
        });

    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select how do you want to upload images");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void uploadPdf(){


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,begURL.getPATIENT_UPLOAD_DOCUMENTS()/*begURL.getUPLOAD_FAMILY_REPORTS()*/, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("uploadPdfRes", new String(response.data));
                Toast.makeText(getContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error: "+error,Toast.LENGTH_SHORT).show();
                Log.e("error","error: "+error);
            }
        }) {


             /** If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */

                       /* * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();

                long imagename = System.currentTimeMillis();
                params.put("document", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                long imagename = System.currentTimeMillis();
                params.put("is_family","true");
                params.put("consultation",id);
                Log.e("consultation", ""+id);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("Authorization","token "+ loginPrefrence.getAuthToken());
                return map;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(multipartRequest);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:

                        bitmap= (Bitmap) data.getExtras().get("data");
                        imageUp.setImageBitmap(bitmap);
                        imageUp.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);

                        Log.e("Camera", "yes");

                    break;
                case 1:
                    Log.e("Camera", "no");

                        Uri selectedImage = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                            imageUp.setImageBitmap(bitmap);
                            imageUp.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            confirm.setVisibility(View.VISIBLE);
                            //sharedPreferences1();
                        } catch (FileNotFoundException e) {
                            //  Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            //  Auto-generated catch block
                            e.printStackTrace();
                        }
                    break;
            }
        }
    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}