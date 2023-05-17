package com.example.womensafety.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.R;
import com.example.womensafety.model.HelpLine;

import java.util.ArrayList;

public class HelpLineAdapter extends RecyclerView.Adapter<HelpLineAdapter.ViewHolder> {

    ArrayList<HelpLine> helpLines;
    Context context;
    public HelpLineAdapter(Context context, ArrayList<HelpLine> helpLines) {
        this.context=context;
        this.helpLines = helpLines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_helpline, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.icon.setImageDrawable(helpLines.get(position).getIcon(context));
        holder.name.setText(helpLines.get(position).getName());
        holder.ph_no.setText(helpLines.get(position).getNumber());
        holder.callButton.setOnClickListener(v -> {
            String phoneNumber = helpLines.get(position).getNumber();
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(dialIntent);
        });
    }

    @Override
    public int getItemCount() {
        return helpLines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView ph_no;
        ImageView callButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.adapter_helpline_icon);
            name = itemView.findViewById(R.id.helpline_name);
            ph_no = itemView.findViewById(R.id.adapter_helpline_ph_no);
            callButton = itemView.findViewById(R.id.callButton);
        }
    }
}
