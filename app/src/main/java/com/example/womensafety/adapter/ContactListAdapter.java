package com.example.womensafety.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.OnAddAppButtonListener;
import com.example.womensafety.R;
import com.example.womensafety.db.Database;
import com.example.womensafety.model.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    protected Context context;
    protected ArrayList<Contact> contacts;
    Database database;
    ViewHolder holder;
    OnAddAppButtonListener onAddAppButtonListener;
    private List<Contact> filteredContacts;

    public ContactListAdapter(Context context, ArrayList<Contact> contacts, OnAddAppButtonListener onAddAppButtonListener) {
        this.context = context;
        this.contacts = contacts;
        this.filteredContacts = new ArrayList<>(contacts);
        database = new Database(context);
        this.onAddAppButtonListener = onAddAppButtonListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        this.holder = holder;
        if (database.checkPackage(contacts.get(position))) {
            contacts.get(position).setSelected(true);
        }
        Contact contact = filteredContacts.get(position);
        holder.app_name.setText(contact.getName());
        holder.ph_no.setText(contact.getPh_no());
        holder.checkBox.setChecked(contact.isSelected());
        holder.checkBox.setTag(position);
    }

    /**
     * @return positions collection of positions which are checked (checkBox)
     * & replace with -1 in unchecked positions.
     **/
    public int[] getCheckedItemsPosition() {
        int[] positions = new int[contacts.size()];
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).isSelected()) {
                positions[i] = i;
            } else {
                positions[i] = -1;
            }
        }
        return positions;
    }

    public boolean isAnyCheckBoxChecked() {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return filteredContacts.size();
    }

    /**
     * @return false
     **/
    public boolean isCheckBoxListEmpty() {
        int lastItem = 0;
        for (int i = 0; i < contacts.size(); i++) {
            if (!contacts.get(i).isSelected()) {
                lastItem++;
            }
            if (lastItem == 1) {
                return true;
            }
        }
        return false;
    }

    // Filter method to search contacts by name or number
    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        filteredContacts.clear();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())
                    || contact.getPh_no().contains(query)) {
                filteredContacts.add(contact);
            }
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sortList() {
        // Sort the list in ascending order based on the object's property
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact obj1, Contact obj2) {
                return obj1.getName().compareToIgnoreCase(obj2.getName());
            }
        });

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Contact> appInfos) {
        contacts.clear();
        contacts.addAll(appInfos);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView app_name;
        TextView ph_no;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            app_name = itemView.findViewById(R.id.adapter_name);
            ph_no = itemView.findViewById(R.id.adapter_ph_no);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(v -> {
                int position = (int) v.getTag();
                contacts.get(position).setSelected(checkBox.isChecked());
//                onAppSelectListener.onAppSelected(appInfo.get(position), appInfo.get(position).isSelected(), position);
                onAddAppButtonListener.isAnyCheckboxChecked(isCheckBoxListEmpty(), isAnyCheckBoxChecked());
            });
        }
    }
}
