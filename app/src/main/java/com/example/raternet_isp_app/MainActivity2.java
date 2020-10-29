package com.example.raternet_isp_app;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.raternet_isp_app.auth_preferences.SaveSharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {

    private Button button;
    private TextView txtHello;
    private User currentUser;
    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.btnLogout);
        txtHello=findViewById(R.id.txtHello);
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

    }
}