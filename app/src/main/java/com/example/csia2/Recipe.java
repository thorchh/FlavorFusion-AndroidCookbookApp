package com.example.csia2;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    private String title;
    private String desc;
    private Integer img;
    // 0-5/5
    private Integer difficulty;
    // in minutes
    private Integer time;


    public Recipe(String title, String desc, Integer img, Integer difficulty, Integer time) {
        this.title = title;
        this.desc = desc;
        this.img = img;
        this.difficulty = difficulty;
        this.time = time;
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        desc = in.readString();
        if (in.readByte() == 0) {
            img = null;
        } else {
            img = in.readInt();
        }
        if (in.readByte() == 0) {
            difficulty = null;
        } else {
            difficulty = in.readInt();
        }
        if (in.readByte() == 0) {
            time = null;
        } else {
            time = in.readInt();
        }
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
        if (difficulty == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(difficulty);
        }
        if (time == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(time);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    //getters
    public String getTitle() {
        return title;
    }
    public String getDesc() {
        return desc;
    }
    public Integer getImg(){return img;}
    public Integer getDifficulty() { return difficulty; }
    public Integer getTime() { return time; }

    //setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setImg(Integer img) {
        this.img = img;
    }
    public void setDifficulty(Integer difficulty) { this.difficulty = difficulty; }
    public void setTime(Integer time) { this.time = time; }
}
