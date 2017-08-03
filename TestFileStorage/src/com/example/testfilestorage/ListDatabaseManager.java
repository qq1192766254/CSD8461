package com.example.testfilestorage;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

public class ListDatabaseManager {

	private static final String TAG = "hhq_listDatabaseManager";

	private static final String DB_MUSIC_NAME = "Music.db";

	private static DatabaseHelper mMyMusicDb = null;
	public static String[] mTblNames = { "SD", "USB1", "USB2", "FLASH" };

	public static void Init(Context c) {

		if (mMyMusicDb == null) {
			mMyMusicDb = new DatabaseHelper(c, DB_MUSIC_NAME);

			for (int i = 0; i < mTblNames.length; i++) {
				mMyMusicDb.createTable(mTblNames[i]);
			}
		}
	}

	public static DatabaseHelper GetDb(){
		return mMyMusicDb;
	}
	
	public static void Release() {
		if (mMyMusicDb != null) {
			mMyMusicDb.close();
			mMyMusicDb = null;
		}
	}
	
	/**
	 * 解决在批量插入数据时导致应用缓慢的问题
	 */
	public static void BeginMusicTransaction() {
		if (mMyMusicDb != null) {
			mMyMusicDb.BeginTransaction();
			Log.w(TAG, "ListDatabaseManager__Music___BeginTransaction");
		}
	}

	public static void EndMusicTransaction(boolean bSuccessful) {
		if (mMyMusicDb != null) {
			mMyMusicDb.EndTransaction(bSuccessful);
			Log.w(TAG, "ListDatabaseManager__Music___EndTransaction");
		}
	}
	
	/**
	 * 删除表
	 */
	public static void DeleteTable(){
		if (mMyMusicDb!=null) {	
			mMyMusicDb.deleteTable("SD");
		}
	}

	/**
	 * 删除行
	 */
	public static void DeleteAllRow(){
		if (mMyMusicDb!=null) {
			mMyMusicDb.deleteTableConent("USB1");
		}
	}
	
	/**
	 * 向数据库中插入数据
	 */
	public static void InsertMusicToDb(MusicSong musicSong) {
		if (mMyMusicDb == null) {
			return;
		}

		long lasttime = SystemClock.elapsedRealtime();

		if (musicSong.GetPath() != null && musicSong.GetPath().length() > 0) {
			ContentValues values = new ContentValues();
			
			values.put(mMyMusicDb.TBL_COLUMN_ALBUM, musicSong.GetAlbum());
			values.put(mMyMusicDb.TBL_COLUMN_ART, musicSong.GetArts());
			values.put(mMyMusicDb.TBL_COLUMN_BOOKMARK, 0);
			values.put(mMyMusicDb.TBL_COLUMN_DISPLAY_NAME, "displayname");
			values.put(mMyMusicDb.TBL_COLUMN_DURATION, 100);
			values.put(mMyMusicDb.TBL_COLUMN_TILE, musicSong.GetTitle());
			values.put(mMyMusicDb.TBL_COLUMN_PATH, musicSong.GetPath());
			
			mMyMusicDb.insert(mTblNames[musicSong.GetStorageType()], values);
			Log.v(TAG, "AddMusicToDB time = "+(SystemClock.elapsedRealtime()-lasttime));
		}
	}
	
	/**
	 * 更新数据库操作
	 */
	public static void UpdateMusicToDb(MusicSong musicSong) {
		if (mMyMusicDb == null) {
			return;
		}
		
		long lasttime = SystemClock.elapsedRealtime();
		
		if (musicSong.GetPath() != null && musicSong.GetPath().length() > 0) {
			ContentValues values = new ContentValues();
			values.put(mMyMusicDb.TBL_COLUMN_ALBUM, musicSong.GetAlbum());
			values.put(mMyMusicDb.TBL_COLUMN_ART, musicSong.GetArts());
			values.put(mMyMusicDb.TBL_COLUMN_BOOKMARK, 0);
			values.put(mMyMusicDb.TBL_COLUMN_DISPLAY_NAME, "displayname");
			values.put(mMyMusicDb.TBL_COLUMN_DURATION, 100);
			values.put(mMyMusicDb.TBL_COLUMN_TILE, musicSong.GetTitle());
			values.put(mMyMusicDb.TBL_COLUMN_PATH, musicSong.GetPath());
			
			String selection = mMyMusicDb.TBL_COLUMN_PATH + " like ?"; //修改条件根据路径like ...
			String[] selectionArgs = {  musicSong.GetPath()+"%" };
			mMyMusicDb.Update(mTblNames[musicSong.GetStorageType()], values,
					selection, selectionArgs);
//			mMyMusicDb.Update(mTblNames[musicSong.GetStorageType()], values,
//					null, null);
		}
	}
	
	/**
	 * 查询数据库
	 */
	public static void QueryMusicListFromDb(int Storage, List<MusicSong> MusicList,
			boolean bExit) {
		if (mMyMusicDb == null) {
			return;
		}
		
		MusicList.clear();
		Cursor lCursor = mMyMusicDb.query(mTblNames[Storage], null, null); //查询全部 column:列
		
		if (lCursor == null || lCursor.getCount() <= 0) {
			return;
		}

		String Title = null;
		String Arts = null;
		String Path = null;
		String DisplayName = null;
		String Album = null;
		int Time = 0;
		int Bookmark = 0;

		if (lCursor.moveToFirst()) { //判断游标是否为空
			do {
				MusicSong m = new MusicSong();
				DisplayName = lCursor.getString(lCursor
						.getColumnIndex(mMyMusicDb.TBL_COLUMN_DISPLAY_NAME)); //查询某一列
				Title = lCursor.getString(lCursor
						.getColumnIndex(mMyMusicDb.TBL_COLUMN_TILE));
				Arts = lCursor.getString(lCursor
						.getColumnIndex(mMyMusicDb.TBL_COLUMN_ART));
				Album = lCursor.getString(lCursor
						.getColumnIndex(mMyMusicDb.TBL_COLUMN_ALBUM));
				Path = lCursor.getString(lCursor
						.getColumnIndex(mMyMusicDb.TBL_COLUMN_PATH));
				Time = lCursor.getInt(lCursor
						.getColumnIndexOrThrow(mMyMusicDb.TBL_COLUMN_DURATION));
				Bookmark = lCursor.getInt(lCursor
						.getColumnIndexOrThrow(mMyMusicDb.TBL_COLUMN_BOOKMARK));
				
				if (Title != null) {
					m.SetTitle(Title);
				}
				
				if (Arts != null) {
					m.SetArts(Arts);
				}
				
				if (Album != null) {
					m.SetAlbum(Album);
				}
				
				if (Path != null) {
					m.SetPath(Path);
				}
				
				if (DisplayName != null) {
					m.SetDisplayName(DisplayName);
				}

				m.SetStorageType(Storage);
				m.SetTime(Time);
				m.SetBookmark(Bookmark);
				MusicList.add(m);

			} while (lCursor.moveToNext() && !bExit);
		}
		
		if (lCursor != null) {
			lCursor.close();
		}
	}
}
