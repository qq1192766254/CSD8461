package com.example.testfilestorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "hhq_DatabaseHelper";
	private static final int mVersion = 1;
	SQLiteDatabase mDb = null;

	//���ݿ��� ����
	public static final String TBL_COLUMN_ID = "_id";  //������KEY ���Ӽ�¼���Զ�+1
	public static final String TBL_COLUMN_PATH = "_path";
	public static final String TBL_COLUMN_DISPLAY_NAME = "_displayname";
	public static final String TBL_COLUMN_BOOKMARK = "_bookmark";
	public static final String TBL_COLUMN_TILE = "_tile";
	public static final String TBL_COLUMN_ART = "_art";
	public static final String TBL_COLUMN_ALBUM = "_album";
	public static final String TBL_COLUMN_DURATION = "_duration";
	
	public DatabaseHelper(Context c, String DBName) {
		super(c, DBName, null, mVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(TAG, "onCreate");
	}

	public boolean createTable(String strTableName) {
		if (mDb == null) {
			try {
				mDb = getWritableDatabase(); //��ȡSqliteDatabase
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		if (mDb == null) {
			return false;
		}
		
		try {
			mDb.execSQL("create table if not exists " + strTableName + " ("
					+ TBL_COLUMN_ID + " INTEGER PRIMARY KEY,"
					+ TBL_COLUMN_DISPLAY_NAME + " TEXT ," 
					+ TBL_COLUMN_PATH+ " TEXT UNIQUE," 
					+ TBL_COLUMN_TILE + " TEXT,"
					+ TBL_COLUMN_ART + " TEXT," 
					+ TBL_COLUMN_ALBUM + " TEXT,"
					+ TBL_COLUMN_DURATION + " INTEGER," 
					+ TBL_COLUMN_BOOKMARK+ " INTEGER" 
					+ ");");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public void deleteTable(String strTableName) {
		if (mDb == null) {
			return;
		}
		
		try {
			mDb.execSQL("drop table if exists " + strTableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteTableConent(String strTableName) {
		if (mDb == null) {
			return;
		}
		
		try {
			mDb.delete(strTableName, null, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void BeginTransaction() {
		if (mDb != null) {
			try {
				if (!mDb.inTransaction()) {
					mDb.beginTransaction();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean IsInTransaction() {
		if (mDb != null) {
			return mDb.inTransaction();
		}
		
		return false;
	}

	public void EndTransaction(boolean bSuccessful) {
		if (mDb != null) {
			try {
				if (mDb.inTransaction()) {
					if (bSuccessful) {
						mDb.setTransactionSuccessful();//����������ɹ��������û��Զ��ع����ύ
					}
					mDb.endTransaction();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void insert(String strTableName, ContentValues values) {
		if (mDb != null) {
			try {
				mDb.replace(strTableName, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����SQLiteDatabase��update(String table,ContentValues values,String  whereClause, String[]  whereArgs)����
		����1  ������
		����2  ������ContentValues���͵ļ�ֵ��Key-Value
		����3  ����������where�־䣩
		����4  ������������
	 */
	public void Update(String strTableName, ContentValues values,
			String whereClause, String[] whereArgs) {
		if (mDb != null) {
			try {
				mDb.update(strTableName, values, whereClause, whereArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Cursor query(String strTableName, String selection,
			String[] selectionArgs) {
		if (mDb == null) {
			return null;
		}
		
		Cursor c = null;
		
		try {
			c = mDb.query(strTableName, null, selection, selectionArgs, null,
					null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return c;
	}

	public void del(String strTableName, String selection,
			String[] selectionArgs) {
		if (mDb != null) {
			try {
				mDb.delete(strTableName, selection, selectionArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(TAG, "onUpgrade");
	}
}
