package com.example.headhunter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.headhunter.models.Categoria;

import java.util.List;

public class CategoriaSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context context;
    List<Categoria> items;

    public CategoriaSpinnerAdapter(Context context, List<Categoria> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setText(items.get(position).titulo);
        return view;
    }
}
