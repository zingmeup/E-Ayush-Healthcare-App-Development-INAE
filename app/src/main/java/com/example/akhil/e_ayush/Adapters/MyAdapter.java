package com.example.akhil.e_ayush.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akhil.e_ayush.Models.UserAppoinment;
import com.example.akhil.e_ayush.R;

import java.util.ArrayList;

/**
 * Created by Avinash on 26-02-2018.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {
    Context context;
    ArrayList<UserAppoinment> userAppoinments;

    public MyAdapter(Context context,ArrayList<UserAppoinment> userAppoinments) {
        this.context = context;
        this.userAppoinments=userAppoinments;
    }

    @Override
    public MyAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.appoint,parent,false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.CustomViewHolder holder, int position) {
        holder.hosName.setText(userAppoinments.get(position).getHospital());
        holder.doctorname.setText(userAppoinments.get(position).getDoc_name());
        if(userAppoinments.get(position).isConfirmed()) {
            holder.category.setText("Appointment Confirmed");
        }
        else{
            holder.category.setText("Appointment Not Confirmed");
        }
        holder.timings.setText(userAppoinments.get(position).getTimeing());
        holder.date.setText(userAppoinments.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return userAppoinments.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorname,category,timings,date,hosName;
        public CustomViewHolder(View itemView) {
            super(itemView);
            hosName=itemView.findViewById(R.id.app_hosName);
            doctorname = itemView.findViewById(R.id.recycler_layout_doctorname);
            category = itemView.findViewById(R.id.recycler_layout_specialisation);
            timings = itemView.findViewById(R.id.recycler_layout_timings);
            date = itemView.findViewById(R.id.recycler_layout_date);
        }
    }
}
