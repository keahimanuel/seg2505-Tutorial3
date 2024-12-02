package com.example.seg2505_tutorial3;

import java.util.Map;

public class User {
    public String name;
    public String email;
    public Map<String, Boolean> groups;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, Map<String, Boolean> groups) {
        this.name = name;
        this.email = email;
        this.groups = groups;
    }
}
