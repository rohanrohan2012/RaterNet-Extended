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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionService;


public class IspRatingsActivity extends AppCompatActivity {

    public FirebaseAuth auth;

    //Insert Review
    public RadioGroup RdoGrpType;
    public RadioButton RdoBtnType;

    public Button btnSubmitReview;

    public EditText txtFeedback;

    public RatingBar RbPrice;
    public RatingBar RbService;
    public RatingBar RbSpeed;
    public RatingBar RbOverall;
    private ProgressDialog progressDialog;

    //Update Review
    public ReviewDetails curReview;

    public Button btnUpdateReview;

    public EditText txtUpdateFeedback;

    public RatingBar RbUpdatePrice;
    public RatingBar RbUpdateService;
    public RatingBar RbUpdateSpeed;
    public RatingBar RbUpdateOverall;

    private ProgressDialog progressDialog2;

    //General Required Variables
    public boolean typeFlag=false;
    public boolean priceRatingFlag=false;
    public boolean serviceRatingFlag=false;
    public boolean speedRatingFlag=false;
    public boolean overallRatingFlag=false;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        
        if(intent.hasExtra("CurrentReview"))
        {
            setContentView(R.layout.activity_update_isp_ratings);

            btnUpdateReview=findViewById(R.id.btnUpdateReview);

            txtUpdateFeedback=findViewById(R.id.txtUpdateFeedback);

            RbUpdatePrice=findViewById(R.id.RbUpdatePrice);
            RbUpdateSpeed=findViewById(R.id.RbUpdateSpeed);
            RbUpdateService=findViewById(R.id.RbUpdateService);
            RbUpdateOverall=findViewById(R.id.RbUpdateOverall);

            awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

            awesomeValidation.addValidation(this,R.id.txtUpdateFeedback, RegexTemplate.NOT_EMPTY,R.string.feedback_error);

            this.curReview=(ReviewDetails)intent.getSerializableExtra("CurrentReview");

            RbUpdatePrice.setRating(Float.valueOf(curReview.getPriceRating()));
            RbUpdateSpeed.setRating(Float.valueOf(curReview.getSpeedRating()));
            RbUpdateService.setRating(Float.valueOf(curReview.getServiceRating()));
            RbUpdateOverall.setRating(Float.valueOf(curReview.getOverallRating()));

            txtUpdateFeedback.setText(curReview.getFeedback());

            RbUpdatePrice.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
                {
                    priceRatingFlag=true;
                    Constants.priceRating=String.valueOf(rating);
                    Toast.makeText(IspRatingsActivity.this, rating+" Stars for Price.", Toast.LENGTH_SHORT).show();
                }
            });

            RbUpdateSpeed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
                {
                    serviceRatingFlag=true;
                    Constants.serviceRating=String.valueOf(rating);
                    Toast.makeText(IspRatingsActivity.this, rating+" Stars for Service.", Toast.LENGTH_SHORT).show();
                }
            });

            RbUpdateService.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
                {
                    speedRatingFlag=true;
                    Constants.speedRating=String.valueOf(rating);
                    Toast.makeText(IspRatingsActivity.this, rating+" Stars for Speed.", Toast.LENGTH_SHORT).show();
                }
            });

            RbUpdateOverall.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
                {
                    overallRatingFlag=true;
                    Constants.overallRating=String.valueOf(rating);
                    Toast.makeText(IspRatingsActivity.this, rating+" Stars Overall.", Toast.LENGTH_SHORT).show();
                }
            });

            btnUpdateReview.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //Setting Feedback
                    Constants.feedback=txtUpdateFeedback.getText().toString().trim();

                    //Setting Date
                    Date date=new Date();
                    SimpleDateFormat curDate=new SimpleDateFormat("dd/MM/yyyy");
                    String getDate=curDate.format(date);
                    Constants.reviewDate=getDate;

                    boolean validateFeedback=awesomeValidation.validate();

                    if(validateFeedback)
                    {
                        //Enabled ProgressDialog
                        progressDialog2 = new ProgressDialog(IspRatingsActivity.this);
                        progressDialog2.setMessage("Updating your Feedback...");
                        progressDialog2.show();

                        //Creating Map
                        Map<String,Object>map=new HashMap<>();

                        map.put("feedback",Constants.feedback);
                        map.put("reviewDate",Constants.reviewDate);

                        map.put("priceRating",Constants.priceRating);
                        map.put("speedRating",Constants.speedRating);
                        map.put("serviceRating",Constants.serviceRating);
                        map.put("overallRating",Constants.overallRating);

                        FirebaseDatabase.getInstance().getReference().child("Reviews")
                                .child(Constants.reviewKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    //Disable ProgressDialog
                                    progressDialog2.dismiss();

                                    Toast.makeText(IspRatingsActivity.this, "Successfully Update Review", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(IspRatingsActivity.this,MainActivity2.class));
                                    finish();
                                }
                                else
                                {
                                    //Disable ProgressDialog
                                    progressDialog2.dismiss();

                                    Toast.makeText(IspRatingsActivity.this, "Failed to Update Review", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(IspRatingsActivity.this,MainActivity2.class));
                                    finish();
                                }
                            }
                        });
                    }
                }
            });
        }
        else
        {
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