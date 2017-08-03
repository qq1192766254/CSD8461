package com.example.testfilestorage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class MusicProvider extends ContentProvider {

	private static final String TAG = "hhq_PeopleProvider";
	private DatabaseHelper mDatabaseHelper = null;
	private SQLiteDatabase db = null;

	private static final String DB_TABLE = "USB1";

	//UriMatcher
	public static final String AUTHORITY = "com.example.MusicProvider";
	public static final String MINE_TYPE_SINGLE = "ITEM";//把整个Music数据库看成是Music根URI,primary key 看成是URI下的子项
	public static final String MINE_TYPE_MULTIPLE = "DIR";
	public static final String PATH_SINGLE = "Music/#";
	public static final String PATH_MULTIPLE = "Music";  
	public static final String CONTENT_URI_STRING = "content://" + AUTHORITY+ "/" + PATH_MULTIPLE;//content://<authority>/<dataPath>/<id> URI格式
	public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
	private static final int MULTIPLE_MUSIC = 1;//多个MusicSong的URI
	private static final int SINGLE_MUSIC = 2;//单个MusicSong的URI
	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, PATH_MULTIPLE,MULTIPLE_MUSIC);
		uriMatcher.addURI(AUTHORITY, PATH_SINGLE, SINGLE_MUSIC);
	}

	@Override
	public boolean onCreate() {
		Log.v(TAG, "onCreate");
		ListDatabaseManager.Init(getContext());
		mDatabaseHelper = ListDatabaseManager.GetDb();
		if (mDatabaseHelper == null) {
			Log.v(TAG, "onCreate false");
			return false;
		}
		db = mDatabaseHelper.getWritableDatabase();
		return true;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.v(TAG, "uri:"+uri.toString()+" selection:"+selection+" sortOrder:"+sortOrder);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(DB_TABLE);
		Log.v(TAG, "uriMatcher:"+uriMatcher.match(uri));
		switch (uriMatcher.match(uri)) {
		case SINGLE_MUSIC: //单个查询
			qb.appendWhere(DatabaseHelper.TBL_COLUMN_ID+"="+uri.getPathSegments().get(1));
			break;
		case MULTIPLE_MUSIC://多个查询
			break;
		default:
			Log.v(TAG, "query error unkown uri");
			break;
		}
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		Log.v(TAG, "________getType");
		Log.v(TAG, "uriMatcher:"+uriMatcher.match(uri));
		switch (uriMatcher.match(uri)) {
		case SINGLE_MUSIC:
			return MINE_TYPE_SINGLE;
		case MULTIPLE_MUSIC:
			return MINE_TYPE_MULTIPLE;
		default:
			Log.v(TAG, "getType error unkown uri");
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.v(TAG, "insert:uri = "+uri.toString()+" values:"+values);
		long id = db.insert(DB_TABLE, null, values);
		if (id > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
			Log.v(TAG, "insert newUri:"+newUri.toString());
			//更新新的URI
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		Log.v(TAG, "insert error");
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.v(TAG, "delete uri:"+uri.toString());
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case MULTIPLE_MUSIC:
			count = db.delete(DB_TABLE, selection, selectionArgs);
			break;
		case SINGLE_MUSIC:
			String segment = uri.getPathSegments().get(1);
			count = db.delete(DB_TABLE, DatabaseHelper.TBL_COLUMN_ID + "=" + segment,
					selectionArgs);
			break;
		default:
			Log.v(TAG, "delete error unkown uri");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.v(TAG, "update: uri = "+uri.toString());
		int count;
		switch (uriMatcher.match(uri)) {
		case MULTIPLE_MUSIC:
			count = db.update(DB_TABLE, values, selection, selectionArgs);
			break;
		case SINGLE_MUSIC:
			String segment = uri.getPathSegments().get(1);//获取传入的更新单个MusicSong的参数 前面传了1
			count = db.update(DB_TABLE, values, DatabaseHelper.TBL_COLUMN_ID + "=" + segment,
					selectionArgs);
			break;
		default:
			Log.v(TAG, "update error unkown uri");
			break;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return 0;
	}
}