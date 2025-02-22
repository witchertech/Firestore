package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_PHONE = "phoneNumber";

    private FirebaseFirestore db;
    private TextView userName, userPhone, userState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        // Find Views
        userName = findViewById(R.id.user_name);
        userPhone = findViewById(R.id.user_phone);
        userState = findViewById(R.id.user_state);
        Button logoutButton = findViewById(R.id.logout_button);

        // Get stored phone number from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String phone = preferences.getString(KEY_PHONE, null);

        if (phone != null) {
            fetchUserData(phone);
        } else {
            Toast.makeText(this, "No user logged in!", Toast.LENGTH_SHORT).show();
            logout();
        }

        logoutButton.setOnClickListener(v -> logout());
    }

    private void fetchUserData(String phone) {
        db.collection("users").document(phone).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("fullName");
                        String state = documentSnapshot.getString("state");

                        userName.setText("Name: " + name);
                        userPhone.setText("Phone: " + phone);
                        userState.setText("State: " + state);
                    } else {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                        logout();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching data!", Toast.LENGTH_SHORT).show());
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }
}
