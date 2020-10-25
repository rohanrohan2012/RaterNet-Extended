package com.example.raternet_isp_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    private Button button;
    private TextView txtHello;

    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = findViewById(R.id.btnLogout);
        txtHello=findViewById(R.id.txtHello);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String userID=firebaseUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserName = (String) dataSnapshot.child("userName").getValue();
                String phNo = (String) dataSnapshot.child("phoneNumber").getValue();
                String email = (String) dataSnapshot.child("emailId").getValue();

                String[] names=UserName.split(" ");

                txtHello.setText("Hello " + names[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity2.this,MainActivity.class));
            }
        });

    }
}