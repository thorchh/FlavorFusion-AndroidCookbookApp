package com.example.csia2;

import android.view.LayoutInflater;

public class CardObj {

    private LayoutInflater layoutInflater;
    private String title;
    private String desc;
    private Integer img;

    CardObj(String title, String desc, Integer img){

        this.title = title;
        this.desc = desc;
        this.img = img;
    }

    String getTitle(){
        return title;
    }

    String getDesc(){
        return desc;
    }

    Integer getImg(){return img;}

}
