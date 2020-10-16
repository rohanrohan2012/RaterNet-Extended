package com.example.raternet_isp_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText edtRegEmailId;
    public EditText edtRegFirstName;
    public EditText edtRegLastName;
    public EditText edtRegPass;
    public EditText edtRegVerifyPass;
    public Button btnReg;
    public TextView txtRegAlreadyUser;
    public ProgressBar progBar;

    private FirebaseAuth auth;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtRegEmailId=findViewById(R.id.edtRegEmailId);
        edtRegFirstName=findViewById(R.id.edtRegFirstName);
        edtRegLastName=findViewById(R.id.edtRegLastName);
        edtRegPass=findViewById(R.id.edtRegPass);
        edtRegVerifyPass=findViewById(R.id.edtRegVerifyPass);
        btnReg=findViewById(R.id.btnReg);
        txtRegAlreadyUser=findViewById(R.id.txtRegAlreadyUser);
        progBar=findViewById(R.id.progBar);

        auth=FirebaseAuth.getInstance();

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.edtRegEmailId, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this,R.id.edtRegFirstName, RegexTemplate.NOT_EMPTY,R.string.firstnamerror);
        awesomeValidation.addValidation(this,R.id.edtRegLastName,RegexTemplate.NOT_EMPTY,R.string.lastnamerror);
        awesomeValidation.addValidation(this,R.id.edtRegPass,RegexTemplate.NOT_EMPTY,R.string.passerror);
        awesomeValidation.addValidation(this,R.id.edtRegVerifyPass,RegexTemplate.NOT_EMPTY,R.string.passerror);

        btnReg.setOnClickListener(this);
        txtRegAlreadyUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnReg:
                boolean validated=awesomeValidation.validate();
                if(validated)//Checked empty fields and email address
                {
                    boolean checker=checkPassMatch();
                    if(checker)//checked matching of passwords
                    {
                        boolean passLen=checkPassLength();
                        if(passLen)//check length of pass (must be >6)
                        {
                            progBar.setVisibility(View.VISIBLE);

                            final String email=edtRegEmailId.getText().toString().trim();
                            final String fname=edtRegFirstName.getText().toString().trim();
                            final String lname=edtRegLastName.getText().toString().trim();
                            String pass=edtRegPass.getText().toString().trim();

                            auth.createUserWithEmailAndPassword(email,pass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful())
                                            {
                                                User user=new User(email,fname,lname);

                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            progBar.setVisibility(View.GONE);

                                                            Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                                        }
                                                        else
                                                        {
                                                            progBar.setVisibility(View.GONE);
                                                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                progBar.setVisibility(View.GONE);
                                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(this, "Enter a Password more than 6 characters", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Unsuccessful Registration", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtRegAlreadyUser:
                Toast.makeText(this, "Redirecting to Login Page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
        }
    }

    private boolean checkPassLength()
    {
        String pass=edtRegPass.getText().toString().trim();

        if(pass.length()<6) {
            return false;
        }
        return true;
    }

    private boolean checkPassMatch()
    {
        String pass=edtRegPass.getText().toString().trim();
        String verifyPass=edtRegVerifyPass.getText().toString().trim();

        if(pass.equals(verifyPass))
        {
            return true;
        }
        return false;
    }
}