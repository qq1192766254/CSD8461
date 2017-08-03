package com.example.testfilestorage;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicSong implements Parcelable { //AIDL用
	private boolean mValid = true;

	private String mDisplayname;
	private String mTitle;
	private String mArts;
	private String mAlbum;
	private String mGenre;
	private String mAlbumKey;
	private String mPath;
	private String mParent = null; // 文件夹也是MusicSong对象
	private boolean bDirStatus = false;

	private int mStorageType = 1;

	private long mSize = 0;
	private long mTime = 0;
	private int mAlbumId = 0;
	private int mArtsId = 0;
	private int mSongid = 0;
	private int mBookmark = 0;

	public boolean isValid() {
		return mValid;
	}

	public void setValid(boolean valid) {
		mValid = valid;
	}

	public int GetBookmark() {
		return mBookmark;
	}

	public void SetBookmark(int Bookmark) {
		this.mBookmark = Bookmark;
	}

	public int GetArtsId() {
		return mArtsId;
	}

	public void SetArtsId(int Artsid) {
		this.mArtsId = Artsid;
	}

	public int GetSongId() {
		return mSongid;
	}

	public void SetSongId(int songid) {
		this.mSongid = songid;
	}

	public int GetStorageType() {
		return mStorageType;
	}

	public void SetStorageType(int Type) {
		this.mStorageType = Type;
	}

	public int GetAlbumId() {
		return mAlbumId;
	}

	public void SetAlbumId(int albumid) {
		this.mAlbumId = albumid;
	}

	public void SetDisplayName(String name) {
		this.mDisplayname = name;
	}

	public String GetDisplsyName() {
		return mDisplayname;
	}

	public String GetTitle() {
		return mTitle;
	}

	public void SetTitle(String title) {
		this.mTitle = title;
	}

	public String GetArts() {
		return mArts;
	}

	public void SetArts(String singer) {
		this.mArts = singer;
	}

	public String GetAlbum() {
		return mAlbum;
	}

	public void SetAlbum(String album) {
		this.mAlbum = album;
	}

	public void SetGenre(String genre) {
		this.mGenre = genre;
	}

	public String GetGenre() {
		return mGenre;
	}

	public String GetPath() {
		return mPath;
	}

	public void SetPath(String Path) {
		this.mPath = Path;
	}

	public String GetAlbumKey() {
		return mAlbumKey;
	}

	public void SetAlbumKey(String key) {
		this.mAlbumKey = key;
	}

	public long GetSize() {
		return mSize;
	}

	public void SetSize(long size) {
		this.mSize = size;
	}

	public long GetTime() {
		return mTime;
	}

	public void SetTime(long time) {
		this.mTime = time;
	}

	// /////////////////////////////////////////////////////xunfei/////////////////////////////////////////
	public MusicSong() {
		mDisplayname = new String();
		mTitle = new String();
		mArts = new String();
		mAlbum = new String();
		mGenre = new String();
		mAlbumKey = new String();
		mPath = new String();
	}

	public MusicSong(Parcel source) {
		mDisplayname = source.readString();
		mTitle = source.readString();
		mArts = source.readString();
		mAlbum = source.readString();
		mGenre = source.readString();
		mAlbumKey = source.readString();
		mPath = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mDisplayname);
		dest.writeString(mTitle);
		dest.writeString(mArts);
		dest.writeString(mAlbum);
		dest.writeString(mGenre);
		dest.writeString(mAlbumKey);
		dest.writeString(mPath);
	}

	public static final Parcelable.Creator<MusicSong> CREATOR = new Parcelable.Creator<MusicSong>() {

		@Override
		public MusicSong createFromParcel(Parcel source) {
			return new MusicSong(source);
		}

		@Override
		public MusicSong[] newArray(int size) {
			return new MusicSong[size];
		}
	};

	// add by hhq for 增加喜爱歌曲记忆功能
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o != null && o.getClass() == MusicSong.class) {
			MusicSong musicSong = (MusicSong) o;
			if (musicSong.GetPath().equals(this.GetPath())) {
				return true;
			}
		}
		return false;
	}

	// ///////文件夹部分/////////////
	public boolean GetDirStatus() {
		return bDirStatus;
	}

	public void SetDirStatus(boolean bStatus) {
		this.bDirStatus = bStatus;
	}

	public void SetmParent(String Parent) {
		this.mParent = Parent;
	}

	public String GetParent() {
		return mParent;
	}
}
