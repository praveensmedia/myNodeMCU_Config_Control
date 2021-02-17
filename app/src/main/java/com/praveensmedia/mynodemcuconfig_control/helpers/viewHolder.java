package com.praveensmedia.mynodemcuconfig_control.helpers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praveensmedia.mynodemcuconfig_control.R;

public class viewHolder extends RecyclerView.ViewHolder {
    View view;
    public viewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setContent(String Name,String sName){
        TextView name =(TextView)view.findViewById(R.id.name);
        TextView nameS =(TextView)view.findViewById(R.id.serviceName);
        name.setText(Name);
        nameS.setText(sName);
    }
}
