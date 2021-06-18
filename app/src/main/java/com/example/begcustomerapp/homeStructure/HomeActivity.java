package com.example.begcustomerapp.homeStructure;

import android.Manifest;
import android.app.ActivityManager;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cashfree.pg.CFPaymentService;
import com.customer.drbegcustomer.CallingService;
import com.customer.drbegcustomer.R;
import com.customer.drbegcustomer.homeStructure.ui.appointment.CallFragment;
import com.customer.drbegcustomer.networkingStructure.NetworkingCalls;
import com.customer.drbegcustomer.prefrences.LoginPrefrence;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LoginPrefrence loginPrefrence;
    private int REQUEST_CODE_BATTERY_OPTIMIZATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, CallingService.class));
        UiModeManager uiModeManager;
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Draw Over Apps Permission")
                        .setCancelable(false)
                        .setMessage("Please allow this permission for the doctor Beg's App!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, 1);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        }
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MODIFY_PHONE_STATE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.MODIFY_AUDIO_SETTINGS}, PackageManager.PERMISSION_GRANTED);
/*
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
*/

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_user,R.id.navigation_videos, R.id.navigation_info, R.id.navigation_stats)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        loginPrefrence = new LoginPrefrence(getApplicationContext());
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
//            String newToken = instanceIdResult.getToken();
//            Log.e("newToken", newToken);
////            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putString("fb", newToken).apply();
//        });

//        Log.d("newToken", getActivity().getPreferences(Context.MODE_PRIVATE).getString("fb", "empty :("));
//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult() != null) {
////                sendFCMToken(task.getResult().getToken());
//            }
//        });

        addAutoStartup();

//        checkForBatteryOpt();
    }

   /* private void sendFCMToken(String token) {
        Log.e("User id", loginPrefrence.getUserId());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USER).document(loginPrefrence.getUserId());
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(aVoid -> Toast.makeText(HomeActivity.this, "Token sent", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Unable to send token" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }*/

    private void checkForBatteryOpt(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Battery optimization is enabled. It can interrupt running background services.");
                builder.setPositiveButton("Disable", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    startActivityForResult(intent, REQUEST_CODE_BATTERY_OPTIMIZATION);
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            }
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BATTERY_OPTIMIZATION) {
            checkForBatteryOpt();
        }
    }*/

    @Override
    protected  void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
        //      Log.d("ReqCode", "ReqCode : " + CFPaymentService.REQ_CODE);
//        Log.d("API Response", "API Response : " + data.getData());
        //Prints all extras. Replace with app logic.
        if (requestCode == CFPaymentService.REQ_CODE) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null)
                    for (String key : bundle.keySet()) {
                        if (bundle.getString(key) != null) {
                            Log.d("Key", key + " : " + bundle.getString(key));
                        }
                    }
                if (bundle.getString("txStatus") != null) {
                    if (bundle.getString("txStatus").equalsIgnoreCase("SUCCESS")) {
                        new NetworkingCalls(HomeActivity.this, HomeActivity.this).bookAppointment_audio_video(CallFragment.problem, CallFragment.description_data, CallFragment.consultation_type, CallFragment.current_date, CallFragment.time, CallFragment.id);
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isMyServiceRunning(CallingService.class)){
            startService(new Intent(this, CallingService.class));
        }
        //Toast.makeText(this, "app destroyed qwer!", Toast.LENGTH_SHORT).show();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }
}