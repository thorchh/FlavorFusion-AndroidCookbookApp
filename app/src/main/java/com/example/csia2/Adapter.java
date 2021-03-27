package com.example.csia2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<String> title = new ArrayList<String>(20);
    private List<Integer> img = new ArrayList<Integer>(20);
    private List<String> desc = new ArrayList<String>(20);
    private ArrayList<Object> cardObjList;
    private ArrayList<CardObj> cardObjListFull;


    Adapter(Context context,ArrayList<CardObj> objList){
        for (int i = 0; i< objList.size(); i++){
            // System.out.println(objList.get(i).getTitle());
            this.title.add(objList.get(i).getTitle());
            this.desc.add((objList.get(i)).getDesc());
            this.img.add((objList.get(i)).getImg());
        }
        this.layoutInflater = LayoutInflater.from(context);
        cardObjListFull = new ArrayList<CardObj>(objList);


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

    @Override
    public Filter getFilter(){
        return filter;
    }
    private Filter filter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Object> filteredList = new ArrayList<>();

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

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cardObjList.clear();
            cardObjList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
