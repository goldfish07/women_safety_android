package com.example.womensafety.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.MainActivity;
import com.example.womensafety.R;
import com.example.womensafety.db.Database;
import com.example.womensafety.model.Contact;

import java.util.ArrayList;

public class SavedContactListAdapter extends RecyclerView.Adapter<SavedContactListAdapter.ViewHolder> {

    protected Context context;
    protected ArrayList<Contact> contacts;
    ViewHolder holder;
    Database database;

    public SavedContactListAdapter(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        database = new Database(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        this.holder = holder;
        holder.app_name.setText(contacts.get(position).getName());
        holder.ph_no.setText(contacts.get(position).getPh_no());
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteContactDialog(contacts.get(position));
            }
        });
    }

    private void showDeleteContactDialog(Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Contact");
        builder.setMessage("Are you sure you want to delete this contact?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform delete operation here
                database.delSelectedAppList(contact);
                contacts.remove(contact);
                notifyDataSetChanged();
                Toast.makeText(context, "Contact deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView app_name;
        TextView ph_no;
        ImageView checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            app_name = itemView.findViewById(R.id.adapter_name);
            ph_no = itemView.findViewById(R.id.adapter_ph_no);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
