package com.example.csia2;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    private String title;
    private String desc;
    private String img;
    // 0/5 to 5/5
    private Integer difficulty;
    // in minutes
    private Integer time;
    private Boolean saved;
    private String colourTag;
    private ArrayList<ArrayList> ingridientsChecklist;
    private float userRating;
    private ArrayList<String> instructionArrayList;
    private Long recipeID;

    //constructor
    public Recipe(String title, String desc, String img, Long difficulty, Long time, Boolean saved, String colourTag, ArrayList<ArrayList> ingridientsChecklist, Double userRating, ArrayList<String> instructionArrayList, Long recipeID){
        this.title = title;
        this.desc = desc;
        this.img = img;
        this.difficulty = difficulty.intValue();
        this.time = time.intValue();
        this.saved = saved;
        this.colourTag = colourTag;
        this.ingridientsChecklist = ingridientsChecklist;
        this.userRating = userRating.floatValue();
        this.instructionArrayList = instructionArrayList;
        this.recipeID = recipeID;
    }

    public Recipe(String title, String desc, String img, Integer difficulty, Integer time, Boolean saved, String colourTag, ArrayList<ArrayList> ingridientsChecklist, float userRating, ArrayList<String> instructionArrayList, Long recipeID){
        this.title = title;
        this.desc = desc;
        this.img = img;
        this.difficulty = difficulty;
        this.time = time;
        this.saved = saved;
        this.colourTag = colourTag;
        this.ingridientsChecklist = ingridientsChecklist;
        this.userRating = userRating;
        this.instructionArrayList = instructionArrayList;
        this.recipeID = recipeID;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Recipe(Parcel in) {
        title = in.readString();
        desc = in.readString();
        if (in.readByte() == 0) { img = null; } else { img = in.readString(); }
        if (in.readByte() == 0) { difficulty = null; } else { difficulty = in.readInt(); }
        if (in.readByte() == 0) { time = null; } else { time = in.readInt(); }
        saved = in.readBoolean();
        colourTag = in.readString();
        ingridientsChecklist = (ArrayList<ArrayList>) in.readSerializable();
        userRating = in.readFloat();
        instructionArrayList = (ArrayList<String>) in.readSerializable();
        recipeID = in.readLong();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(desc);
        if (img == null) { dest.writeByte((byte) 0); } else { dest.writeByte((byte) 1);dest.writeString(img); }
        if (difficulty == null) { dest.writeByte((byte) 0); } else { dest.writeByte((byte) 1);dest.writeInt(difficulty); }
        if (time == null) { dest.writeByte((byte) 0); } else { dest.writeByte((byte) 1);dest.writeInt(time); }
        dest.writeBoolean(saved);
        dest.writeString(colourTag);
        dest.writeSerializable(ingridientsChecklist);
        dest.writeFloat(userRating);
        dest.writeSerializable(instructionArrayList);
        dest.writeLong(recipeID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
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
    public String getImg(){return img;}
    public Integer getDifficulty() { return difficulty; }
    public Integer getTime() { return time; }
    public Boolean getSaved() {return saved; }
    public String getColourTag() { return colourTag; }
    public ArrayList<ArrayList> getingridientsChecklist() {return ingridientsChecklist; }
    public float getUserRating() {return userRating; }
    public ArrayList<String> getInstructionsArrayList() {return instructionArrayList; }
    public Long getRecipeID() { return recipeID; }


    //setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public void setDifficulty(Integer difficulty) { this.difficulty = difficulty; }
    public void setTime(Integer time) { this.time = time; }
    public void setIngridientsChecklist(ArrayList<ArrayList> ingridientsChecklist) { this.ingridientsChecklist = ingridientsChecklist; }
    public void setInstructionArrayList(ArrayList<String> instructionArrayList) { this.instructionArrayList = instructionArrayList; }

}
