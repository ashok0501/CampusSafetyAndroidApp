package com.sriher.campussafetyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<CardModel> cardList;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double[] locationCoordinates = new double[2];

    public double lat,lon;
    private SharedPreferences sharedPreferences;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");

    private CountDownTimer countDownTimer;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle=getIntent().getExtras();
        lat=0;
        lon=0;
        if(bundle!=null) {
            lat = bundle.getDouble("lat", 0);
            lon = bundle.getDouble("lon", 0);
        }
        recyclerView = findViewById(R.id.recycler);

        cardList = new ArrayList<>();
        cardList.add(new CardModel(12,24, "G Block", "9840999804"));
        cardList.add(new CardModel(56,78, "Medical Center", "9840999808"));
        cardList.add(new CardModel(23,45, "Outer Areas", "9840999823"));
        cardList.add(new CardModel(67,89, "Main Gate (EXT: 250)", "9480999803"));
        cardList.add(new CardModel(45,89, "Ambulance", "04424768402"));


        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardAdapter = new CardAdapter(cardList,this);
        recyclerView.setAdapter(cardAdapter);

        Button but=(Button) findViewById(R.id.butcallnear);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findShortestItem();
            }
        });

        Button emer=(Button) findViewById(R.id.butemergency);
        emer.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showCountdownDialog();
            }
        });
    }

    public void saveDataToFirebase()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        String uid,name,phno,time;
        uid=sharedPreferences.getString("UniqueID","");
        name=sharedPreferences.getString("Name","");
        phno=sharedPreferences.getString("PhoneNumber","");


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(calendar.getTime());

        System.out.println(uid);
        User user = new User(uid, name, lat, lon,currentDateTime,phno);
        database.child(uid).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Data saved successfully.");
            } else {
                System.out.println("Failed to save data: " + task.getException().getMessage());
            }
        });
        showInformationDialog();
    }
    public void call(String ph)
    {
        String phoneNumber =ph; // Replace with your number
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void findShortestItem() {

        call(cardList.get(0).getPhone());
        CardModel shortestItem = null;
        double shortestDistance = Double.MAX_VALUE;
        for (CardModel item : cardList) {
            double itemLatitude = item.getLat();
            double itemLongitude = item.getLon();

            float[] results = new float[1];
            Location.distanceBetween(
                    lat,
                    lon,
                    itemLatitude,
                    itemLongitude,
                    results
            );
            double distance = results[0];
            if (distance < shortestDistance) {
                shortestDistance = distance;
                shortestItem = item;
            }
        }
        if (shortestItem != null) {
            call(shortestItem.getPhone());
        } else {
            call(cardList.get(0).getPhone());
        }
    }


    private void showCountdownDialog() {
        // Create a TextView for countdown display
        final TextView countdownText = new TextView(this);
        countdownText.setTextSize(18);
        countdownText.setPadding(20, 20, 20, 20);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call Security office Countdown Dialog")
                .setMessage("Your data (automatically) will reach security office in:")
                .setView(countdownText)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        dialog.dismiss();
                    }
                });

        alertDialog = builder.create();
        alertDialog.show();

        // Start the countdown timer
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText("Time left: " + (millisUntilFinished / 1000) + " sec");
            }

            @Override
            public void onFinish() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                saveDataToFirebase();
            }
        }.start();
    }

    private void showInformationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert")
                .setMessage("Information Sent to Security Office. Security Office will call you soon.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

}