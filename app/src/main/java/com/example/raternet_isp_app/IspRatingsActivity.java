package com.example.raternet_isp_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.concurrent.CompletionService;

public class IspRatingsActivity extends AppCompatActivity {

    public RadioGroup RdoGrpType;
    public RadioButton RdoBtnType;

    public Button btnSubmitReview;

    public EditText txtFeedback;

    public RatingBar RbPrice;
    public RatingBar RbService;
    public RatingBar RbSpeed;
    public RatingBar RbOverall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isp_ratings);

        btnSubmitReview=findViewById(R.id.btnSubmitReview);

        txtFeedback=findViewById(R.id.txtFeedback);

        RdoGrpType=findViewById(R.id.RdoGrpType);

        RbPrice=findViewById(R.id.RbPrice);
        RbService=findViewById(R.id.RbService);
        RbSpeed=findViewById(R.id.RbSpeed);
        RbOverall=findViewById(R.id.RbOverall);

        RbPrice.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
            {
                Constants.priceRating=String.valueOf(rating);
                Toast.makeText(IspRatingsActivity.this, rating+" Stars for Price.", Toast.LENGTH_SHORT).show();
            }
        });


        RbService.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
            {
                Constants.serviceRating=String.valueOf(rating);
                Toast.makeText(IspRatingsActivity.this, rating+" Stars for Service.", Toast.LENGTH_SHORT).show();
            }
        });

        RbSpeed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
            {
                Constants.speedRating=String.valueOf(rating);
                Toast.makeText(IspRatingsActivity.this, rating+" Stars for Speed.", Toast.LENGTH_SHORT).show();
            }
        });

        RbOverall.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b)
            {
                Constants.overallRating=String.valueOf(rating);
                Toast.makeText(IspRatingsActivity.this, rating+" Stars Overall.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


            }
        });

    }

    public void checkType(View v)
    {
        int radioId=RdoGrpType.getCheckedRadioButtonId();

        RdoBtnType=findViewById(radioId);

        Constants.type=RdoBtnType.getText().toString().trim();

        Toast.makeText(this, "Selected "+RdoBtnType.getText(), Toast.LENGTH_SHORT).show();
    }
}