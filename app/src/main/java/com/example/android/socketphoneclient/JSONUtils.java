package com.example.android.socketphoneclient;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public final class JSONUtils {
    private static final String TAG = JSONUtils.class.getSimpleName();

    /**
     * Helper method to get the String value of a key into a JSON object.
     *
     * @param jsonObject is the JSON object that is being parsed.
     * @param key        is the key
     *                   to search into the JSON object.
     * @return the String value associated to the given key, or an empty String if the key does not
     * exist.
     */
    static public String getStringFromJSON(JSONObject jsonObject, String key) {
        String value = "";
        if (!jsonObject.isNull(key)) try {
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            // If an error is thrown when executing the "try" block, catch the exception here, so
            // the app doesn't crash. Print a log message with the message from the exception.
            Log.e(TAG, "(getStringFromJSON) Error parsing JSON response: ", e);
        }
        return value;
    }

    /**
     * Helper method to get the int value of a key into a JSON object.
     *
     * @param jsonObject is the JSON object that is being parsed.
     * @param key        is the key to search into the JSON object.
     * @return the int value associated to the given key, or 0 if the key does not exist.
     */
    static public int getIntFromJSON(JSONObject jsonObject, String key) {
        int value = 0;
        if (!jsonObject.isNull(key)) try {
            value = jsonObject.getInt(key);
        } catch (JSONException e) {
            // If an error is thrown when executing the "try" block, catch the exception here, so
            // the app doesn't crash. Print a log message with the message from the exception.
            Log.e(TAG, "(getIntFromJSON) Error parsing JSON response: ", e);
        }
        return value;
    }

    /**
     * Helper method to get the boolean value of a key into a JSON object.
     *
     * @param jsonObject is the JSON object that is being parsed.
     * @param key        is the key to search into the JSON object.
     * @return the boolean value associated to the given key, or false if the key does not exist.
     */
    static public boolean getBooleanFromJSON(JSONObject jsonObject, String key) {
        boolean value = false;
        if (!jsonObject.isNull(key)) try {
            value = jsonObject.getBoolean(key);
        } catch (JSONException e) {
            // If an error is thrown when executing the "try" block, catch the exception here, so
            // the app doesn't crash. Print a log message with the message from the exception.
            Log.e(TAG, "(getBooleanFromJSON) Error parsing JSON response: ", e);
        }
        return value;
    }

    /**
     * Helper method to get the Double value of a key into a JSON object.
     *
     * @param jsonObject is the JSON object that is being parsed.
     * @param key        is the key to search into the JSON object.
     * @return the Double value associated to the given key, or 0.0 if the key does not exist.
     */
    static public Double getDoubleFromJSON(JSONObject jsonObject, String key) {
        Double value = 0.0;
        if (!jsonObject.isNull(key)) try {
            value = jsonObject.getDouble(key);
        } catch (JSONException e) {
            // If an error is thrown when executing the "try" block, catch the exception here, so
            // the app doesn't crash. Print a log message with the message from the exception.
            Log.e(TAG, "(getBooleanFromJSON) Error parsing JSON response: ", e);
        }
        return value;
    }
}
