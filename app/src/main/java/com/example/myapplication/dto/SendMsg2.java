package com.example.myapplication.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class SendMsg2 implements Parcelable {
    String title;
    String content;

    public SendMsg2(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public SendMsg2(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SendMsg2> CREATOR = new Creator<SendMsg2>() {
        @Override
        public SendMsg2 createFromParcel(Parcel in) {
            return new SendMsg2(in);
        }

        @Override
        public SendMsg2[] newArray(int size) {
            return new SendMsg2[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
