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
			//��ContentResolver������ContentProvider���н���ʱ��ͨ��URIȷ��Ҫ���ʵ�ContentProvider���ݼ���
			//�ڷ����һ������Ĺ����У�Androidϵͳ����URIȷ�����������ѯ��ContentProvider
			//����Ҫ��AndroidManifest.xml�ж�MusicProvider��������
            Uri newUri = getContentResolver().insert(MusicProvider.CONTENT_URI, values);
            Log.v("hello", "after insert new uri:"+newUri);
            //////////query///////////
            Cursor cursor = getContentResolver().query(MusicProvider.CONTENT_URI,
                    new String[]{"_path","_art"},null, null, null); //CONTENT_URI�����ѯ
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
    
    ////////////////////////////////�ļ��洢/////////////////////////////
    public String Read(){
    	FileInputStream fileInputStream = null;
    	String Ret = null;
    	try {
			    fileInputStream = openFileInput("message.txt");
			if (fileInputStream!=null) {
				byte[] buffer = new byte[1024];
				int readCount = 0;
				StringBuilder sb = new StringBuilder();
				//��������е��ֽڻ��嵽����b�У����ص���������е��ֽڸ��������������û�����Ļ����򷵻���ʵ���ֽڸ�������δβʱ������-1
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
    
    
    /*MODE_PRIVATE��ΪĬ�ϲ���ģʽ��������ļ���˽�����ݣ�ֻ�ܱ�Ӧ�ñ�����ʣ�
     * �ڸ�ģʽ�£�д������ݻḲ��ԭ�ļ������ݣ���������д�������׷�ӵ�ԭ�ļ��п���ʹ��Context.MODE_APPEND
    MODE_APPEND��ģʽ�����ļ��Ƿ���ڣ����ھ����ļ�׷�����ݣ�����ʹ������ļ���
    MODE_WORLD_READABLE����ʾ��ǰ�ļ����Ա�����Ӧ�ö�ȡ��
    MODE_WORLD_WRITEABLE����ʾ��ǰ�ļ����Ա�����Ӧ��д��*/
    public void Write(String msg){
    	if (msg == null) {
			return;
		}
    	FileOutputStream fileOutputStream = null;
    	try {
			fileOutputStream  = openFileOutput("message.txt", MODE_PRIVATE); //����ļ������ڻ���data/data/fileĿ¼�´���
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
    ////////////////////////�ļ��洢///////////////////////////////  
}
