package com.foloke.xmlview;

import android.os.Parcel;
import android.os.Parcelable;

public class ImportInfo implements Parcelable {
    public String id;
    public String imagePath;

    public ImportInfo() { }

    protected ImportInfo(Parcel in) {
        id = in.readString();
        imagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImportInfo> CREATOR = new Creator<ImportInfo>() {
        @Override
        public ImportInfo createFromParcel(Parcel in) {
            return new ImportInfo(in);
        }

        @Override
        public ImportInfo[] newArray(int size) {
            return new ImportInfo[size];
        }
    };
}
