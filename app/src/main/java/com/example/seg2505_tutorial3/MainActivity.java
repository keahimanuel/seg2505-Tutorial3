package com.example.seg2505_tutorial3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView textViewUsername;
    private TextView textViewEmail;

    private String userId = "user123"; // Exemple d'ID utilisateur
    private ValueEventListener userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des TextViews
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);

        // Initialisation de Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Écrire un nouvel utilisateur
        writeNewUser(userId, "John Doe", "john.doe@example.com");

        // Lire les données de l'utilisateur avec un écouteur persistant
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    textViewUsername.setText("Nom : " + user.username);
                    textViewEmail.setText("Email : " + user.email);
                } else {
                    Log.d("Firebase", "Les données de l'utilisateur sont nulles");
                }

