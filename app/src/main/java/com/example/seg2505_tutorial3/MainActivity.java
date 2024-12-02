package com.example.seg2505_tutorial3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;

    private ChildEventListener messagesListener;
    private ValueEventListener connectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation de Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Synchroniser les messages pour une utilisation hors ligne
        DatabaseReference messagesRef = mDatabase.child("messages");
        messagesRef.keepSynced(true);

        // Initialiser RecyclerView et l'adaptateur
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter();
        recyclerViewMessages.setAdapter(messageAdapter);

        // Écouteur pour les messages
        messagesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    message.key = dataSnapshot.getKey(); // Définir la clé du message
                    displayNewMessage(message);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    message.key = dataSnapshot.getKey();
                    updateMessageDisplay(message);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String messageKey = dataSnapshot.getKey();
                removeMessageFromDisplay(messageKey);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Gérer le déplacement si nécessaire
                Message movedMessage = dataSnapshot.getValue(Message.class);
                if (movedMessage != null) {
                    movedMessage.key = dataSnapshot.getKey();
                    moveMessageInDisplay(movedMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "messagesListener:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Échec du chargement des messages.", Toast.LENGTH_SHORT).show();
            }
        };

        // Ajouter l'écouteur des messages avec tri et limitation
        Query messagesQuery = messagesRef.limitToLast(50);
        messagesQuery.addChildEventListener(messagesListener);

        // Écouteur pour l'état de connexion
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean connected = snapshot.getValue(Boolean.class);
                if (Boolean.TRUE.equals(connected)) {
                    Log.d("Firebase", "Connecté au serveur Firebase Realtime Database.");
                    Toast.makeText(MainActivity.this, "Connecté", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Firebase", "Déconnecté du serveur Firebase Realtime Database.");
                    Toast.makeText(MainActivity.this, "Déconnecté", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Impossible d'obtenir l'état de connexion.", error.toException());
            }
        };
        connectedRef.addValueEventListener(connectedListener);

        // Ajouter un message de test
        writeNewMessage("user123", "John Doe", "Bonjour, ceci est un message test.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messagesListener != null) {
            DatabaseReference messagesRef = mDatabase.child("messages");
            messagesRef.removeEventListener(messagesListener);
        }
        if (connectedListener != null) {
            DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.removeEventListener(connectedListener);
        }
    }

    private void writeNewMessage(String userId, String username, String text) {
        DatabaseReference messagesRef = mDatabase.child("messages");
        String key = messagesRef.push().getKey();
        Message message = new Message(userId, username, text);
        message.key = key; // Définir la clé du message
        Map<String, Object> messageValues = message.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/" + key, messageValues);

        mDatabase.updateChildren(childUpdates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Message ajouté avec succès.");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Échec de l'ajout du message.", e);
                });
    }

    // Méthodes pour mettre à jour l'interface utilisateur
    private void displayNewMessage(Message message) {
        messageAdapter.addMessage(message);
    }

    private void updateMessageDisplay(Message message) {
        messageAdapter.updateMessage(message);
    }

    private void removeMessageFromDisplay(String messageKey) {
        messageAdapter.removeMessage(messageKey);
    }

    private void moveMessageInDisplay(Message message) {
        // Implémentation si nécessaire
    }
}
