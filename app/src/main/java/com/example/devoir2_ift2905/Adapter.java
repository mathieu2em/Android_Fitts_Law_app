package com.example.devoir2_ift2905;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Adapter extends RecyclerView.Adapter {

    String[] items;
    String[] items2;

    public Adapter(String[] items, String[] items2) {
        this.items = items;
        this.items2 = items2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new ItemHolder(row);
    }

    // c'est ca qui met les trucs dans les textview
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ItemHolder)viewHolder).textView1.setText(items[i]);
        ((ItemHolder)viewHolder).textView2.setText(items2[i]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        TextView textView2;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}
