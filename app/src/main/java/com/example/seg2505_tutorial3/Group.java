package com.example.seg2505_tutorial3;

import java.util.Map;

public class Group {
    public String name;
    public Map<String, Boolean> members;
    public String lastMessage;
    public long timestamp;

    // Constructeur par défaut requis pour DataSnapshot.getValue(Group.class)
    public Group() {
    }

    public Group(String name, Map<String, Boolean> members, String lastMessage, long timestamp) {
        this.name = name;
        this.members = members;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }
}
