package com.personal.case_study_promotions.model;

import java.util.concurrent.ConcurrentHashMap;

public class Node {
    private final String name;
    private final ConcurrentHashMap<String, Item> dataStore;

    public Node(String name) {
        this.name = name;
        this.dataStore = new ConcurrentHashMap<>();
    }

    public void put(String key, Item value) {
        dataStore.put(key, value);
    }

    public Item get(String key) {
        return dataStore.get(key);
    }

    public void clear() {
        dataStore.clear();
        System.out.println("Node " + name + " cleared");
    }

    @Override
    public String toString() {
        return "Node{" + "name='" + name + '\'' + '}';
    }
}
