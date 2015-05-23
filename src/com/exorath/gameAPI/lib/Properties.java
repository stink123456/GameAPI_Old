package com.exorath.gameAPI.lib;

import java.util.HashMap;

//TODO: CHILDS ARE INTERPRETED WRONGLY! Remove all child stuff. Childs shouldn't be diggable.
/**
 * Created by too on 23/05/2015.
 * Properties are used to load and save certain settings within a session.
 */

public class Properties {
    private HashMap<String, Object> properties = new HashMap<String, Object>();
    private HashMap<String,Properties> childs = new HashMap<String,Properties>();

    public Properties() {

    }

    /**
     *
     * @param key   The property key
     * @return      Returns the value out of this or out of the childs properties or null if value not found.
     */
    public Object get(String key) {
        return get(key, null);
    }

    /**
     *
     * @param key   The property key
     * @param def   Default value to return if key not found
     * @return      Returns the value out of this or out of the childs properties or def if value not found.
     */
    public Object get(String key, Object def) {
        if(key.contains("."))
            return getChild(key).get(key.split(".")[1], def);

        if (properties.containsKey(key))
            return properties.get(key);
        else return def;
    }

    /**
     * Set a property
     * @param key The key you want to store the value in
     * @param value The value you want to store in the key.
     */
    public void set(String key, Object value){
        if(key.contains("."))
            getChild(key).set(key.split(".")[1], value);
        else
            properties.put(key,value);
    }

    /**
     *
     * @param key the key entered in {@link #get(String key, Object def) get} method.
     * @return If never used before new child properties object. If it already exist, return from childs HashMap.
     */
    private Properties getChild(String key){
        String childName = key.split(".")[0];

        if(childs.containsKey(childName))
            return childs.get(childName);

        Properties child = new Properties();
        childs.put(childName, child);
        return child;
    }
}
