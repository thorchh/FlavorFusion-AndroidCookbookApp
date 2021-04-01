package com.example.csia2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<String> title = new ArrayList<String>(20);
    private List<Integer> img = new ArrayList<Integer>(20);
    private List<String> desc = new ArrayList<String>(20);

    private ArrayList<CardObj> cardObjList;
    private ArrayList<CardObj> cardObjListFull;

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


    Adapter(Context context,ArrayList<CardObj> objList){
        cardObjList = objList;
        cardObjListFull = new ArrayList<>(objList);
        for (int i = 0; i< cardObjList.size(); i++){
            this.title.add(cardObjList.get(i).getTitle());
            this.desc.add((cardObjList.get(i)).getDesc());
            this.img.add((cardObjList.get(i)).getImg());
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

        for (int j = 0; j< cardObjList.size(); j++){
            title.add(cardObjList.get(j).getTitle());
            desc.add((cardObjList.get(j)).getDesc());
            img.add((cardObjList.get(j)).getImg());
        }

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

    @Override
    public Filter getFilter(){
        return filter;
    }
    private Filter filter = new Filter(){


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CardObj> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(cardObjListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CardObj obj: cardObjListFull){
                    if (obj.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(obj);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            System.out.println("performFiltering");
            System.out.println("performFiltering");
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cardObjList.clear();
            cardObjList.addAll((List) results.values);
            for (CardObj i:cardObjList){
                System.out.println(i);
            }
            notifyDataSetChanged();
            System.out.println("publishResults");
        }
    };


}
