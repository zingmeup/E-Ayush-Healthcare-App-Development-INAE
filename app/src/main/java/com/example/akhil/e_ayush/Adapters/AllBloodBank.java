package com.example.akhil.e_ayush.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akhil.e_ayush.Models.BloodBank;
import com.example.akhil.e_ayush.R;

import java.util.ArrayList;

/**
 * Created by Akhil on 22-03-2018.
 */

public class AllBloodBank extends RecyclerView.Adapter<AllBloodBank.MyViewHoler> {
    private Context context;
    private ArrayList<BloodBank> bloodBanks;

    public AllBloodBank(Context context, ArrayList<BloodBank> bloodBanks){
        this.context=context;
        this.bloodBanks=bloodBanks;
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.blood_bank_item,parent,false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {
        String a1=bloodBanks.get(position).getName();
        String a2=bloodBanks.get(position).getAddress();
        String a3=bloodBanks.get(position).getEmail();
        String a4=bloodBanks.get(position).getMob();
        String a5=bloodBanks.get(position).getAvail();
        if(!a1.equals("")){
            holder.t1.setText(a1);
        }
        if(!a2.equals("")){
            holder.t2.setText(a2);
        }
        if(!a3.equals("")){
            holder.t3.setText(a3);
        }
        if(!a4.equals("")){
            holder.t4.setText(a4);
        }
        if(!a5.equals("")){
            if(a5.equals("YES")){
                holder.t5.setBackgroundResource(R.drawable.bloodyes);
            }
            else {
                holder.t5.setBackgroundResource(R.drawable.bloodno);
            }
            holder.t5.setText(a5);
        }
    }

    @Override
    public int getItemCount() {
        return bloodBanks.size();
    }

    public class MyViewHoler extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5;
        public MyViewHoler(View itemView) {
            super(itemView);

            t1=itemView.findViewById(R.id.blood_bank_item1);
            t2=itemView.findViewById(R.id.blood_bank_item2);
            t3=itemView.findViewById(R.id.blood_bank_item3);
            t4=itemView.findViewById(R.id.blood_bank_item4);
            t5=itemView.findViewById(R.id.blood_bank_item5);
        }
    }
}
