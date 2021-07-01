package com.example.csia2;

import android.content.Context;
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

    private LayoutInflater layoutInflater;

    private ArrayList<CardObj> cardObjList;
    private ArrayList<CardObj> cardObjListFull;
    private ArrayList<String> check;
    String finalCheck = "";
    private OnNoteListener mOnNoteListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textTitle, textDescription,colourTagText;
        ImageView imageViewf;
        OnNoteListener onNoteListener;
        private int[] colorArray;


        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.recipeTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageViewf = itemView.findViewById(R.id.imageView);
            colourTagText = itemView.findViewById(R.id.colourTag);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);

    }


    Adapter(Context context,ArrayList<CardObj> objList, OnNoteListener onNoteListener){
        this.mOnNoteListener = onNoteListener;
        cardObjList = objList;
        cardObjListFull = new ArrayList<>(objList);

        this.layoutInflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, viewGroup, false);
        return new ViewHolder(view, mOnNoteListener);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        String titleb = cardObjList.get(i).getTitle();
        viewHolder.textTitle.setText(titleb);

        String descb = cardObjList.get(i).getDesc();
        viewHolder.textDescription.setText(descb);

        String imgb = cardObjList.get(i).getImg();
        Picasso.get().load(imgb).into(viewHolder.imageViewf);

        String colourb = cardObjList.get(i).getColourTag();

        Context context  = viewHolder.colourTagText.getContext();
        int[] colorArray= context.getResources().getIntArray(R.array.array_name);
        for (int b = 0; b < colorArray.length; b++) {
            if (colourb.equals("Red")){
                viewHolder.colourTagText.setBackgroundColor(colorArray[0]);
            }
            else if (colourb.equals("blue")){
                viewHolder.colourTagText.setBackgroundColor(colorArray[1]);
            }
            else if (colourb.equals("Green")){
                viewHolder.colourTagText.setBackgroundColor(colorArray[2]);
            }
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

    private Filter filter = new Filter(){


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CardObj> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(cardObjListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CardObj obj: cardObjListFull){
                    check = obj.getIngridientsChecklist().get(0);
                    for (int i = 0; i< check.size(); i++){
                        finalCheck+=(check.get(i));
                    }
                    if (obj.getTitle().toLowerCase().contains(filterPattern) || obj.getDesc().toLowerCase().contains(filterPattern)||finalCheck.contains(filterPattern)){
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
