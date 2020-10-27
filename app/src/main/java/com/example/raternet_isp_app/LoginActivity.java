package com.example.raternet_isp_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public EditText edtLogEmailId;
    public EditText edtLogPass;
    public Button btnLog;
    public TextView txtLogNotAUser;
    public ProgressBar progBar;
    private FirebaseAuth auth;
    private AwesomeValidation awesomeValidation;
    private FirebaseDatabase firebaseDatabase;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogEmailId=findViewById(R.id.edtLogEmailId);
        edtLogPass=findViewById(R.id.edtLogPass);
        btnLog=findViewById(R.id.btnLog);
        txtLogNotAUser=findViewById(R.id.txtLogNotAUser);

        progBar=findViewById(R.id.progBar);

        auth=FirebaseAuth.getInstance();

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.edtLogPass, RegexTemplate.NOT_EMPTY, R.string.passerror);
        awesomeValidation.addValidation(this, R.id.edtLogEmailId, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        btnLog.setOnClickListener(this);
        txtLogNotAUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnLog:
                boolean validated=awesomeValidation.validate();//checking empty
                if(validated)
                {
                    String email=edtLogEmailId.getText().toString().trim();
                    String pass=edtLogPass.getText().toString().trim();

                    progBar.setVisibility(View.VISIBLE);

                    //Check for already  existing email and wrong password
                    loginUser(email,pass);
                }
                else
                {
                    Toast.makeText(this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtLogNotAUser:
                Toast.makeText(this, "Redirecting to Registration", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }

    private void loginUser(String email, String pass)
    {
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //Add user details here
                    Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                    //fetch user object and store into shared pref, pass object to main screen
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progBar.setVisibility(View.GONE);
            }
        });
    }
}