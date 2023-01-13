package com.example.contactmanager;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.contactmanager.adapter.ContactAdapter;
import com.example.contactmanager.db.DatabaseHelper;
import com.example.contactmanager.db.Entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private RecyclerView rvContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrEditContacts(false, null, -1);
            }
        });
        db = new DatabaseHelper(this);
        contactArrayList.addAll(db.getAllContacts());
        contactAdapter = new ContactAdapter(contactArrayList, this);
        rvContactList = findViewById(R.id.rvContactList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvContactList.setLayoutManager(layoutManager);
        rvContactList.setAdapter(contactAdapter);
    }

    public void addOrEditContacts(final boolean isUpdateAction, final Contact contact, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.dialog_add_contact, null, false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        TextView contactTitle = view.findViewById(R.id.tvTitle);
        final EditText etName = view.findViewById(R.id.etName);
        final EditText etEmail = view.findViewById(R.id.etEmail);

        contactTitle.setText(isUpdateAction ? "Edit Contact" : "Add New Contact");
        if (isUpdateAction && contact != null) {
            etName.setText(contact.getName());
            etEmail.setText(contact.getEmail());
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(isUpdateAction ? "Save" : "Add", (dialog, which) -> {
                })
                .setNegativeButton(isUpdateAction ? "Delete" : "Cancel", (dialog, which) -> {
                    if (isUpdateAction) {
                        deleteContact(contact, position);
                    } else {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (TextUtils.isEmpty(etEmail.getText().toString())) {
                Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            } else {
                alertDialog.dismiss();
            }
            if (isUpdateAction && contact != null) {
                updateContact(etName.getText().toString(), etEmail.getText().toString(), position);
            } else {
                createContact(etName.getText().toString(), etEmail.getText().toString());
            }
        });
    }

    private void createContact(String name, String email) {
        long ID = db.insertContact(name, email);
        Contact contact = db.getContact(ID);

        if (contact != null) {
            contactArrayList.add(0, contact);
            contactAdapter.notifyDataSetChanged();
        }
    }

    private void updateContact(String name, String email, int position) {
        Contact contact = contactArrayList.get(position);
        contact.setName(name);
        contact.setEmail(email);
        db.updateContact(contact);
        contactArrayList.set(position, contact);
        contactAdapter.notifyDataSetChanged();
    }

    private void deleteContact(Contact contact, int position) {
        contactArrayList.remove(position);
        db.deleteContact(contact);
        contactAdapter.notifyDataSetChanged();
    }
}






















