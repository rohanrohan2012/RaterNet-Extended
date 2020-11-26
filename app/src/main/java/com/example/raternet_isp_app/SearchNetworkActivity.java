package com.example.raternet_isp_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.raternet_isp_app.adapter.UserReviewAdapter;
import com.example.raternet_isp_app.models.Constants;
import com.example.raternet_isp_app.models.ReviewDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchNetworkActivity extends AppCompatActivity {

    private UserReviewAdapter userReviewAdapter;
    private RecyclerView userReviews;
    private List<ReviewDetails> reviewDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_network);
        reviewDetailsList = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Scanning your Location..."); // show progess dialog till server responds
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference().child("Reviews")
                .orderByChild("locality").equalTo(Constants.locality).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                    //Toast.makeText(getApplicationContext(),messageSnapshot.child("isp_Name").getValue().toString(),Toast.LENGTH_SHORT).show();
                    ReviewDetails review = new ReviewDetails(
                            messageSnapshot.child("isp_Name").getValue().toString(),
                            messageSnapshot.child("userEmail").getValue().toString(),
                            messageSnapshot.child("overallRating").getValue().toString(),
                            messageSnapshot.child("feedback").getValue().toString());
                    reviewDetailsList.add(review);
                }
                progressDialog.dismiss();
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchNetworkActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initRecyclerView(){
        userReviews = findViewById(R.id.reviews);
        userReviewAdapter = new UserReviewAdapter(this,reviewDetailsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchNetworkActivity.this);
        userReviews.setLayoutManager(layoutManager);
        userReviews.setAdapter(userReviewAdapter);
    }
}