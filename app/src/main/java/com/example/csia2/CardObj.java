package com.example.csia2;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;

public class CardObj implements Parcelable {

    private LayoutInflater layoutInflater;
    private String title;
    private String desc;
    private String img;
    private String colourTag;

    CardObj(String title, String desc, String img, String colourTag){

        this.title = title;
        this.desc = desc;
        this.img = img;
        this.colourTag = colourTag;
    }

    protected CardObj(Parcel in) {
        title = in.readString();
        desc = in.readString();
        if (in.readByte() == 0) {
            img = null;
        } else {
            img = in.readString();
        }
        colourTag = in.readString();
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

    String getImg(){return img;}

    String getColourTag() {return colourTag;}

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
            dest.writeString(img);
        }
        dest.writeString(colourTag);
    }
}
