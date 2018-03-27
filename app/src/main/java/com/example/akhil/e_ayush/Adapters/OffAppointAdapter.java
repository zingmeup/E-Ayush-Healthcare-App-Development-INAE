package com.example.akhil.e_ayush.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.Models.Hospitals;
import com.example.akhil.e_ayush.Models.OffHospital;
import com.example.akhil.e_ayush.R;

import java.util.ArrayList;

/**
 * Created by Akhil on 22-03-2018.
 */

public class OffAppointAdapter extends RecyclerView.Adapter<OffAppointAdapter.MyViewHolder>{
    private final Context context;
    private final int i;
    private ArrayList<OffHospital.MyObject> hospitals;

    public OffAppointAdapter(Context context, ArrayList<OffHospital.MyObject> hospitals, int i){
        this.context=context;
        this.hospitals=hospitals;
        this.i=i;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.hospital_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.km.setText(hospitals.get(position).distance+" KM");
        holder.name.setText(hospitals.get(position).Hospital_Name);
        holder.address.setText(hospitals.get(position).Location);
        if(hospitals.get(position).cell_no=="0"){
            holder.phone.setText("9460553853");
        }
        else {
            holder.phone.setText(String.valueOf(hospitals.get(position).cell_no));
        }
        holder.book.setText("");
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView km,name,address,phone,book;
        public MyViewHolder(View itemView) {
            super(itemView);
            km=itemView.findViewById(R.id.hos_km);
            name=itemView.findViewById(R.id.hospital_name);
            address=itemView.findViewById(R.id.address);
            phone=itemView.findViewById(R.id.textView3);
            book=itemView.findViewById(R.id.hos_bookNow);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
