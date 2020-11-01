package com.example.raternet_isp_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raternet_isp_app.auth_preferences.SaveSharedPreferences;
import com.example.raternet_isp_app.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.example.raternet_isp_app.endpoints.GetDataService;
import com.example.raternet_isp_app.network.RetrofitClientInstance;
import com.example.raternet_isp_app.network.RetrofitClientInstance2;
import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private TextView btnLogout;
    private TextView btnWriteReview;
    private TextView btnUpdateReview;
    private TextView btnSearchNetwork;
    private TextView btnFeedbackForReview;
    private TextView txtHello;
    private User currentUser;
    public FirebaseAuth firebaseAuth;

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
        txtHello = findViewById(R.id.txtHello);

        btnLogout.setOnClickListener(this);
        btnWriteReview.setOnClickListener(this);
        btnUpdateReview.setOnClickListener(this);
        btnSearchNetwork.setOnClickListener(this);
        btnFeedbackForReview.setOnClickListener(this);

        currentUser = SaveSharedPreferences.getUser(MainActivity2.this);
        //Setting email in Constants.
        Constants.UserEmail=currentUser.getEmailId();
        //Setting Name on Main Screen.
        String[] names = currentUser.getUserName().split(" ");
        txtHello.setText("Welcome " + names[0]);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWriteReview:
                Toast.makeText(this, "Entering Isp-Ratings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity2.this, IspRatingsActivity.class));
                this.finish();
                break;

            case R.id.btnUpdateReview:
                Toast.makeText(this, "Enter Update-Review", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity2.this,UpdateReviewActivity.class));
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



    @Override
    public void onBackPressed ()
    {
        this.finishAffinity();
    }

}





