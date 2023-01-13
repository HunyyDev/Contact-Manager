package com.example.contactmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactmanager.MainActivity;
import com.example.contactmanager.R;
import com.example.contactmanager.db.Entity.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    public ArrayList<Contact> contactArrayList;
    private final MainActivity mainActivity;

    public ContactAdapter(ArrayList<Contact> contactArrayList, MainActivity mainActivity) {
        this.contactArrayList = contactArrayList;
        this.mainActivity = mainActivity;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvEmail;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        final Contact contact = contactArrayList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvEmail.setText(contact.getEmail());
        holder.itemView.setOnClickListener(v -> {
            mainActivity.addOrEditContacts(true, contact, position);
        });

    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }
}
