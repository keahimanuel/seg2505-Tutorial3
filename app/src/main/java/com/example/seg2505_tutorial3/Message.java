package com.example.seg2505_tutorial3;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Message {
    public String key;   // Clé unique du message
    public String uid;   // ID de l'utilisateur
    public String author;
    public String text;

    public Message() {
        // Constructeur par défaut requis pour les appels à DataSnapshot.getValue(Message.class)
    }

    public Message(String uid, String author, String text) {
        this.uid = uid;
        this.author = author;
        this.text = text;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("text", text);
        return result;
    }
}
