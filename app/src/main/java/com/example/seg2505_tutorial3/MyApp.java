package com.example.seg2505_tutorial3;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Activer la persistance hors ligne
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
