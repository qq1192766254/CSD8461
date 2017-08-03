package com.example.testfilestorage;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
	private Context context;
	private final static String PREFERENCE_FILE_NAME = "Can";

	public SharedPreferencesUtil(Context context) {
		this.context = context;
	}

	public boolean readPreferences(String key, boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		boolean tmp = preferences.getBoolean(key, value);
		return tmp;
	}

	public int readPreferences(String key, int value) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		int tmp = preferences.getInt(key, value);
		return tmp;
	}

	public String readPreferences(String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		String text = preferences.getString(key, value);
		return text;
	}

	public Set<String> readPreferences(String key, Set<String> value) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		Set<String> set = preferences.getStringSet(key, value);
		return set;
	}

	public void writePreferences(String key, int value) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void writePreferences(String key, boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void writePreferences(String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void writePreferences(String key, Set<String> value) {
		SharedPreferences preferences = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putStringSet(key, value);
		editor.commit();
	}
}
