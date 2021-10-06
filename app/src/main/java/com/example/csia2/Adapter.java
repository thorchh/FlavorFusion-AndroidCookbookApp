package com.example.csia2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.security.AccessController.getContext;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
    //initialise
    private LayoutInflater layoutInflater;
    private ArrayList<CardObj> cardObjList;
    private ArrayList<CardObj> cardObjListFull;
    private ArrayList<String> check;
    String finalCheck = "";
    private OnNoteListener mOnNoteListener;

    //ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //initialise
        TextView textTitle, textDescription,colourTagText;
        ImageView imageViewf;
        OnNoteListener onNoteListener;

        //ViewHolder constructor
        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.recipeTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageViewf = itemView.findViewById(R.id.imageView);
            colourTagText = itemView.findViewById(R.id.colourTag);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        //onClick method, returns adapterPosition
        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    //interface
    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    //onNoteClick gets Recipe object and starts activity with intent (passThrough and user are in .putExtra)
    //in MainHomeActivity.java


    //Adapter constructor
    Adapter(Context context,ArrayList<CardObj> objList, OnNoteListener onNoteListener){
        this.mOnNoteListener = onNoteListener;
        cardObjList = objList;
        cardObjListFull = new ArrayList<>(objList);
        this.layoutInflater = LayoutInflater.from(context);
    }

    //onCreateViewHolder method, returns created ViewHolder with previously made class + constructor
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate custom_view xml with viewGroup
        View view = layoutInflater.inflate(R.layout.custom_view, viewGroup, false);
        return new ViewHolder(view, mOnNoteListener);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //get title
        String titleb = cardObjList.get(i).getTitle();
        //set title
        viewHolder.textTitle.setText(titleb);

        //get description
        String descb = cardObjList.get(i).getDesc();
        //set description
        viewHolder.textDescription.setText(descb);

        //get img
        String imgb = cardObjList.get(i).getImg();
        //load img
        Picasso.get().load(imgb).into(viewHolder.imageViewf);

        //get colourTag
        String colourb = cardObjList.get(i).getColourTag();
        //get colourTag context
        Context context  = viewHolder.colourTagText.getContext();
        //get colorArray from res
        int[] colorArray= context.getResources().getIntArray(R.array.array_name);
        //loop through colorArray
        for (int b = 0; b < colorArray.length; b++) {
            //if colourTag == red
            if (colourb.equals("Red")){
                //set colourTag xml as predefined colour red
                viewHolder.colourTagText.setBackgroundColor(colorArray[0]); }
            //if colourTag == blue
            else if (colourb.equals("Blue")){
                //set colourTag xml as predefined colour blue
                viewHolder.colourTagText.setBackgroundColor(colorArray[2]); }
            //if colourTag == green
            else if (colourb.equals("Green")){
                //set colourTag xml as predefined colour green
                viewHolder.colourTagText.setBackgroundColor(colorArray[1]); }
        }

    }

    @Override
    public int getItemCount() {
        return cardObjList.size();
    }

    @Override
    public Filter getFilter(){
        return filter;
    }

    //create Filter Object
    private Filter filter = new Filter(){


        // implemented method performFiltering: filtering from search
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CardObj> filteredList = new ArrayList<>();
            //if no search
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(cardObjListFull);
            } else {
                //normalize constraint
                String filterPattern = constraint.toString().toLowerCase().trim();
                //loop through the cardObjList
                for (CardObj obj: cardObjListFull){
                    //get ingredient and not check
                    check = obj.getIngridientsChecklist().get(0);
                    //loop through ingredients
                    for (int i = 0; i< check.size(); i++){
                        //concatenate all ingredients into one string
                        finalCheck+=(check.get(i));
                    }
                    //check title, description and ingredients
                    if (obj.getTitle().toLowerCase().contains(filterPattern) || obj.getDesc().toLowerCase().contains(filterPattern)||finalCheck.contains(filterPattern)){
                        filteredList.add(obj);
                    }
                }
            }
            //results
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        // implemented method publishResults
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //clear cardObjList from previous search
            cardObjList.clear();
            //add new results to cardObjList
            cardObjList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
