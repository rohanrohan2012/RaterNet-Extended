package com.example.raternet_isp_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.raternet_isp_app.models.ReviewDetails;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateReviewActivity extends AppCompatActivity {

    public RecyclerView recView;
    public ReviewViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_review);

        recView=findViewById(R.id.recView);

        recView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ReviewDetails> options =
                new FirebaseRecyclerOptions.Builder<ReviewDetails>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Reviews"), ReviewDetails.class)
                        .build();

        adapter=new ReviewViewAdapter(options);

        recView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed ()
    {
        startActivity(new Intent(UpdateReviewActivity.this,MainActivity2.class));
        this.finish();
    }
}