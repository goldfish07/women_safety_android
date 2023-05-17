package com.example.womensafety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.adapter.SavedContactListAdapter;
import com.example.womensafety.db.Database;
import com.example.womensafety.model.Contact;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ShowRegisteredContact extends BottomSheetDialogFragment {
    RecyclerView recyclerView;
    Database database;
    LinearLayout layoutContactNotFound;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = new Database(getContext());
        return inflater.inflate(R.layout.actvity_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Contact> contactArrayList = database.getContactList();
        recyclerView = view.findViewById(R.id.contactRecyclerView);
        layoutContactNotFound = view.findViewById(R.id.layoutContactNotFound);
        if(contactArrayList.isEmpty()){
            layoutContactNotFound.setVisibility(View.VISIBLE);
        }
        SavedContactListAdapter savedContactListAdapter = new SavedContactListAdapter(view.getContext(), contactArrayList);
        recyclerView.setAdapter(savedContactListAdapter);
    }
}
