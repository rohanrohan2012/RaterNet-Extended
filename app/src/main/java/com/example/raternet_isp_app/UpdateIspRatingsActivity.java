package com.example.raternet_isp_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.raternet_isp_app.models.ReviewDetails;

public class UpdateIspRatingsActivity extends AppCompatActivity
{
    public ReviewDetails curReview;

    public TextView txtSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_isp_ratings);

        this.curReview=(ReviewDetails)getIntent().getSerializableExtra("CurrentReview");

        txtSample=findViewById(R.id.txtSample);

        txtSample.setText(this.curReview.getOverallRating());
    }
}