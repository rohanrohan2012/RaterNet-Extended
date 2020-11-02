package com.example.raternet_isp_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raternet_isp_app.models.ReviewDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

public class ReviewViewAdapter extends FirebaseRecyclerAdapter<ReviewDetails,ReviewViewAdapter.ViewHolder>
{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReviewViewAdapter(@NonNull FirebaseRecyclerOptions<ReviewDetails> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ReviewDetails model)
    {
        holder.ispName.setText(model.getISP_Name());
        holder.typeName.setText(model.getType());
        holder.dateName.setText(model.getReviewDate());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ispName=(TextView)itemView.findViewById(R.id.ispText);
            typeName=(TextView)itemView.findViewById(R.id.typeText);
            dateName=(TextView)itemView.findViewById(R.id.dateText);
        }
    }



}
