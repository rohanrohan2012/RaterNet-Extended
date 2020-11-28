package com.example.raternet_isp_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.raternet_isp_app.adapter.CompanyAdapter;
import com.example.raternet_isp_app.adapter.UserReviewAdapter;
import com.example.raternet_isp_app.models.Company;
import com.example.raternet_isp_app.models.Constants;
import com.example.raternet_isp_app.models.ReviewDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchNetworkActivity extends AppCompatActivity {

    private UserReviewAdapter userReviewAdapter;
    private CompanyAdapter companyAdapter;
    private RecyclerView userReviews,companies;
    private List<ReviewDetails> reviewDetailsList;
    private List<Company> companyList;
    private Integer noofUsers;
    private Map<String,Integer> noofUsersMap;
    private Map<String,Float> avgRatingMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_network);

        reviewDetailsList = new ArrayList<>();
        companyList = new ArrayList<>();
        noofUsersMap = new HashMap<>();
        avgRatingMap = new HashMap<>();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Scanning your Location..."); // show progess dialog till server responds
        progressDialog.show();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Company").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot messageSnapshot: snapshot.getChildren()){
                    noofUsersMap.put(messageSnapshot.child("Name").getValue().toString(),0);
                    avgRatingMap.put(messageSnapshot.child("Name").getValue().toString(), (float) 0.0);
                    Log.i("name1",messageSnapshot.child("Name").getValue().toString());
                    Company company = new Company(
                            messageSnapshot.child("Name").getValue().toString(),
                            messageSnapshot.child("url").getValue().toString(),
                            messageSnapshot.child("photoUrl").getValue().toString(),
                            messageSnapshot.child("Number").getValue().toString(),
                            messageSnapshot.child("Address").getValue().toString(),
                            messageSnapshot.child("TypeofService").getValue().toString(),
                            messageSnapshot.child("noofUsers").getValue().toString(),
                            messageSnapshot.child("avgRating").getValue().toString()
                    );
                    companyList.add(company);
                }
                //Log.i("size",String.valueOf(companyList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchNetworkActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("Reviews").orderByChild("locality")
                .equalTo(Constants.locality).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noofUsers = (int) snapshot.getChildrenCount();
                for (DataSnapshot messageSnapshot: snapshot.getChildren()) {
                    //Toast.makeText(getApplicationContext(),messageSnapshot.child("isp_Name").getValue().toString(),Toast.LENGTH_SHORT).show();
                    ReviewDetails review = new ReviewDetails(
                            messageSnapshot.child("isp_Name").getValue().toString(),
                            messageSnapshot.child("userEmail").getValue().toString(),
                            messageSnapshot.child("overallRating").getValue().toString(),
                            messageSnapshot.child("feedback").getValue().toString());
                    Log.i("name",messageSnapshot.child("isp_Name").getValue().toString());

                    if(noofUsersMap.containsKey(messageSnapshot.child("isp_Name").getValue().toString())) {
                        //Log.i("name", noofUsersMap.get(messageSnapshot.child("isp_Name").getValue().toString()).toString());
                        noofUsersMap.put(messageSnapshot.child("isp_Name").getValue().toString(),
                                noofUsersMap.get(messageSnapshot.child("isp_Name").getValue().toString()) + 1);

                    }

                    if(avgRatingMap.containsKey(messageSnapshot.child("isp_Name").getValue().toString())) {
                        avgRatingMap.put(messageSnapshot.child("isp_Name").getValue().toString(),
                                avgRatingMap.get(messageSnapshot.child("isp_Name").getValue().toString())
                                        + Float.parseFloat(messageSnapshot.child("overallRating").getValue().toString())
                        );
                    }
                    reviewDetailsList.add(review);
                }

                for(Company company : companyList){
                    avgRatingMap.put(company.getName(),
                            avgRatingMap.get(company.getName())/noofUsersMap.get(company.getName()));
                    company.setAvgRating(avgRatingMap.get(company.getName()).toString());
                    company.setNoofUsers(noofUsersMap.get(company.getName()).toString()); // update calculated values of avgRating and noofUsers
                }

                progressDialog.dismiss();
                initCompanyRecyclerView();
                initReviewRecyclerView();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchNetworkActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void initReviewRecyclerView(){
        userReviews = findViewById(R.id.reviews);
        userReviewAdapter = new UserReviewAdapter(this,reviewDetailsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchNetworkActivity.this);
        userReviews.setLayoutManager(layoutManager);
        userReviews.setAdapter(userReviewAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initCompanyRecyclerView(){
        companies = findViewById(R.id.betternetworks);
        companyAdapter = new CompanyAdapter(this,companyList);
        companies.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        companies.setAdapter(companyAdapter);
    }
}