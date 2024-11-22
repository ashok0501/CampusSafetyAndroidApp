package com.sriher.campussafetyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class startedActivity extends AppCompatActivity {

    private EditText uniqueId, name, phoneNumber;
    private Button submitButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_started);
        sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uniqueId = findViewById(R.id.uniqueId);
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        if(sharedPreferences.contains("UniqueID"))
        {
            uniqueId.setText(sharedPreferences.getString("UniqueID",""));
            name.setText(sharedPreferences.getString("Name",""));
            phoneNumber.setText(sharedPreferences.getString("PhoneNumber",""));
        }
        submitButton=(Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = uniqueId.getText().toString().trim();
                String userName = name.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();

                if (id.isEmpty() || userName.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(startedActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    saveDataToSharedPreferences(id, userName, phone);
                    Toast.makeText(startedActivity.this, "Details Saved Successfully!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void saveDataToSharedPreferences(String id, String userName, String phone) {
        // Save data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UniqueID", id);
        editor.putString("Name", userName);
        editor.putString("PhoneNumber", phone);
        editor.apply(); // Save changes asynchronously
        startActivity(new Intent(this,HomeActivity.class));
    }

}