package com.example.testfilestorage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	TextView mTextView = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Write("helloworld!1111111");
//        Log.v("hello", Read()+" ");
        //test sqlite
        ListDatabaseManager.Init(this);
    	ListDatabaseManager.DeleteAllRow();
        MusicSong musicSong = new MusicSong();
        musicSong.SetAlbum("album");
        musicSong.SetArts("my");
        musicSong.SetBookmark(0);
        musicSong.SetDisplayName("displayname");
        musicSong.SetPath("/data/data/hello.mp3");
        musicSong.SetStorageType(1);
        musicSong.SetTitle("title");
        ListDatabaseManager.InsertMusicToDb(musicSong);
        
        mTextView = (TextView)findViewById(R.id.button);
        mTextView.setOnClickListener(this);
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	Log.v("hello", "onDestroy");
    	ListDatabaseManager.DeleteTable();
    	ListDatabaseManager.DeleteAllRow();
    	ListDatabaseManager.Release();
    }
    
    ////////////////////////SQlite//////////////////////////////
    
    @Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
		case R.id.button:
			////////test sqlite///////
//	        MusicSong musicSong = new MusicSong();
//	        musicSong.SetAlbum("222");
//	        musicSong.SetArts("222");
//	        musicSong.SetBookmark(0);
//	        musicSong.SetPath("/data/data/222.mp3");
//	        musicSong.SetStorageType(1);
//	        musicSong.SetTitle("222");
//			ListDatabaseManager.UpdateMusicToDb(musicSong);
//			
//			List<MusicSong> musicSongs = new ArrayList<MusicSong>();
//			ListDatabaseManager.BeginMusicTransaction();
//			ListDatabaseManager.QueryMusicListFromDb(1, musicSongs, false);
//			ListDatabaseManager.EndMusicTransaction(true);
//			for(int i = 0;i<musicSongs.size();i++){
//				MusicSong musicSong1 = musicSongs.get(i);
//				Log.v("hello", " "+musicSong1.GetPath());
//			}
			
			///////test contentProvider////
			///////////insert//////////////
			//Test ContentProvider
			ContentValues values = new ContentValues();
			values.put("_displayname", "3333");
			values.put("_path", "3333");
			values.put("_tile", "3333");
			values.put("_art", "3333");
			values.put("_album", "3333");
			values.put("_duration", 3333);
			values.put("_bookmark", 100);
			//在ContentResolver对象与ContentProvider进行交互时，通过URI确定要访问的ContentProvider数据集。
			//在发起的一个请求的过程中，Android系统根据URI确定处理这个查询的ContentProvider
			//所以要在AndroidManifest.xml中对MusicProvider进行配置
            Uri newUri = getContentResolver().insert(MusicProvider.CONTENT_URI, values);
            Log.v("hello", "after insert new uri:"+newUri);
            //////////query///////////
            Cursor cursor = getContentResolver().query(MusicProvider.CONTENT_URI,
                    new String[]{"_path","_art"},null, null, null); //CONTENT_URI多个查询
            if (cursor == null) {
				Log.v("hello", "contentProvider query error");
			}
            if (cursor.moveToFirst()) {
				do {
					Log.v("hello", "_path = "+cursor.getString(cursor.getColumnIndex("_path")));
				} while (cursor.moveToNext());
			}
            
            ////////////delete//////////
            //getContentResolver().delete(MusicProvider.CONTENT_URI, null, null);
            
            ////////////update//////////
			ContentValues values1 = new ContentValues();
			values1.put("_displayname", "44444");
			values1.put("_path", "44444");
			values1.put("_tile", "44444");
			values1.put("_art", "4444");
			values1.put("_album", "44444");
			values1.put("_duration", 444);
			values1.put("_bookmark", 444);
			Uri uri = Uri.parse(MusicProvider.CONTENT_URI_STRING + "/" + 1);
			int result = getContentResolver().update(uri, values1, null, null);
			break;

		default:
			break;
		}
    }
    
    ////////////////////////////////文件存储/////////////////////////////
    public String Read(){
    	FileInputStream fileInputStream = null;
    	String Ret = null;
    	try {
			    fileInputStream = openFileInput("message.txt");
			if (fileInputStream!=null) {
				byte[] buffer = new byte[1024];
				int readCount = 0;
				StringBuilder sb = new StringBuilder();
				//将这个流中的字节缓冲到数组b中，返回的这个数组中的字节个数，这个缓冲区没有满的话，则返回真实的字节个数，到未尾时都返回-1
				while((readCount = fileInputStream.read(buffer)) != -1){
					sb.append(new String(buffer,0,readCount));
				}
				
				Ret =  sb.toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    	return Ret;
    }
    
    
    /*MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，
     * 在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中可以使用Context.MODE_APPEND
    MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
    MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；
    MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入*/
    public void Write(String msg){
    	if (msg == null) {
			return;
		}
    	FileOutputStream fileOutputStream = null;
    	try {
			fileOutputStream  = openFileOutput("message.txt", MODE_PRIVATE); //如果文件不存在会在data/data/file目录下创建
			if (fileOutputStream!=null) {
				fileOutputStream.write(msg.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (fileOutputStream!=null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
    ////////////////////////文件存储///////////////////////////////  
}
