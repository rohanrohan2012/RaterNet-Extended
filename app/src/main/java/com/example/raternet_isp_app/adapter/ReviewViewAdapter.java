package com.example.raternet_isp_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raternet_isp_app.R;
import com.example.raternet_isp_app.UpdateIspRatingsActivity;
import com.example.raternet_isp_app.models.ReviewDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ReviewViewAdapter extends FirebaseRecyclerAdapter<ReviewDetails,ReviewViewAdapter.ViewHolder>
{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context context;

    public ReviewViewAdapter(@NonNull FirebaseRecyclerOptions<ReviewDetails> options,Context context)
    {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ReviewDetails model)
    {
        holder.ispName.setText(model.getISP_Name());
        holder.typeName.setText(model.getType());
        holder.dateName.setText(model.getReviewDate());

        final ReviewDetails curReview=model;

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context, "Clicked! "+curReview.getFeedback()+" "+curReview.getUserEmail(), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(context, UpdateIspRatingsActivity.class);

                intent.putExtra("CurrentReview",curReview);

                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_review,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView ispName;
        TextView typeName;
        TextView dateName;

        CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ispName=(TextView)itemView.findViewById(R.id.ispText);
            typeName=(TextView)itemView.findViewById(R.id.typeText);
            dateName=(TextView)itemView.findViewById(R.id.dateText);

            parent=(CardView)itemView.findViewById(R.id.parent);
        }
    }
}
