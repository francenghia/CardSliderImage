package com.example.franc.cardsliderimage.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.cardsliderimage.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView image;
    public TextView txtName,txtStatus,txtStatusType,txtEmail;

    public ViewHolder(View itemView) {
        super(itemView);
        image= itemView.findViewById(R.id.image);
        txtName= itemView.findViewById(R.id.name);
        txtStatus= itemView.findViewById(R.id.status);
        txtStatusType= itemView.findViewById(R.id.status_type);
        txtEmail= itemView.findViewById(R.id.gmail);
    }
}
