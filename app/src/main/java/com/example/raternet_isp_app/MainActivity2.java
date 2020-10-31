package com.example.raternet_isp_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raternet_isp_app.auth_preferences.SaveSharedPreferences;
import com.example.raternet_isp_app.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.example.raternet_isp_app.endpoints.GetDataService;
import com.example.raternet_isp_app.network.RetrofitClientInstance;
import com.example.raternet_isp_app.network.RetrofitClientInstance2;
import com.google.gson.JsonObject;


import java.io.IOException;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogout;
    private Button btnWriteReview;
    private Button btnUpdateReview;
    private Button btnSearchNetwork;
    private Button btnFeedbackForReview;
    private FusedLocationProviderClient fusedLocationClient;

    private final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12; // passed back to you on completion to differentiate on request from other
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private TextView txtHello, ISPView, LocView;
    private User currentUser;
    public FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String ip = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogout = findViewById(R.id.btnLogout);
        btnWriteReview = findViewById(R.id.btnWriteReview);
        btnUpdateReview = findViewById(R.id.btnUpdateReview);
        btnSearchNetwork = findViewById(R.id.btnSearchNetwork);
        btnFeedbackForReview = findViewById(R.id.btnFeedbackForReview);

        btnLogout.setOnClickListener(this);
        btnWriteReview.setOnClickListener(this);
        btnUpdateReview.setOnClickListener(this);
        btnSearchNetwork.setOnClickListener(this);
        btnFeedbackForReview.setOnClickListener(this);

        txtHello = findViewById(R.id.txtHello);
        ISPView = findViewById(R.id.ISP);
        LocView = findViewById(R.id.location);
        currentUser = SaveSharedPreferences.getUser(MainActivity2.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //Setting email in Constants.
        Constants.UserEmail=currentUser.getEmailId();
        //Setting Name on Main Screen.
        String[] names = currentUser.getUserName().split(" ");
        txtHello.setText("Welcome " + names[0]);

        //Calling API to GET ISP through IP-Address
        fetchIPInfo();
        getLocationInfo();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocationInfo(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED){ // if user has already given permission
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // geocoder converts lat and long coordinates into address fields
                                Double latitude = location.getLatitude();
                                Double longitude = location.getLongitude();
                                Geocoder geocoder = new Geocoder(getApplicationContext());
                                String displayAdress = "Location";
                                try {
                                    Address address = geocoder.getFromLocation(latitude,longitude,1).get(0);
                                    displayAdress = address.getLocality() + "," +
                                            address.getSubLocality() + "," + address.getPostalCode() + "," + address.getCountryName();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                LocView.setText(displayAdress);
                                Constants.MAP_Latitude = latitude.toString();
                                Constants.MAP_Longitude = longitude.toString();
                            }
                        }
                    });
        }
        else {
            requestPermissions(LOCATION_PERMS,MY_PERMISSION_ACCESS_FINE_LOCATION); // request to allow location
            // request results are returned in onRequestPermissionsResult function
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                   getLocationInfo();
                }  else {
                    LocView.setText("Feature Unavailiable..."); // service disabled
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWriteReview:
                Toast.makeText(this, "Entering Isp-Ratings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity2.this, IspRatingsActivity.class));
                this.finish();
                break;

            case R.id.btnLogout:
                firebaseAuth.signOut();
                SaveSharedPreferences.clearUser(MainActivity2.this);
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
                this.finish();
                break;
        }
    }

    public void fetchIPInfo(){
        progressDialog = new ProgressDialog(MainActivity2.this);
        progressDialog.setMessage("Getting your Network Info..."); // show progess dialog till server responds
        progressDialog.show();
        GetDataService serviceIP= RetrofitClientInstance.getRetrofitInstance()
                .create(GetDataService.class);
        Call<String> callIP = serviceIP.getIP();
        callIP.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    ip = response.body();
                    GetDataService serviceISP= RetrofitClientInstance2.getRetrofitInstance()
                            .create(GetDataService.class);
                    Call<JsonObject> callISP = serviceISP.getIPInfo(ip);
                    callISP.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressDialog.dismiss();
                            try{
                                JsonObject jsonObject = response.body();
                                //Setting ISP in Constants
                                Constants.ISP_Name=jsonObject.get("isp").getAsString();
                                ISPView.setText(jsonObject.get("isp").getAsString());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                ISPView.setText("Json Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("ISP Error",t.getMessage());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    ISPView.setText("IP Error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("IP Error",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed ()
    {
        this.finishAffinity();
    }

}
//Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
/*
RESPONSES:
* D/OkHttp: <-- 200 OK https://api.ipify.org/ (1156ms)
D/OkHttp: Server: Cowboy
    Connection: keep-alive
    Content-Type: text/plain
    Vary: Origin
    Date: Fri, 30 Oct 2020 07:49:25 GMT
    Content-Length: 13
    Via: 1.1 vegur
D/OkHttp: 116.74.187.92
    <-- END HTTP (13-byte body)
    *
    *  I/ISP Error: CLEARTEXT communication to ip-api.com not permitted by network security policy
    *
    * D/OkHttp: <-- 200 OK http://ip-api.com/json/116.74.187.92 (538ms)
D/OkHttp: Date: Fri, 30 Oct 2020 10:56:57 GMT
    Content-Type: application/json; charset=utf-8
D/OkHttp: Content-Length: 339
    Access-Control-Allow-Origin: *
    X-Ttl: 60
    X-Rl: 44
D/OkHttp: {"status":"success","country":"India","countryCode":"IN","region":"MH",
"regionName":"Maharashtra","city":"Pune","zip":"411038","lat":18.5196,"lon":73.8554,
"timezone":"Asia/Kolkata","isp":"Hathway IP over Cable Internet Access",
"org":"Hathway Cable and Datacom Pvt Ltd","as":"AS17488 Hathway IP Over Cable Internet",
"query":"116.74.187.92"}
D/OkHttp: <-- END HTTP (339-byte body)
* */




