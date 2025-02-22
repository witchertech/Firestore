package com.example.myapplication;

import android.os.Bundle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore FIRST
        db = FirebaseFirestore.getInstance();

        // Configure settings AFTER initialization
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db = FirebaseFirestore.getInstance();

        // Find the Spinner by ID
        Spinner stateSpinner = findViewById(R.id.state_spinner);

        // List of Indian States
        String[] indianStates = {
                "Select State", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
                "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
                "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra",
                "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
                "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
                "Telangana", "Tripura", "Uttar Pradesh",
                "Uttarakhand", "West Bengal"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, indianStates);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        stateSpinner.setAdapter(adapter);

        // Set an item selected listener for the spinner
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // Ignore default option ("Select State")
                    String selectedState = indianStates[position];
                    Toast.makeText(MainActivity.this,
                            "Selected State: " + selectedState, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> saveUserData());
    }

    private void saveUserData() {
        EditText nameInput = findViewById(R.id.name_input);
        EditText phoneInput = findViewById(R.id.phone_number_input);
        Spinner stateSpinner = findViewById(R.id.state_spinner);

        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String state = stateSpinner.getSelectedItem().toString();

        if (phone.length() != 10) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> user = new HashMap<>();
        user.put("fullName", name);
        user.put("phoneNumber", phone);
        user.put("state", state);

        // Add document with phone as ID
        db.collection("users").document(phone)
                .set(user)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(MainActivity.this, "Data saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }



}