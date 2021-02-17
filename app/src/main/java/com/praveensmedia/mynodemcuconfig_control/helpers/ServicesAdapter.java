package com.praveensmedia.mynodemcuconfig_control.helpers;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praveensmedia.mynodemcuconfig_control.R;
import com.praveensmedia.mynodemcuconfig_control.helpers.viewHolder;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<viewHolder> {
 ArrayList<NsdServiceInfo> nsdInfoList;

    public ServicesAdapter(ArrayList<NsdServiceInfo> nsdList, Context context) {
        this.nsdInfoList = nsdList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        //holder.image.setImageResource(R.drawable.planetimage);
        NsdServiceInfo service = nsdInfoList.get(position);
        holder.setContent(service.getServiceName(),service.getServiceType());
    }

    @Override
    public int getItemCount() {
        return nsdInfoList.size();
    }

   /* public static class PlanetViewHolder extends RecyclerView.ViewHolder{

        protected ImageView image;
        protected TextView text;

        public PlanetViewHolder(View itemView) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.image_id);
            text= (TextView) itemView.findViewById(R.id.text_id);
        }
    }*/
}
