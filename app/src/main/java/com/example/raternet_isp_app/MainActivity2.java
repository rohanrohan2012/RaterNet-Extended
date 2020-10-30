package com.example.raternet_isp_app;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raternet_isp_app.auth_preferences.SaveSharedPreferences;
import com.example.raternet_isp_app.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogout;
    private Button btnWriteReview;
    private Button btnUpdateReview;
    private Button btnSearchNetwork;
    private Button btnFeedbackForReview;

    private TextView txtHello, ISPView, LocView;
    private User currentUser;
    public FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String ip = null;

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
        currentUser = SaveSharedPreferences.getUser(MainActivity2.this);

        String[] names = currentUser.getUserName().split(" ");

        txtHello.setText("Welcome " + names[0]);

//        fetchIPInfo();
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
}


//    public void fetchIPInfo(){
//        progressDialog = new ProgressDialog(MainActivity2.this);
//        progressDialog.setMessage("Getting your Network Info..."); // show progess dialog till server responds
//        progressDialog.show();
//        GetDataService serviceIP= RetrofitClientInstance.getRetrofitInstance(this,0)
//                .create(GetDataService.class);
//        Call<IP> call = serviceIP.getIP();
//        call.enqueue(new Callback<IP>() {
//            @Override
//            public void onResponse(Call<IP> call, Response<IP> response) {
//                progressDialog.dismiss();
//                try {
//                    IP ip = response.body();
//                    ISPView.setText(ip.getIP());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ISPView.setText("Json Error");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<IP> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(MainActivity2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.i("Error",t.getMessage());
//            }
//        });
//    }
//}
//Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $