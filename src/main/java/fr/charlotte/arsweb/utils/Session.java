package fr.charlotte.arsweb.utils;

import java.util.HashMap;

public class Session {

    private static final HashMap<String, Session> allSesssion = new HashMap<>();
    private final HashMap<String, Object> objectHashMap = new HashMap<>();

    String address;

    public static Session getSession(String address) {
        if (allSesssion.containsKey(address)) {
            return allSesssion.get(address);
        }
        Session s = new Session(address);
        allSesssion.put(address, s);
        return s;
    }

    private Session(String address) {
        this.address = address;
    }

    public void addKey(String key, Object value, boolean replace) {
        if (objectHashMap.containsKey(key)) {
            if (replace) {
                objectHashMap.remove(key);
            } else {
                return;
            }
        }
        objectHashMap.put(key, value);
    }

    public void addKey(String key, Object value) {
        addKey(key, value, true);
    }

    public Object getValue(String key) {
        if (!objectHashMap.containsKey(key))
            return "null";
        return objectHashMap.get(key);
    }

    @Override
    public String toString() {
        return objectHashMap.toString();
    }

    public void destroy() {
        Session.allSesssion.remove(this.address);
    }

}
