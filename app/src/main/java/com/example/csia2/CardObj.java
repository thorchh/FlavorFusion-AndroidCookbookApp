package com.example.csia2;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;

public class CardObj implements Parcelable {

    private LayoutInflater layoutInflater;
    private String title;
    private String desc;
    private Integer img;

    CardObj(String title, String desc, Integer img){

        this.title = title;
        this.desc = desc;
        this.img = img;
    }

    protected CardObj(Parcel in) {
        title = in.readString();
        desc = in.readString();
        if (in.readByte() == 0) {
            img = null;
        } else {
            img = in.readInt();
        }
    }

    public static final Creator<CardObj> CREATOR = new Creator<CardObj>() {
        @Override
        public CardObj createFromParcel(Parcel in) {
            return new CardObj(in);
        }

        @Override
        public CardObj[] newArray(int size) {
            return new CardObj[size];
        }
    };

    String getTitle(){
        return title;
    }

    String getDesc(){
        return desc;
    }

    Integer getImg(){return img;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(desc);
        if (img == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(img);
        }
    }
}
