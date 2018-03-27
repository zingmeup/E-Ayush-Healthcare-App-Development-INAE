package com.example.akhil.e_ayush.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.akhil.e_ayush.R;

/**
 * Created by Akhil on 25-02-2018.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder>{

    private final String[] array;
    private final Context context;
    public int lastSelectedPosition = -1;

    public LanguageAdapter(Context context, String[] array){
        this.context=context;
        this.array=array;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.language_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(array[position]);
        holder.radioButton.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return array.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private RadioButton radioButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.text);
            radioButton=(RadioButton)itemView.findViewById(R.id.radio_btn);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
