package com.example.raternet_isp_app;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raternet_isp_app.auth_preferences.SaveSharedPreferences;
import com.example.raternet_isp_app.endpoints.GetDataService;
import com.example.raternet_isp_app.models.User;
import com.example.raternet_isp_app.network.RetrofitClientInstance;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    private Button button;
    private TextView txtHello,ISPView,LocView;
    private User currentUser;
    public FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String ip = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.btnLogout);
        txtHello=findViewById(R.id.txtHello);
        ISPView = findViewById(R.id.ISP);
        currentUser = SaveSharedPreferences.getUser(MainActivity2.this);

        String[] names=currentUser.getUserName().split(" ");

        txtHello.setText("Welcome " + names[0]);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                SaveSharedPreferences.clearUser(MainActivity2.this);
                startActivity(new Intent(MainActivity2.this,MainActivity.class));
            }
        });

        fetchIPInfo();
    }
    public void fetchIPInfo(){
        progressDialog = new ProgressDialog(MainActivity2.this);
        progressDialog.setMessage("Getting your Network Info..."); // show progess dialog till server responds
        progressDialog.show();
        GetDataService serviceIP= RetrofitClientInstance.getRetrofitInstance(this,0)
                .create(GetDataService.class);
        Call<String> call = serviceIP.getIP();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                try {
                    ip = response.body();
                    ISPView.setText(ip);
                } catch (Exception e) {
                    e.printStackTrace();
                    ISPView.setText("Json Error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Error",t.getMessage());
            }
        });
    }
}
//Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
/*
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
* */