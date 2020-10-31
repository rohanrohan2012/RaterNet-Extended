package com.example.raternet_isp_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class UpdateReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_review);
    }

    @Override
    public void onBackPressed ()
    {
        startActivity(new Intent(UpdateReviewActivity.this,MainActivity2.class));
        this.finish();
    }
}