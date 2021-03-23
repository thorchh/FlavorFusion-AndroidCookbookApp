package com.example.csia2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;
    private List<Integer> img;
    private List<String> desc;


    Adapter(Context context, List<String> data, List<String> desc, List<Integer> img){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.img = img;
        this.desc = desc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        String title = data.get(i);
        viewHolder.textTitle.setText(title);

        String descb = desc.get(i);
        viewHolder.textDescription.setText(descb);

        Integer imgb = img.get(i);
        viewHolder.imageViewf.setImageResource(imgb);

    }



    @Override
    public int getItemCount() {
        return data.size();
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
