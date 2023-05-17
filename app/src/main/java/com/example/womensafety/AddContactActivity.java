package com.example.womensafety;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.womensafety.db.Database;
import com.example.womensafety.model.Contact;

public class AddContactActivity extends AppCompatActivity {

    EditText editTxtUserName, editTxtMobileNumber;
    TextView txtSaveUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        editTxtUserName = findViewById(R.id.editTxtUserName);
        editTxtMobileNumber = findViewById(R.id.editTxtMobileNumber);
        txtSaveUser = findViewById(R.id.txtSaveUser);

        txtSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTxtUserName.getText().equals("")) {
                    Toast.makeText(AddContactActivity.this, "User name cannot be Empty", Toast.LENGTH_SHORT).show();
                } else if (editTxtMobileNumber.getText().equals("")) {
                    Toast.makeText(AddContactActivity.this, "Mobile Number is required", Toast.LENGTH_SHORT).show();
                } else {
                    Database database = new Database(AddContactActivity.this);
                    database.saveContact(new Contact(editTxtUserName.getText().toString(), editTxtMobileNumber.getText().toString()));
                    Toast.makeText(AddContactActivity.this, "Contact Saved!", Toast.LENGTH_SHORT).show();

                    editTxtMobileNumber.setText("");
                    editTxtUserName.setText("");
                }
            }
        });
    }
}
