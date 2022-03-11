package com.developers.serenity.oceansmith1;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InfoAdapter extends ArrayAdapter <InfoClass> {

    private final Context context;
    private final int layoutResourceId;
    private ArrayList<InfoClass> arrayList;

    public InfoAdapter(Context context1, int layoutResourceId1, ArrayList<InfoClass> arrayList1){
        super(context1, layoutResourceId1, arrayList1);
        this.context = context1;
        this.layoutResourceId = layoutResourceId1;
        this.arrayList = arrayList1;
    }

    public void setListData(ArrayList<InfoClass> classy) {
        this.arrayList = classy;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView (int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.img = view.findViewById(R.id.img);
            holder.name = view.findViewById(R.id.name);
            holder.hb = view.findViewById(R.id.hb);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        InfoClass cl = arrayList.get(position);

        holder.name.setText(cl.getName());
        if(cl.getHabitat().matches("non")){
            holder.hb.setVisibility(View.GONE);
        } else {
            holder.hb.setText(cl.getHabitat());
        }

        Picasso.get()
                .load(cl.getImg())
                .fit()
                .placeholder(R.color.blue_dark)
                .centerInside()
                .into(holder.img);

        return view;
    }

    private static class ViewHolder {
        private TextView name, hb;
        private ImageView img;
    }

}
