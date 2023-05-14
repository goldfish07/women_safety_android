package com.example.womensafety.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.OnAddMoreItemsListener;
import com.example.womensafety.R;
import com.example.womensafety.model.Contact;

import java.util.ArrayList;

public class SelectedContactAdapter extends RecyclerView.Adapter<SelectedContactAdapter.ViewHolder> {

    private final int VIEW_TYPE_DEFAULT = 0;
    private final int VIEW_TYPE_IMAGE = 1;
    protected Context context;
    protected ArrayList<Contact> contacts;
    int RECENT_PHOTO_LIMIT = 50;
    int[] positions;
    OnAddMoreItemsListener onAddMoreItemsListener;

    public SelectedContactAdapter(Context context, ArrayList<Contact> contacts, OnAddMoreItemsListener onAddMoreItemsListener) {
        this.context = context;
        this.contacts = contacts;
        positions = new int[contacts.size()];
        this.onAddMoreItemsListener = onAddMoreItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_dash_contact, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        if(position==0){
//            holder.app_icon.setImageResource(R.drawable.ic_add_main_app);
//            holder.app_name.setText("");
//        } else {
        //   }
//        if (position < Math.min(contacts.size(), RECENT_PHOTO_LIMIT)) {
            holder.app_name.setText(contacts.get(position).getName());
            holder.ph_no.setText(contacts.get(position).getPh_no());
//            holder.app_icon.setOnClickListener(v -> {
//                try {
//                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appInfo.get(position).packageName);
//                    context.startActivity(launchIntent);
//                } catch (NullPointerException e) {
//                    Toast.makeText(v.getContext(), "App maybe uninstalled", Toast.LENGTH_SHORT).show();
//                    new Database(v.getContext()).delSelectedAppList(appInfo.get(position));
//                    appInfo.remove(position);
//                    notifyItemRemoved(position);
//                }
//            });

//        } else {
            //            holder.app_icon.setOnClickListener(v ->
//                            onAddMoreItemsListener.onClickAddMore()
//                    // context.startActivity(new Intent(context, AddAppsListActivity.class))
//            );
//            holder.app_name.setText("");
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(ArrayList<Contact> info) {
        contacts.clear();
        contacts.addAll(info);
        this.notifyDataSetChanged();
    }

    private void deleteItem(ViewHolder holder, int position) {
        contacts.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        // Limit the recent photo selection
        return contacts.size(); //Math.min(contacts.size(), RECENT_PHOTO_LIMIT) + 1;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return (position == Math.min(contacts.size(), RECENT_PHOTO_LIMIT)) ? VIEW_TYPE_DEFAULT : VIEW_TYPE_IMAGE;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ph_no;
        TextView app_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ph_no = itemView.findViewById(R.id.adapter_ph_no);
            app_name = itemView.findViewById(R.id.adapter_name);
        }
    }
}
