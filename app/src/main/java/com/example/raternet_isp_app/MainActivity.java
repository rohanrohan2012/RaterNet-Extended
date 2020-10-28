package com.example.raternet_isp_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.raternet_isp_app.auth_preferences.SaveSharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public Button btnLogin;
    public Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SaveSharedPreferences.getUser(MainActivity.this)!=null){
            startActivity(new Intent(MainActivity.this,MainActivity2.class));
            this.finish();
        }

        setContentView(R.layout.activity_main);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnLogin:
                Toast.makeText(this, "Entering Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                this.finish();
                break;
            case R.id.btnRegister:
                Toast.makeText(this, "Entering Register", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                this.finish();
                break;
        }
    }
}