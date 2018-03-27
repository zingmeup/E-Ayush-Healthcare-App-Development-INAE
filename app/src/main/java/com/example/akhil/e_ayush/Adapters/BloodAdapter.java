package com.example.akhil.e_ayush.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.R;

/**
 * Created by Akhil on 26-02-2018.
 */

public class BloodAdapter extends RecyclerView.Adapter<BloodAdapter.MyViewHolder>{

    private final String[] array;
    private final Context context;
    private final FragmentCallbacks connect;
    private int[] img={R.drawable.group39, R.drawable.group40, R.drawable.group41, R.drawable.group42, R.drawable.group43,
            R.drawable.group44, R.drawable.group45, R.drawable.group46};

    public BloodAdapter(String[] array, Context context, FragmentCallbacks connect){
        this.array=array;
        this.context=context;
        this.connect=connect;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.blood_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(array[position]+" Blood Group");
        holder.imageView.setImageResource(img[position]);
    }

    @Override
    public int getItemCount() {
        return array.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView text;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.blood_view_txt);
            imageView=(ImageView)itemView.findViewById(R.id.blood_view_img);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String group=array[getAdapterPosition()];
            connect.nextFrag(group,3);
        }
    }
}
