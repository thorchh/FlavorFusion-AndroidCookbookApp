package com.example.csia2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> title;
    private List<Integer> img;
    private List<String> desc;
    private ArrayList<Object> cardObjList;


    Adapter(Context context,ArrayList<CardObj> objList){
        for (int i = 0; i< objList.size(); i++){

            this.title.add(objList.get(i).getTitle());
            this.desc.add(objList.get(i).getDesc());
            this.img.add(objList.get(i).getImg());
        }
        this.layoutInflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        String titleb = title.get(i);
        viewHolder.textTitle.setText(titleb);

        String descb = desc.get(i);
        viewHolder.textDescription.setText(descb);

        Integer imgb = img.get(i);
        viewHolder.imageViewf.setImageResource(imgb);

    }



    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textDescription;
        ImageView imageViewf;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageViewf = itemView.findViewById(R.id.imageView);
        }
    }
}
