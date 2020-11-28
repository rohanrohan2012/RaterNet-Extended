package com.example.raternet_isp_app.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raternet_isp_app.R;

import com.example.raternet_isp_app.models.Discussion;
import com.example.raternet_isp_app.models.ReviewDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DiscussAdapter extends FirebaseRecyclerAdapter<Discussion,DiscussAdapter.DiscussViewHolder>
{
    public FirebaseAuth firebaseAuth;
    public DiscussAdapter(@NonNull FirebaseRecyclerOptions<Discussion> options) {
        super(options);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull DiscussViewHolder holder,
                                    int position,
                                    @NonNull Discussion model)
    {

        holder.userEmail.setText(model.getUserEmail());
        holder.issueType.setText(model.getIssueType());
        holder.issueTitle.setText(model.getIssueTitle());
        if(firebaseAuth.getCurrentUser().getEmail().equals(model.getUserEmail()))
            holder.issueTitle.setGravity(Gravity.RIGHT);
        else
            holder.issueTitle.setGravity(Gravity.LEFT);
    }

    @NonNull
    @Override
    public DiscussViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_discussion,parent,false);
        return new DiscussViewHolder(view);
    }

    class DiscussViewHolder extends RecyclerView.ViewHolder
    {
        TextView userEmail;
        TextView issueType;
        TextView issueTitle;

        public DiscussViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail=itemView.findViewById(R.id.txtuser_email);
            issueTitle=itemView.findViewById(R.id.txtissueTitle);
            issueType=itemView.findViewById(R.id.txtissueType);

        }
    }
}
