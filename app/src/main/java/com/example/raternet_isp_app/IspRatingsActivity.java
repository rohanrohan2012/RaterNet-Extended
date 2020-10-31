package com.example.raternet_isp_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.raternet_isp_app.models.ReviewDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletionService;


public class IspRatingsActivity extends AppCompatActivity {

    public FirebaseAuth auth;

    public RadioGroup RdoGrpType;
    public RadioButton RdoBtnType;

    public Button btnSubmitReview;

    public EditText txtFeedback;

    public RatingBar RbPrice;
    public RatingBar RbService;
    public RatingBar RbSpeed;
    public RatingBar RbOverall;

    public boolean typeFlag=false;
    public boolean priceRatingFlag=false;
    public boolean serviceRatingFlag=false;
    public boolean speedRatingFlag=false;
    public boolean overallRatingFlag=false;

    private ProgressDialog progressDialog;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isp_ratings);

        btnSubmitReview=findViewById(R.id.btnSubmitReview);

        txtFeedback=findViewById(R.id.txtFeedback);

        auth= FirebaseAuth.getInstance();

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.txtFeedback, RegexTemplate.NOT_EMPTY,R.string.feedback_error);

        RdoGrpType=findViewById(R.id.RdoGrpType);

        RbPrice=findViewById(R.id.RbPrice);
        RbService=findViewById(R.id.RbService);
        RbSpeed=findViewById(R.id.RbSpeed);
        RbOverall=findViewById(R.id.RbOverall);

        RbPrice.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
            {
                priceRatingFlag=true;
                Constants.priceRating=String.valueOf(rating);
                Toast.makeText(IspRatingsActivity.this, rating+" Stars for Price.", Toast.LENGTH_SHORT).show();
            }
        });


        RbService.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
            {
                serviceRatingFlag=true;
                Constants.serviceRating=String.valueOf(rating);
                Toast.makeText(IspRatingsActivity.this, rating+" Stars for Service.", Toast.LENGTH_SHORT).show();
            }
        });

        RbSpeed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
            {
                speedRatingFlag=true;
                Constants.speedRating=String.valueOf(rating);
                Toast.makeText(IspRatingsActivity.this, rating+" Stars for Speed.", Toast.LENGTH_SHORT).show();
            }
        });

        RbOverall.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
            {
                overallRatingFlag=true;
                Constants.overallRating=String.valueOf(rating);
                Toast.makeText(IspRatingsActivity.this, rating+" Stars Overall.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Setting Feedback
                Constants.feedback=txtFeedback.getText().toString().trim();

                //Setting Date
                Date date=new Date();
                SimpleDateFormat curDate=new SimpleDateFormat("dd/MM/yyyy");
                String getDate=curDate.format(date);
                Constants.reviewDate=getDate;

                boolean validateFeedback=awesomeValidation.validate();

                if(validateFeedback)
                {
                    if(typeFlag)
                    {
                        if(priceRatingFlag && speedRatingFlag && serviceRatingFlag && overallRatingFlag)
                        {
                            //Enabled ProgressDialog
                            progressDialog = new ProgressDialog(IspRatingsActivity.this);
                            progressDialog.setMessage("Submitting your Feedback...");
                            progressDialog.show();

                            //Enter Review here
                            final String ISP_Name = Constants.ISP_Name;
                            final String MAP_Latitude = Constants.MAP_Latitude;
                            final String MAP_Longitude = Constants.MAP_Longitude;

                            final String UserEmail = Constants.UserEmail;
                            final String reviewDate=Constants.reviewDate;

                            final String type = Constants.type;

                            final String priceRating = Constants.priceRating;
                            final String speedRating = Constants.speedRating;
                            final String serviceRating = Constants.serviceRating;
                            final String overallRating = Constants.overallRating;

                            final String feedback = Constants.feedback;

                            //Adding Review

                            final ReviewDetails review=new ReviewDetails(ISP_Name,MAP_Latitude,MAP_Longitude,UserEmail,type,speedRating,priceRating,serviceRating,overallRating,feedback,reviewDate);

                            FirebaseDatabase.getInstance().getReference("Reviews").
                                    push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //Disable ProgressDialog
                                        progressDialog.dismiss();

                                        Toast.makeText(IspRatingsActivity.this, "Successfully Added Review", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(IspRatingsActivity.this,MainActivity2.class));
                                        finish();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(IspRatingsActivity.this, "Adding Review Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(IspRatingsActivity.this, "Please Provide all Ratings!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(IspRatingsActivity.this, "Please Select Type of Connection!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(IspRatingsActivity.this, "Please Provide Feedback!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkType(View v)
    {
        int radioId=RdoGrpType.getCheckedRadioButtonId();

        RdoBtnType=findViewById(radioId);

        Constants.type=RdoBtnType.getText().toString().trim();

        typeFlag=true;

        Toast.makeText(this, "Selected "+RdoBtnType.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed ()
    {
        startActivity(new Intent(IspRatingsActivity.this,MainActivity2.class));
        this.finish();
    }
}